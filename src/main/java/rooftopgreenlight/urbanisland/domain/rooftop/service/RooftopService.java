package rooftopgreenlight.urbanisland.domain.rooftop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.*;
import rooftopgreenlight.urbanisland.domain.rooftop.repository.RooftopRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RooftopService {

    private final FileService fileService;
    private final MemberService memberService;
    private final RooftopRepository rooftopRepository;

    /**
     * Rooftop 저장
     */
    @Transactional
    public void createGreenRooftop(String rooftopType, String width, String phoneNumber, String explainContent,
                                   String refundContent, String roleContent, String ownerContent, LocalDateTime startTime,
                                   LocalDateTime endTime, int totalPrice, int widthPrice, RooftopPeopleCount peopleCount,
                                   Address address, List<MultipartFile> normalFiles, List<MultipartFile> structureFiles,
                                   List<Integer> detailInfos, List<Integer> requiredItems, List<Integer> deadLines,
                                   List<String> options, List<Integer> prices, List<Integer> counts, Long memberId) {

        int wPrice = rooftopType.equals("G") ? 0 : widthPrice;
        RooftopType type = rooftopType.equals("G") ? RooftopType.GREEN : RooftopType.NOT_GREEN;
        RooftopProgress progress = rooftopType.equals("G") ? RooftopProgress.ADMIN_WAIT : RooftopProgress.GREENBEE_WAIT;

        Rooftop rooftop = getRooftop(width, phoneNumber, explainContent, refundContent, roleContent, ownerContent,
                startTime, endTime, totalPrice, wPrice, peopleCount, address, type, progress);

        saveRooftopImages(normalFiles, structureFiles, rooftop);

        if(rooftopType.equals("NG")) {
            saveRooftopDetails(requiredItems, RooftopDetailType.REQUIRED_ITEM, rooftop);
            saveRooftopDetails(deadLines, RooftopDetailType.DEADLINE, rooftop);
        }

        saveRooftopDetails(detailInfos, RooftopDetailType.DETAIL, rooftop);
        saveRooftopOptions(options, prices, counts, rooftop);
        rooftop.changeMember(memberService.findById(memberId));

        rooftopRepository.save(rooftop);
    }

    /**
     * RooftopOption 저장
     */
    private void saveRooftopOptions(List<String> options, List<Integer> prices, List<Integer> counts, Rooftop rooftop) {
        List<RooftopOption> rooftopOptions = rooftop.getRooftopOptions();
        IntStream.range(0, options.size()).forEach(i -> {
            RooftopOption rooftopOption = RooftopOption.of(options.get(i), prices.get(i), counts.get(i));
            rooftopOption.changeRooftop(rooftop);
            rooftopOptions.add(rooftopOption);
        });
    }


    /**
     * RooftopDetail 저장
     */
    private void saveRooftopDetails(List<Integer> details, RooftopDetailType type, Rooftop rooftop) {
        List<RooftopDetail> rooftopDetails = rooftop.getRooftopDetails();
        details.stream()
                .map(detail -> RooftopDetail.of(detail, type))
                .forEach(rooftopDetail -> {
                    rooftopDetail.changeRooftop(rooftop);
                    rooftopDetails.add(rooftopDetail);
                });
    }

    /**
     * RooftopImage 저장
     */
    private void saveRooftopImages(List<MultipartFile> normalFiles, List<MultipartFile> structureFiles, Rooftop rooftop) {
        List<RooftopImage> rooftopImages = rooftop.getRooftopImages();
        normalFiles.parallelStream()
                .map(file -> (RooftopImage) fileService.createImage(file, ImageType.NORMAL, ImageName.ROOFTOP))
                .forEach(rooftopImage -> {
                    rooftopImage.changeRooftop(rooftop);
                    rooftopImages.add(rooftopImage);
                });

        structureFiles.parallelStream()
                .map(file -> (RooftopImage) fileService.createImage(file, ImageType.STRUCTURE, ImageName.ROOFTOP))
                .forEach(rooftopImage -> {
                    rooftopImage.changeRooftop(rooftop);
                    rooftopImages.add(rooftopImage);
                });
    }


    private Rooftop getRooftop(String width, String phoneNumber, String explainContent, String refundContent,
                               String roleContent, String ownerContent, LocalDateTime startTime, LocalDateTime endTime,
                               int totalPrice, int widthPrice, RooftopPeopleCount peopleCount, Address address,
                               RooftopType rooftopType, RooftopProgress progress) {
        return Rooftop.createRooftop()
                .width(width)
                .phoneNumber(phoneNumber)
                .explainContent(explainContent)
                .refundContent(refundContent)
                .roleContent(roleContent)
                .ownerContent(ownerContent)
                .startTime(startTime)
                .endTime(endTime)
                .totalPrice(totalPrice)
                .widthPrice(widthPrice)
                .peopleCount(peopleCount)
                .address(address)
                .rooftopType(rooftopType)
                .rooftopProgress(progress)
                .build();
    }
}
