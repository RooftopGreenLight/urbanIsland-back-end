package rooftopgreenlight.urbanisland.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.exception.FileIOException;
import rooftopgreenlight.urbanisland.api.common.properties.AwsS3Properties;
import rooftopgreenlight.urbanisland.api.common.properties.ProfileDirProperties;
import rooftopgreenlight.urbanisland.api.controller.dto.FileResponse;
import rooftopgreenlight.urbanisland.domain.exception.NotFoundMemberException;
import rooftopgreenlight.urbanisland.domain.exception.NotFoundProfileException;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;
import rooftopgreenlight.urbanisland.domain.file.entity.QProfile;
import rooftopgreenlight.urbanisland.domain.file.repository.ProfileRepository;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.repository.MemberRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static rooftopgreenlight.urbanisland.domain.file.entity.QProfile.profile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    private final AmazonS3Client amazonS3Client;
    private final AwsS3Properties awsS3Properties;
    private final ProfileDirProperties profileDirProperties;

    /**
     * 프로필 저장 및 DB 저장
     */
    @Transactional
    public FileResponse saveProfile(MultipartFile file, Long memberId) {
        Member member = findMember(memberId);

        String ext = getExt(file.getOriginalFilename());
        String storeFilename = createStoreFilename(ext);
        String fullStorePath = getFullStorePath(storeFilename);
        String fileUrl = "";

        try {
            file.transferTo(new File(fullStorePath));
            fileUrl = uploadS3(new File(fullStorePath), storeFilename);

            Predicate predicate = profile.member.id.eq(member.getId());
            Profile profile = profileRepository.findOne(predicate).orElse(null);

            if (profile != null) {
                deleteFileS3(profile.getStoreFilename());
                profile.changeProfile(file.getContentType(), file.getOriginalFilename(), storeFilename, fileUrl);
            } else {
                saveProfileInfo(file.getOriginalFilename(), member, ext, storeFilename, fileUrl);
            }

            deleteSavedProfile(fullStorePath);
        } catch (IOException e) {
            throw new FileIOException("회원 프로필 저장 오류");
        }

        return FileResponse.of(member.getId(), ext, fileUrl);
    }

    @Transactional
    public void deleteProfile(Long memberId) {
        Profile findProfile = profileRepository.findOne(QProfile.profile.member.id.eq(memberId)).orElseThrow(() -> {
            throw new NotFoundProfileException("프로필을 찾을 수 없습니다.");
        });

        profileRepository.delete(findProfile);
        deleteFileS3(findProfile.getStoreFilename());
    }

    /**
     * 파일 정보 Bytes -> String
     */
//    private String getFileString(String fullStorePath) {
//        byte[] fileBytes = null;
//        try {
//            InputStream inputStream = new FileInputStream(fullStorePath);
//            fileBytes = inputStream.readAllBytes();
//
//        } catch (IOException e) {
//            throw new FileIOException("File Bytes Encoding 오류");
//        }
//         return Base64.getEncoder().encodeToString(fileBytes);
//    }

    /**
     * 회원 찾기
     */
    private Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NotFoundMemberException("회원을 찾을 수 없습니다.");
        });
        return member;
    }

    /**
     * 기존에 저장된 프로필이 존재하면 저장 후 삭제
     */
    private void deleteSavedProfile(String fullStorePath) {
        File savedProfile = new File(fullStorePath);

        if (savedProfile.exists()) savedProfile.delete();
    }

    /**
     * profile 생성
     */
    public void saveProfileInfo(String originalFilename, Member member, String ext, String storeFilename, String fileUrl) {
        Profile saveProfile = Profile.createProfile()
                .storeFilename(storeFilename)
                .uploadFilename(originalFilename)
                .fileUrl(fileUrl)
                .type(ext)
                .build();

        saveProfile.updateMember(member);
        profileRepository.save(saveProfile);
    }


    /**
     * 저장할 파일 이름 생성
     */
    public String createStoreFilename(String ext) {
        return UUID.randomUUID().toString() + "." + ext;
    }

    /**
     * 저장될 파일의 저장 경로 가져오기
     */
    public String getFullStorePath(String storeFilename) {
        return createAndGetProfileDir() + "/" + storeFilename;
    }

    /**
     * profile 저장 Dir 생성
     */
    private String createAndGetProfileDir() {
        String path = Paths.get("").toAbsolutePath() + "/" + profileDirProperties.getProfileDir();

        File dir = new File(path);
        if(!dir.exists()) dir.mkdir();

        return path;
    }

    /**
     * 파일 확장자 가져오기
     */
    private String getExt(String originFilename) {
        return originFilename.substring(originFilename.lastIndexOf(".") + 1);
    }

    /**
     * S3 파일 업로드
     */
    private String uploadS3(File uploadFile, String filename) {
        amazonS3Client.putObject(new PutObjectRequest(awsS3Properties.getBucket(), filename, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(awsS3Properties.getBucket(), filename).toString();
    }

    /**
     * S3 파일 삭제
     */
    private void deleteFileS3(String filename) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(awsS3Properties.getBucket(), filename));
    }
}
