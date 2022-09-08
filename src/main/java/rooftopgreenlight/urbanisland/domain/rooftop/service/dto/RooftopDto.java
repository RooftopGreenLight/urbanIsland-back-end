package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.common.Address;
import rooftopgreenlight.urbanisland.domain.file.entity.RooftopImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.reservation.entity.Reservation;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopDetail;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopDetailType;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopPeopleCount;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RooftopDto {
    private Long id;
    private Long ownerId;

    private Integer totalPrice;
    private Integer widthPrice;
    private Integer requiredTermType;

    private Integer adultCount;
    private Integer kidCount;
    private Integer petCount;
    private Integer totalCount;

    private LocalTime startTime;
    private LocalTime endTime;

    private Double width;
    private String grade;
    private String city;
    private String district;
    private String detail;
    private String phoneNumber;
    private String explainContent;
    private String refundContent;
    private String roleContent;
    private String ownerContent;

    private String progress;
    private LocalDateTime rooftopDate;

    private List<Integer> detailNums;
    private List<RooftopImageDto> rooftopImages;
    private RooftopImageDto structureImage;
    private RooftopImageDto mainImage;
    private List<RooftopReviewDto> rooftopReviews;
    private List<RooftopOptionDto> rooftopOptions;

    private List<ReservationDto> reservations;

    protected RooftopDto(Long id, String city, String district, String detail, String progress, LocalDateTime rooftopDate) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.progress = progress;
        this.rooftopDate = rooftopDate;
    }

    protected RooftopDto(Long id, String phoneNumber, String ownerContent, RooftopImageDto structureImage) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.ownerContent = ownerContent;
        this.structureImage = structureImage;
    }

    protected RooftopDto(Long id, String city, String district, String detail,
                         String grade, Integer totalPrice, RooftopImage mainImage) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.grade = grade;
        this.totalPrice = totalPrice;
        this.mainImage = RooftopImageDto.of(mainImage);
    }

    protected RooftopDto(Long id, int widthPrice, Double width, String city, String district, String detail) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    protected RooftopDto(Long id, int widthPrice, int requiredTermType, Double width, String city, String district,
                         String detail, String phoneNumber, String ownerContent, List<Integer> requiredItemNums) {
        this.id = id;
        this.widthPrice = widthPrice;
        this.requiredTermType = requiredTermType;
        this.width = width;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.phoneNumber = phoneNumber;
        this.ownerContent = ownerContent;
        this.detailNums = requiredItemNums;
    }

    protected RooftopDto(Long id, String city, String district, String detail,
                         Double width, Integer widthPrice, RooftopImage mainImage) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.width = width;
        this.widthPrice = widthPrice;
        this.mainImage = RooftopImageDto.of(mainImage);
    }

    protected RooftopDto(Long id, Integer totalPrice, String city, String district, String detail, String explainContent, String roleContent,
                         String refundContent, String grade, Double width, RooftopImageDto mainImage, List<RooftopImageDto> rooftopImages, RooftopImageDto structureImage,
                         Integer adultCount, Integer kidCount, Integer petCount, Integer totalCount, List<Integer> detailNums,
                         LocalTime startTime, LocalTime endTime, List<RooftopReviewDto> reviews, Long ownerId, List<RooftopOptionDto> options,
                         List<Reservation> reservations) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.explainContent = explainContent;
        this.roleContent = roleContent;
        this.refundContent = refundContent;
        this.grade = grade;
        this.width = width;
        this.mainImage= mainImage;
        this.rooftopImages = rooftopImages;
        this.structureImage = structureImage;
        this.adultCount = adultCount;
        this.kidCount = kidCount;
        this.petCount = petCount;
        this.totalCount = totalCount;
        this.detailNums = detailNums;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rooftopReviews = reviews;
        this.ownerId = ownerId;
        this.rooftopOptions = options;
        this.reservations = reservations.size() > 0
                ? reservations.stream().map(ReservationDto::from).collect(Collectors.toList())
                : null;
    }

    protected RooftopDto(String city, String district, String detail) {
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    public static RooftopDto getRooftopAddress(String city, String district, String detail) {
        return new RooftopDto(city, district, detail);
    }

    public static RooftopDto getRooftopStatusDto(Long id, String city, String district, String detail, String progress, LocalDateTime rooftopDate) {
        return new RooftopDto(id, city, district, detail, progress, rooftopDate);
    }

    public static RooftopDto getAdminNGRooftopDto(Long id, String phoneNumber, String ownerContent, List<RooftopImage> rooftopImages) {
        return new RooftopDto(
                id,
                phoneNumber,
                ownerContent,
                RooftopImageDto.of(
                rooftopImages
                    .stream()
                    .filter(rooftopImage -> rooftopImage.getRooftopImageType() == ImageType.STRUCTURE)
                    .findFirst()
                    .orElse(null)
                )
        );
    }

    public RooftopDto RooftopSearchResultDto(Long id, String city, String district, String detail,
                                             String grade, Integer totalPrice, RooftopImage mainImage) {
        return new RooftopDto(id, city, district, detail, grade, totalPrice, mainImage);
    }

    public RooftopDto NGRooftopSearchResultDto(Long id, String city, String district, String detail,
                                               Double width, Integer widthPrice, RooftopImage mainImage) {
        return new RooftopDto(id, city, district, detail, width, widthPrice, mainImage);
    }

    public static RooftopDto getRooftopDto(Rooftop rooftop) {
        Map<ImageType, List<RooftopImageDto>> listMap = getRooftopImageByType(rooftop);

        Address address = rooftop.getAddress();
        RooftopPeopleCount peopleCount = rooftop.getPeopleCount();

        List<Integer> detailNums = null;
        if(rooftop.getRooftopDetails() != null) {
            detailNums = rooftop.getRooftopDetails().stream().map(RooftopDetail::getContentNum).collect(Collectors.toList());
        }

        List<RooftopOptionDto> options = null;
        if(rooftop.getRooftopOptions() != null) {
            options = rooftop.getRooftopOptions().stream().map(option ->
                            RooftopOptionDto.of(option.getId(), option.getContent(), option.getPrice(), option.getCount()))
                    .collect(Collectors.toList());
        }

        List<RooftopReviewDto> reviews = new ArrayList<>();
        if(rooftop.getReviews() != null) {
             reviews = rooftop.getReviews().stream().map(review ->
                 RooftopReviewDto.of(review.getGrade(), review.getContent(), review.getCreatedDate(), getNewNickname(review.getMember().getNickname()))
            ).collect(Collectors.toList());
        }

        return new RooftopDto(rooftop.getId(), rooftop.getTotalPrice(), address.getCity(), address.getDistrict(),
                address.getDetail(), rooftop.getExplainContent(), rooftop.getRoleContent(), rooftop.getRefundContent(),
                rooftop.getGrade(), rooftop.getWidth(), listMap.get(ImageType.MAIN) == null ? null : listMap.get(ImageType.MAIN).get(0),
                listMap.get(ImageType.NORMAL), listMap.get(ImageType.STRUCTURE) == null ? null : listMap.get(ImageType.STRUCTURE).get(0),
                peopleCount.getAdultCount(), peopleCount.getKidCount(), peopleCount.getPetCount(), peopleCount.getTotalCount(),
                detailNums, rooftop.getStartTime(), rooftop.getEndTime(), reviews, rooftop.getMember().getId(), options,
                rooftop.getReservations());
    }

    private static String getNewNickname(String nickname) {
        return nickname.charAt(0) + "*".repeat(Math.max(0, nickname.length() - 2)) +
                nickname.charAt(nickname.length() - 1);
    }

    public static RooftopDto getNGRooftopDto(Rooftop rooftop, boolean isOne) {
        RooftopDto rooftopDto = isOne ? createDetailNGRooftopDto(rooftop) : createListInfoNGRooftopDto(rooftop);
        Map<ImageType, List<RooftopImageDto>> listMap = getRooftopImageByType(rooftop);

        if(listMap.get(ImageType.NORMAL) != null) {
            rooftopDto.setRooftopImages(listMap.get(ImageType.NORMAL));
        }
        if (listMap.get(ImageType.STRUCTURE) != null) {
            rooftopDto.setStructureImage(listMap.get(ImageType.STRUCTURE).get(0));
        }

        return rooftopDto;
    }

    private static Map<ImageType, List<RooftopImageDto>> getRooftopImageByType(Rooftop rooftop) {
        Map<ImageType, List<RooftopImageDto>> listMap = rooftop.getRooftopImages().stream().map(RooftopImageDto::of)
                .collect(Collectors.groupingBy(RooftopImageDto::getRooftopImageType));
        return listMap;
    }

    private static RooftopDto createListInfoNGRooftopDto(Rooftop rooftop) {
        return new RooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail());
    }

    private static RooftopDto createDetailNGRooftopDto(Rooftop rooftop) {
        List<Integer> requiredItemNums = null;
        if(rooftop.getRooftopDetails() != null) {
            requiredItemNums = rooftop.getRooftopDetails().stream().filter(rooftopDetail ->
                            rooftopDetail.getRooftopDetailType().equals(RooftopDetailType.REQUIRED_ITEM))
                    .map(RooftopDetail::getContentNum)
                    .collect(Collectors.toList());
        }

        return new RooftopDto(rooftop.getId(), rooftop.getWidthPrice(), rooftop.getDeadLineType(), rooftop.getWidth(),
                rooftop.getAddress().getCity(), rooftop.getAddress().getDistrict(), rooftop.getAddress().getDetail(),
                rooftop.getPhoneNumber(), rooftop.getOwnerContent(), requiredItemNums);
    }

}
