package rooftopgreenlight.urbanisland.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.exception.FileIOException;
import rooftopgreenlight.urbanisland.domain.common.properties.AwsS3Properties;
import rooftopgreenlight.urbanisland.domain.common.properties.FileDirProperties;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final MemberService memberService;
    private final AmazonS3Client amazonS3Client;
    private final AwsS3Properties awsS3Properties;
    private final FileDirProperties fileDirProperties;

    /**
     * 프로필 저장 및 DB 저장
     */
    @Transactional
    public Profile saveProfile(MultipartFile file, Long memberId) {
        Member findMember = memberService.findByIdWithProfile(memberId);

        String ext = getExt(file.getOriginalFilename());
        String storeFilename = createStoreFilename(ext);
        String fullStorePath = getFullStorePath(storeFilename);
        String fileUrl = "";
        Profile profile = null;

        try {
            file.transferTo(new File(fullStorePath));
            fileUrl = uploadS3(new File(fullStorePath), storeFilename);
            profile = findMember.getProfile();

            if (profile != null) {
                deleteFileS3(profile.getStoreFilename());
                profile.changeFile(file.getContentType(), file.getOriginalFilename(), storeFilename, fileUrl);
            } else {
                saveProfileInfo(file.getOriginalFilename(), findMember, ext, storeFilename, fileUrl);
            }

            deleteSavedProfile(fullStorePath);
        } catch (IOException e) {
            throw new FileIOException("회원 프로필 저장 오류");
        }

        return profile;
    }

    @Transactional
    public void deleteProfile(Long memberId) {
        Member findMember = memberService.findByIdWithProfile(memberId);

        deleteFileS3(findMember.getProfile().getStoreFilename());
        findMember.changeProfile(null);
    }

    /**
     * 녹화 옥상 사진 저장 및 DB 저장
     */
    @Transactional
    public Object createImage(MultipartFile file, ImageType imageType, ImageName imageName) {
        String ext = getExt(file.getOriginalFilename());
        String storeFilename = createStoreFilename(ext);
        String fullStorePath = getFullStorePath(storeFilename);
        String fileUrl = "";

        try {
            file.transferTo(new File(fullStorePath));
            fileUrl = uploadS3(new File(fullStorePath), storeFilename);
            deleteSavedProfile(fullStorePath);
        } catch (IOException e) {
            throw new FileIOException("옥상 이미지 저장 오류");
        }

        if (imageName == ImageName.ROOFTOP) {
            return rooftopImage(file.getOriginalFilename(), ext, storeFilename, fileUrl, imageType);
        }

        return greenBeeImage(file.getOriginalFilename(), imageType, ext, storeFilename, fileUrl);
    }

    public void deleteRooftopImage(String rooftopStoreImage) {
        deleteFileS3(rooftopStoreImage);
    }


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

        member.changeProfile(saveProfile);
    }

    /**
     * RooftopImage 생성
     */
    public RooftopImage rooftopImage(String originalFilename, String ext, String storeFilename, String fileUrl, ImageType imageType) {
        return RooftopImage.createRooftopImage()
                .storeFilename(storeFilename)
                .uploadFilename(originalFilename)
                .fileUrl(fileUrl)
                .type(ext)
                .rooftopImageType(imageType)
                .build();
    }

    /**
     * greenBeeImage 생성
     */
    private GreenBeeImage greenBeeImage(String originalFilename, ImageType imageType, String ext, String storeFilename, String fileUrl) {
        return GreenBeeImage.createGreenBeeImage()
                .fileUrl(fileUrl)
                .uploadFilename(originalFilename)
                .storeFilename(storeFilename)
                .type(ext)
                .greenBeeImageType(imageType)
                .build();
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
        String path = Paths.get("").toAbsolutePath() + "/" + fileDirProperties.getDir();

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
