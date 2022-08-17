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
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopSearchCond;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopDto;
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
    public void createGreenRooftop(String rooftopType, Double width, String phoneNumber, String explainContent,
                                   String refundContent, String roleContent, String ownerContent, LocalTime startTime,
                                   LocalTime endTime, Integer totalPrice, Integer widthPrice, RooftopPeopleCount peopleCount,
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
        if(options != null && prices != null && counts != null) {
            IntStream.range(0, options.size()).forEach(i -> {
                RooftopOption rooftopOption = RooftopOption.of(options.get(i), prices.get(i), counts.get(i));
                rooftopOption.changeRooftop(rooftop);
                rooftopOptions.add(rooftopOption);
            });
        }
    }


    /**
     * RooftopDetail 저장
     */
    private void saveRooftopDetails(List<Integer> details, RooftopDetailType type, Rooftop rooftop) {
        List<RooftopDetail> rooftopDetails = rooftop.getRooftopDetails();
        if(details != null) {
            details.stream()
                    .map(detail -> RooftopDetail.of(detail, type))
                    .forEach(rooftopDetail -> {
                        rooftopDetail.changeRooftop(rooftop);
                        rooftopDetails.add(rooftopDetail);
                    });
        }
    }

    /**
     * RooftopImage 저장
     */
    private void saveRooftopImages(List<MultipartFile> normalFiles, List<MultipartFile> structureFiles, Rooftop rooftop) {
        List<RooftopImage> rooftopImages = rooftop.getRooftopImages();
        if(normalFiles != null) {
            normalFiles.parallelStream()
                    .map(file -> (RooftopImage) fileService.createImage(file, ImageType.NORMAL, ImageName.ROOFTOP))
                    .forEach(rooftopImage -> {
                        rooftopImage.changeRooftop(rooftop);
                        rooftopImages.add(rooftopImage);
                    });
        }
        if(structureFiles != null) {
            structureFiles.parallelStream()
                    .map(file -> (RooftopImage) fileService.createImage(file, ImageType.STRUCTURE, ImageName.ROOFTOP))
                    .forEach(rooftopImage -> {
                        rooftopImage.changeRooftop(rooftop);
                        rooftopImages.add(rooftopImage);
                    });
        }
    }


    /**
     * page 단위로 녹화되지 않은 옥상 정보 가져오기
     */
    public RooftopPageDto getNGRooftop(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Rooftop> rooftopPage = rooftopRepository.findByNGRooftopPage(RooftopType.NOT_GREEN, pageRequest);

        return RooftopPageDto.of(rooftopPage.getTotalPages(), rooftopPage.getTotalElements(), rooftopPage.getContent(), false);
    }

    /**
     * 녹화되지 않은 옥상 개별 조회
     */
    public RooftopDto getNGRooftopDetail(Long rooftopId) {
        Rooftop rooftop = findByRooftopId(rooftopId);
        return RooftopDto.of(rooftop, true);
    }

    /**
     * 그린비 -> 녹화할 옥상 선택하기
     */
    @Transactional
    public void selectGreenBeeNGRooftop(Long rooftopId, Long memberId) {
        Rooftop rooftop = findByRooftopId(rooftopId);
        rooftop.changeProgress(Progress.GREENBEE_COMPLETED);
        GreenBee greenBee = greenBeeService.getGreenBee(memberId);
        greeningApplyService.saveApply(rooftop, greenBee, memberId);
    }

    /**
     * 옥상 삭제
     */
    @Transactional
    public void deleteRooftop(Long rooftopId, boolean isNg) {
        rooftopRepository.deleteRooftopDetails(rooftopId);
        rooftopRepository.deleteRooftopOptions(rooftopId);
        List<RooftopImage> rooftopImages = rooftopRepository.findRooftopImagesByRooftopId(rooftopId);
        rooftopImages.forEach(rooftopImage -> fileService.deleteFileS3(rooftopImage.getStoreFilename()));
        rooftopRepository.deleteRooftopImages(rooftopId);

        if (isNg) greeningApplyService.deleteGreeningApplies(rooftopId);

        rooftopRepository.deleteRooftopsById(rooftopId);
    }

    /**
     * 신청한 그린비 목록 중 확정하기
     */
    @Transactional
    public void confirmGreenBeeNGRooftop(Long rooftopId, Long greenbeeId) {
        Rooftop rooftop = findByRooftopId(rooftopId);
        rooftop.changeProgress(Progress.GREENING_ACCEPTED);

        List<RooftopGreeningApply> greeningApplies = greeningApplyService.getNotSelectedApply(rooftopId);
        greeningApplies.forEach(apply -> {
            Progress progress = Objects.equals(apply.getGreenBeeMemberId(), greenbeeId)
                    ? Progress.GREENING_ACCEPTED : Progress.GREENING_REJECTED;
            apply.changeProgress(progress);
        });

    }

    /**
     * 녹화 완료(확정)하기
     */
    @Transactional
    public void completeGreeningRooftop(Long rooftopId, Long memberId) {
        RooftopGreeningApply rooftopGreeningApply = greeningApplyService.getRooftopApplyByGreenBeeId(rooftopId, memberId);
        rooftopGreeningApply.changeProgress(Progress.ADMIN_COMPLETED);

        Rooftop findRooftop = findByRooftopId(rooftopId);
        findRooftop.changeProgress(Progress.ADMIN_COMPLETED);
        findRooftop.changeRooftopType(RooftopType.GREEN);
    }

    /**
     * 옥상 Entity 만들기
     */
    private Rooftop createRooftop(Double width, String phoneNumber, String explainContent, String refundContent,
                               String roleContent, String ownerContent, LocalTime startTime, LocalTime endTime,
                               Integer totalPrice, Integer widthPrice, RooftopPeopleCount peopleCount,
                                  Address address, Integer deadLineType) {
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

    /**
     * 옥상 Id 기준으로 옥상 정보 가져오기
     */
    private Rooftop findByRooftopId(Long rooftopId) {
        Rooftop rooftop = rooftopRepository.findById(rooftopId).orElseThrow(() -> {
            throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");
        });
        return rooftop;
    }

    /**
     * Admin
     * 옥상 Progress 기준으로 옥상 정보 가져오기
     */
    public RooftopPageDto getRooftopPageByProgress(int page, Progress progress) {
        PageRequest pageRequest = PageRequest.of(page, 20);

        Page<Rooftop> rooftopPage = rooftopRepository.findRooftopPageByProgress(progress, pageRequest);
        return RooftopPageDto.of(rooftopPage.getTotalPages(), rooftopPage.getTotalElements(), rooftopPage.getContent(), true);
    }

    /**
     * Admin
     * 옥상 신청 승인
     */
    @Transactional
    public void acceptRooftop(long rooftopId) {
        Rooftop rooftop = findByRooftopId(rooftopId);
        rooftop.changeProgress(Progress.ADMIN_COMPLETED);
    }

    /**
     * Admin
     * 옥상 신청 거절
     */
    @Transactional
    public void rejectRooftop(long rooftopId) {
        deleteRooftop(rooftopId, false);
    }

    /**
     * Filter
     * 조건에 맞는 옥상 검색
     */
    public RooftopPageDto searchRooftopByCond(int page, RooftopSearchCond searchCond) {
        PageRequest pageRequest = PageRequest.of(page, 20);

        Page<Rooftop> rooftopPage = rooftopRepository.searchRooftopByCond(pageRequest, searchCond);

        return new RooftopPageDto().RooftopSearchPageDto(
                rooftopPage.getTotalPages(),
                rooftopPage.getTotalElements(),
                rooftopPage.getContent()
        );
    }
}
