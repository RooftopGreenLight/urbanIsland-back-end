package rooftopgreenlight.urbanisland.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.exception.ExistObjectException;
import rooftopgreenlight.urbanisland.exception.NotFoundGreenBeeException;
import rooftopgreenlight.urbanisland.domain.file.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.file.ImageName;
import rooftopgreenlight.urbanisland.domain.file.ImageType;
import rooftopgreenlight.urbanisland.domain.greenbee.GreenBee;
import rooftopgreenlight.urbanisland.repository.greenbee.GreenBeeRepository;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeDto;
import rooftopgreenlight.urbanisland.domain.member.Authority;
import rooftopgreenlight.urbanisland.domain.member.Member;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenBeeService {

    private final FileService fileService;
    private final MemberService memberService;
    private final GreenBeeRepository greenBeeRepository;

    /**
     * 회원 -> 그린비 변경
     * 그린비 정보 등록
     */
    @Transactional
    public void saveGreenBee(Long memberId, String officeNumber, String content, Address address,
                             List<MultipartFile> normalFiles, MultipartFile confirmationFile) {

        isGreenBeeJoinValid(memberId);

        Member member = memberService.findById(memberId);

        GreenBee greenBee = createGreenBee(officeNumber, content, address);
        registerImageFile(normalFiles, confirmationFile, greenBee);

        greenBee.changeMember(member);
        greenBee.changeProgress(Progress.ADMIN_WAIT);

        greenBeeRepository.save(greenBee);
    }

    /**
     * 내 그린비 정보 가져오기
     */
    public GreenBee getMyGreenBeeInfo(Long memberId) {
        return greenBeeRepository.findByIdWithImages(memberId).orElseThrow(() -> {
            throw new NotFoundGreenBeeException("그린비 정보를 찾을 수 없습니다.");
        });

    }

    private GreenBee createGreenBee(String officeNumber, String content, Address address) {
        return GreenBee.createGreenBee()
                .officeNumber(officeNumber)
                .content(content)
                .address(address)
                .build();
    }

    /**
     * 승인 대기 중인 그린비 정보 가져오기
     */
    public Page<GreenBeeDto> getWaitGreenBeeWaits(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "createdDate"));

        return greenBeeRepository.getWaitInfoWithCfImage(pageRequest);
    }

    /**
     * 승인 대기 중인 그린비 승인
     */
    @Transactional
    public void acceptGreenBee(long memberId) {
        GreenBee greenBee = getMyGreenBeeInfo(memberId);

        Member member = greenBee.getMember();
        if (member.getAuthority() == Authority.ROLE_ROOFTOPOWNER) {
            member.changeAuthority(Authority.ROLE_ALL);
        } else {
            member.changeAuthority(Authority.ROLE_GREENBEE);
        }

        greenBee.changeProgress(Progress.ADMIN_COMPLETED);
    }

    /**
     * 승인 대기 중인 그린비 거절
     */
    @Transactional
    public void rejectGreenBee(long memberId) {
        GreenBee greenBee = greenBeeRepository.findByMemberIdWithImages(memberId).orElseThrow(() -> {
            throw new NotFoundGreenBeeException("그린비 요청 정보를 찾을 수 없습니다.");
        });

        List<GreenBeeImage> greenBeeImages = greenBee.getGreenBeeImages();
        greenBeeImages.forEach(greenBeeImage -> fileService.deleteFileS3(greenBeeImage.getStoreFilename()));

        greenBeeRepository.delete(greenBee);
    }

    /**
     * 그린비 정보 수정
     */
    @Transactional
    public void editGreenBeeInfo(Long memberId, String officeNumber, String content, List<String> deleteImages, List<MultipartFile> addImages) {
        GreenBee greenBee = getMyGreenBeeInfo(memberId);
        greenBee.changeGreenBeeInfo(officeNumber, content);

        if(addImages != null) {
            registerImageFile(addImages, null, greenBee);
        }

        if(deleteImages != null) {
            greenBeeRepository.deleteImagesByFileName(deleteImages);
            deleteImages.forEach(fileService::deleteFileS3);
        }
    }

    private void registerImageFile(List<MultipartFile> normalFiles, MultipartFile confirmationFile, GreenBee greenBee) {
        if (normalFiles != null) {
            normalFiles.stream().parallel()
                    .map(file -> (GreenBeeImage) fileService.createImage(file, ImageType.NORMAL, ImageName.GREENBEE))
                    .forEach(image -> {
                        image.changeGreenBee(greenBee);
                        greenBee.addGreenBeeImage(image);
                    });
        }

        if (confirmationFile != null) {
            GreenBeeImage greenBeeConfirmationImage =
                    (GreenBeeImage) fileService.createImage(confirmationFile, ImageType.CONFIRMATION, ImageName.GREENBEE);

            greenBeeConfirmationImage.changeGreenBee(greenBee);
            greenBee.addGreenBeeImage(greenBeeConfirmationImage);
        }
    }

    private void isGreenBeeJoinValid(Long memberId) {
        if (greenBeeRepository.existsByMemberId(memberId)) {
            throw new ExistObjectException("그린비 등록을 할 수 없는 상태입니다.");
        }
    }

}
