package rooftopgreenlight.urbanisland.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.model.Address;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.exception.ExistObjectException;
import rooftopgreenlight.urbanisland.exception.NoMatchMemberIdException;
import rooftopgreenlight.urbanisland.exception.NotFoundRooftopException;
import rooftopgreenlight.urbanisland.exception.NotFoundRooftopReviewException;
import rooftopgreenlight.urbanisland.domain.file.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.ImageName;
import rooftopgreenlight.urbanisland.domain.file.ImageType;
import rooftopgreenlight.urbanisland.domain.greenbee.GreenBee;
import rooftopgreenlight.urbanisland.domain.member.Member;
import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopDetail;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopDetailType;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopGreeningApply;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopOption;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopPeopleCount;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopReview;
import rooftopgreenlight.urbanisland.domain.rooftop.RooftopType;
import rooftopgreenlight.urbanisland.repository.rooftop.RooftopRepository;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopDto;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopPageDto;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopSearchCond;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
                                   MultipartFile mainFile, List<Integer> detailInfos, List<Integer> requiredItems, Integer deadLine,
                                   List<String> options, List<Integer> prices, List<Integer> counts, Long memberId) {

        int wPrice = rooftopType.equals("G") ? 0 : widthPrice;
        RooftopType type = rooftopType.equals("G") ? RooftopType.GREEN : RooftopType.NOT_GREEN;
        Progress progress = rooftopType.equals("G") ? Progress.ADMIN_WAIT : Progress.GREENBEE_WAIT;

        Rooftop rooftop = createRooftop(width, phoneNumber, explainContent, refundContent, roleContent, ownerContent,
                startTime, endTime, totalPrice, wPrice, peopleCount, address, deadLine);
        rooftop.changeProgress(progress);
        rooftop.changeRooftopType(type);

        saveRooftopImages(normalFiles, structureFiles, mainFile, rooftop);

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
    private void saveRooftopImages(List<MultipartFile> normalFiles, List<MultipartFile> structureFiles, MultipartFile mainFile, Rooftop rooftop) {
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
        if(mainFile != null) {
            RooftopImage rooftopImage = (RooftopImage) fileService.createImage(mainFile, ImageType.MAIN, ImageName.ROOFTOP);
            rooftopImage.changeRooftop(rooftop);
            rooftopImages.add(rooftopImage);
        }
    }


    /**
     * page 단위로 녹화되지 않은 옥상 정보 가져오기
     */
    public RooftopPageDto getNGRooftop(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Rooftop> rooftopPage = rooftopRepository.findByNGRooftopPage(RooftopType.NOT_GREEN,
                List.of(Progress.GREENBEE_WAIT, Progress.GREENBEE_COMPLETED), pageRequest);
        return new RooftopPageDto().RooftopSearchPageDto(rooftopPage.getTotalPages(), rooftopPage.getTotalElements(), rooftopPage.getContent(), "NG");
    }

    /**
     * 옥상 개별 조회
     */
    @Cacheable(value = "rooftop", key = "#p0", condition = "#type == 'G'")
    public RooftopDto getRooftopDetail(Long rooftopId, String type) {
        Rooftop rooftop = rooftopRepository.findRooftopWithAll(rooftopId).orElseThrow(() -> {
            throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");
        });
        if(type.equals("G"))
            return RooftopDto.getRooftopDto(rooftop);
        return RooftopDto.getNGRooftopDto(rooftop, true);
    }

    /**
     * 그린비 -> 녹화할 옥상 선택하기
     */
    @Transactional
    public void selectGreenBeeNGRooftop(Long rooftopId, Long memberId) {
        if(greeningApplyService.isExistRooftopApplyByGreenBeeId(rooftopId, memberId))
            throw new ExistObjectException("이미 신청을 완료하였습니다.");

        Rooftop rooftop = findByRooftopId(rooftopId);
        rooftop.changeProgress(Progress.GREENBEE_COMPLETED);
        GreenBee greenBee = greenBeeService.getMyGreenBeeInfo(memberId);
        greeningApplyService.saveApply(rooftop, greenBee, memberId);
    }

    /**
     * 옥상 삭제
     */
    @Transactional
    public void deleteRooftop(Long memberId, Long rooftopId, boolean isNg) {
        Rooftop findRooftop = rooftopRepository.findRooftopWithMember(rooftopId).orElseThrow(() -> {
                    throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");});

        if(isNg && !findRooftop.getCreatedBy().equals(String.valueOf(memberId))) {
            throw new NoMatchMemberIdException("회원 정보가 일치하지 않습니다.");
        }

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
    public void confirmGreenBeeNGRooftop(Long rooftopId, Long greenbeeId, Long memberId) {
        Rooftop rooftop = findByRooftopId(rooftopId);
        if(!rooftop.getCreatedBy().equals(String.valueOf(memberId)))
            throw new NoMatchMemberIdException("권한이 없습니다.");
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
        if(!rooftopGreeningApply.getGreeningProgress().equals(Progress.GREENING_ACCEPTED))
            throw new NotFoundRooftopException("옥상 녹화가 거절되었거나 존재하지 않는 옥상입니다.");

        rooftopGreeningApply.changeProgress(Progress.ADMIN_COMPLETED);
        Rooftop findRooftop = findByRooftopId(rooftopId);
        findRooftop.changeProgress(Progress.ADMIN_COMPLETED);
        findRooftop.changeRooftopType(RooftopType.GREEN);
        rooftopRepository.deleteRooftopRequiredItemOptions(rooftopId);
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
    public Rooftop findByRooftopId(Long rooftopId) {
        Rooftop rooftop = rooftopRepository.findById(rooftopId).orElseThrow(() -> {
            throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");
        });
        return rooftop;
    }

    /**
     * 옥상 Id 기준으로 옥상 정보 가져오기
     */
    private Rooftop findByRooftopIdWithReview(Long rooftopId) {
        Rooftop rooftop = rooftopRepository.findByIdWithReview(rooftopId).orElseThrow(() -> {
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
        deleteRooftop(null, rooftopId, false);
    }

    /**
     * Filter
     * 조건에 맞는 옥상 검색
     */
    public RooftopPageDto searchRooftopByCond(int page, String type, RooftopSearchCond searchCond) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Rooftop> rooftopPage = type.equals("G") ?
                rooftopRepository.searchRooftopByCond(pageRequest, searchCond)
                : rooftopRepository.searchNGRooftopByCond(pageRequest, searchCond);

        return new RooftopPageDto().RooftopSearchPageDto(
                rooftopPage.getTotalPages(),
                rooftopPage.getTotalElements(),
                rooftopPage.getContent(),
                type
        );
    }

    /**
     * Review 생성
     */
    @Transactional
    public void createReview(final Long memberId, final Long rooftopId, final String content, final int grade) {

        Rooftop rooftop = findByRooftopIdWithReview(rooftopId);

        isValidMember(memberId, rooftop);

        Member findMember = memberService.findById(memberId);
        RooftopReview review = createReview(content, grade, findMember);

        review.addRooftop(rooftop);
        rooftop.addGrade(grade); // 평점 계산
    }

    /**
     * Review 삭제
     */
    @Transactional
    public void deleteReview(final Long memberId, final Long rooftopId, final Long reviewId) {
        Rooftop findRooftop = findByRooftopId(rooftopId);
        RooftopReview findRooftopReview = rooftopRepository.findRooftopReviewByRooftopReviewId(reviewId).orElseThrow(
                () -> {
                    throw new NotFoundRooftopReviewException("옥상 리뷰를 찾을 수 없습니다.");
                }
        );

        if (!isDeleteRooftopReviewValidation(memberId, findRooftopReview)) {
            throw new NoMatchMemberIdException("MemberId가 일치하지 않아 Review를 삭제할 수 없습니다.");
        }

        findRooftop.minusGrade(findRooftopReview.getGrade());
        rooftopRepository.deleteRooftopReviewByRooftopReviewId(reviewId);
    }

    /**
     * Review 조회
     */
    public Page<RooftopReview> findByMyRooftopReview(final Long memberId, final int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));

        return rooftopRepository.findRooftopReviewPageByMemberId(memberId, pageRequest);
    }

    /**
     * 녹화 완료된 옥상 중 관리자 결과 기다리는 옥상 리스트 조회
     */
    public List<RooftopDto> getGreenRooftopByMemberId(Long memberId) {
        List<Progress> progress = Arrays.asList(Progress.ADMIN_WAIT, Progress.ADMIN_COMPLETED);
        List<Rooftop> rooftopList = rooftopRepository.findGreenRooftopByMemberId(memberId, progress);
        return rooftopList.stream().map(rooftop -> {
            LocalDateTime date = rooftop.getRooftopProgress() == Progress.ADMIN_WAIT ? rooftop.getCreatedDate() : rooftop.getLastModifiedDate();
            return RooftopDto.getRooftopStatusDto(rooftop.getId(), rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(),
                    rooftop.getAddress().getDetail(), rooftop.getRooftopProgress().name(), date);
        }).collect(Collectors.toList());
    }

    /**
     * 녹화되지 않은 옥상 목록 조회
     */
    public List<RooftopDto> getNGGreenRooftopByMemberId(Long memberId) {
        List<Progress> progress = Arrays.asList(Progress.GREENBEE_WAIT, Progress.GREENBEE_COMPLETED, Progress.GREENING_ACCEPTED);
        List<Rooftop> rooftopList = rooftopRepository.findNGRooftopByMemberId(memberId, progress);

        if(rooftopList != null)
            return rooftopList.stream().map(rooftop ->
                RooftopDto.getNGRooftopDto(rooftop, true)).collect(Collectors.toList());
        return null;
    }

    private RooftopReview createReview(final String content, final int grade, final Member findMember) {
        RooftopReview review = RooftopReview.createReview()
                .content(content)
                .grade(grade)
                .member(findMember)
                .build();

        return review;
    }

    private boolean isDeleteRooftopReviewValidation(final Long memberId, final RooftopReview findRooftopReview) {
        return findRooftopReview.getCreatedBy().equals(String.valueOf(memberId));
    }

    /**
     * 나의 Rooftop 가져오기
     */
    public RooftopPageDto getMyRooftopInfo(Long memberId, Pageable pageable) {
        Page<Rooftop> rooftopPage = rooftopRepository.findByMyRooftopInfo(memberId, pageable);

        return new RooftopPageDto().RooftopSearchPageDto(rooftopPage.getTotalPages(), rooftopPage.getTotalElements(), rooftopPage.getContent(), "G");
    }

    /**
     * 옥상 수정하기
     */
    @Transactional
    public void editRooftopDetail(Long rooftopId, Long memberId, List<MultipartFile> addImages, List<String> deleteFileNames, MultipartFile mainImage,
                                  Integer adultCount, Integer kidCount, Integer petCount, Integer totalCount,
                                  LocalTime startTime, LocalTime endTime, Integer totalPrice) {
        Rooftop rooftop = findByRooftopId(rooftopId);

        if(!rooftop.getCreatedBy().equals(String.valueOf(memberId)))
            throw new NoMatchMemberIdException("권한이 없습니다.");

        saveRooftopImages(addImages, null, mainImage, rooftop);
        RooftopPeopleCount peopleCount = rooftop.getPeopleCount();
        rooftop.updateRooftop(startTime, endTime, peopleCount.updatePeopleCount(adultCount, kidCount, petCount, totalCount), totalPrice);

        if(deleteFileNames != null) {
            rooftopRepository.deleteImagesByFileName(deleteFileNames);
            deleteFileNames.forEach(fileService::deleteFileS3);
        }
    }

    /**
     * 옥상 pay option 수정하기
     */
    @Transactional
    public void editRooftopOption(Long rooftopId, Long memberId, List<String> contents, List<Integer> prices, List<Integer> counts) {
        Rooftop rooftop = findByRooftopId(rooftopId);

        if(!rooftop.getCreatedBy().equals(String.valueOf(memberId)))
            throw new NoMatchMemberIdException("권한이 없습니다.");

        rooftopRepository.deleteRooftopOptions(rooftopId);
        saveRooftopOptions(contents, prices, counts, rooftop);
    }

    private static void isValidMember(Long memberId, Rooftop rooftop) {
        for (RooftopReview review : rooftop.getReviews()) {
            if (review.getMember().getId().equals(memberId)) {
                throw new ExistObjectException("이미 리뷰를 등록한 회원입니다.");
            }
        }
    }
}
