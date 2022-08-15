package rooftopgreenlight.urbanisland.domain.rooftop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundRooftopException;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.greenbee.service.GreenBeeService;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.*;
import rooftopgreenlight.urbanisland.domain.rooftop.repository.RooftopRepository;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.NGRooftopDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopPageDto;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RooftopService {

    private final FileService fileService;
    private final MemberService memberService;
    private final GreenBeeService greenBeeService;
    private final RooftopRepository rooftopRepository;
    private final RooftopGreeningApplyService greeningApplyService;

    /**
     * Rooftop 저장
     */
    @Transactional
    public void createGreenRooftop(String rooftopType, String width, String phoneNumber, String explainContent,
                                   String refundContent, String roleContent, String ownerContent, LocalTime startTime,
                                   LocalTime endTime, int totalPrice, int widthPrice, RooftopPeopleCount peopleCount,
                                   Address address, List<MultipartFile> normalFiles, List<MultipartFile> structureFiles,
                                   List<Integer> detailInfos, List<Integer> requiredItems, Integer deadLine,
                                   List<String> options, List<Integer> prices, List<Integer> counts, Long memberId) {

        int wPrice = rooftopType.equals("G") ? 0 : widthPrice;
        RooftopType type = rooftopType.equals("G") ? RooftopType.GREEN : RooftopType.NOT_GREEN;
        Progress progress = rooftopType.equals("G") ? Progress.ADMIN_WAIT : Progress.GREENBEE_WAIT;

        Rooftop rooftop = createRooftop(width, phoneNumber, explainContent, refundContent, roleContent, ownerContent,
                startTime, endTime, totalPrice, wPrice, peopleCount, address, deadLine);
        rooftop.changeProgress(progress);
        rooftop.changeRooftopType(type);

        saveRooftopImages(normalFiles, structureFiles, rooftop);

        if(rooftopType.equals("NG")) {
            saveRooftopDetails(requiredItems, RooftopDetailType.REQUIRED_ITEM, rooftop);
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


    public RooftopPageDto getNGRooftop(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Rooftop> rooftopPage = rooftopRepository.findByNGRooftopPage(RooftopType.NOT_GREEN, pageRequest);

        return RooftopPageDto.of(rooftopPage.getTotalPages(), rooftopPage.getTotalElements(), rooftopPage.getContent());
    }

    public NGRooftopDto getNGRooftopDetail(Long rooftopId) {
        Rooftop rooftop = getRooftop(rooftopId);
        return NGRooftopDto.of(rooftop, true);
    }

    @Transactional
    public void selectGreenBeeNGRooftop(Long rooftopId, Long memberId) {
        Rooftop rooftop = getRooftop(rooftopId);
        rooftop.changeProgress(Progress.GREENBEE_COMPLETED);
        GreenBee greenBee = greenBeeService.getGreenBee(memberId);
        // 신청 관리
        greeningApplyService.saveApply(rooftop, greenBee, memberId);
    }

    public void deleteNGRooftop(Long rooftopId) {
        rooftopRepository.deleteById(rooftopId);
    }

    @Transactional
    public void confirmGreenBeeNGRooftop(Long rooftopId, Long greenbeeId) {
        Rooftop rooftop = getRooftop(rooftopId);
        rooftop.changeProgress(Progress.GREENING_ACCEPTED);

        List<RooftopGreeningApply> greeningApplies = greeningApplyService.getNotSelectedApply(rooftopId);
        greeningApplies.forEach(apply -> {
            Progress progress = Objects.equals(apply.getGreenBeeMemberId(), greenbeeId)
                    ? Progress.GREENING_ACCEPTED : Progress.GREENING_REJECTED;
            apply.changeProgress(progress);
        });

    }

    @Transactional
    public void completeGreeningRooftop(Long rooftopId, Long memberId) {
        RooftopGreeningApply rooftopGreeningApply = greeningApplyService.completeGreeningRooftop(rooftopId, memberId);
        rooftopGreeningApply.changeProgress(Progress.ADMIN_COMPLETED);

        Rooftop findRooftop = getRooftop(rooftopId);
        findRooftop.changeProgress(Progress.ADMIN_COMPLETED);
        findRooftop.changeRooftopType(RooftopType.GREEN);
    }

    private Rooftop createRooftop(String width, String phoneNumber, String explainContent, String refundContent,
                               String roleContent, String ownerContent, LocalTime startTime, LocalTime endTime,
                               int totalPrice, int widthPrice, RooftopPeopleCount peopleCount, Address address, int deadLineType) {
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
                .views(0)
                .deadLineType(deadLineType)
                .build();
    }

    private Rooftop getRooftop(Long rooftopId) {
        Rooftop rooftop = rooftopRepository.findById(rooftopId).orElseThrow(() -> {
            throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");
        });
        return rooftop;
    }

}
