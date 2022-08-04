package rooftopgreenlight.urbanisland.domain.rooftop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImageType;
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
    public void createGreenRooftop(String width, String explainContent, String refundContent, String roleContent,
                                   LocalDateTime startTime, LocalDateTime endTime, RooftopPeopleCount peopleCount,
                                   RooftopAddress rooftopAddress, List<MultipartFile> normalFiles, List<MultipartFile> structureFiles,
                                   List<Integer> details, List<String> options, List<Integer> prices, Long memberId) {

        Rooftop rooftop = getRooftop(width, explainContent, refundContent, roleContent, startTime,
                endTime, peopleCount, rooftopAddress, RooftopType.GREEN);

        saveRooftopImages(normalFiles, structureFiles, rooftop);
        saveRooftopDetails(details, rooftop);
        saveRooftopOptions(options, prices, rooftop);
        rooftop.changeMember(memberService.findById(memberId));

        rooftopRepository.save(rooftop);

    }

    /**
     * RooftopOption 저장
     */
    private void saveRooftopOptions(List<String> options, List<Integer> prices, Rooftop rooftop) {
        List<RooftopOption> rooftopOptions = rooftop.getRooftopOptions();
        IntStream.range(0, options.size()).forEach(i -> {
            RooftopOption rooftopOption = RooftopOption.of(options.get(i), prices.get(i));
            rooftopOption.changeRooftop(rooftop);
            rooftopOptions.add(rooftopOption);
        });
    }


    /**
     * RooftopDetail 저장
     */
    private void saveRooftopDetails(List<Integer> details, Rooftop rooftop) {
        List<RooftopDetail> rooftopDetails = rooftop.getRooftopDetails();
        details.stream()
                .map(RooftopDetail::of)
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
                .map(file -> fileService.createRooftopImage(file, RooftopImageType.NORMAL))
                .forEach(rooftopImage -> {
                    rooftopImage.changeRooftop(rooftop);
                    rooftopImages.add(rooftopImage);
                });

        structureFiles.parallelStream()
                .map(file -> fileService.createRooftopImage(file, RooftopImageType.STRUCTURE))
                .forEach(rooftopImage -> {
                    rooftopImage.changeRooftop(rooftop);
                    rooftopImages.add(rooftopImage);
                });
    }

    private Rooftop getRooftop(String width, String explainContent, String refundContent, String roleContent,
                               LocalDateTime startTime, LocalDateTime endTime, RooftopPeopleCount peopleCount,
                               RooftopAddress rooftopAddress, RooftopType rooftopType) {
        return Rooftop.createRooftop()
                .width(width)
                .explainContent(explainContent)
                .refundContent(refundContent)
                .roleContent(roleContent)
                .startTime(startTime)
                .endTime(endTime)
                .peopleCount(peopleCount)
                .rooftopAddress(rooftopAddress)
                .rooftopType(rooftopType)
                .build();
    }
}
