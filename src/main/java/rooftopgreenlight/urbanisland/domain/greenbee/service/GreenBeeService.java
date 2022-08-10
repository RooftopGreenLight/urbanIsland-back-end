package rooftopgreenlight.urbanisland.domain.greenbee.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.controller.dto.GreenBeeInfoResponse;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundGreenBeeInfoException;
import rooftopgreenlight.urbanisland.domain.file.entity.GreenBeeImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.QGreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.repository.GreenBeeRepository;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;

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

        Member member = memberService.findById(memberId);

        GreenBee greenBee = createGreenBee(officeNumber, content, address);
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

        greenBee.changeMember(member);
        greenBee.changeProgress(Progress.WAIT);

        greenBeeRepository.save(greenBee);
    }

    /**
     * 내 그린비 정보 가져오기
     */
    public GreenBee getMyGreenBeeInfo(Long memberId) {
        return greenBeeRepository.findByIdWithImages(memberId).orElseThrow(() -> {
            throw new NotFoundGreenBeeInfoException("그린비 정보를 찾을 수 없습니다.");
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
}
