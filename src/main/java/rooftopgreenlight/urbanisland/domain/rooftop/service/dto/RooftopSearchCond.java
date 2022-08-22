package rooftopgreenlight.urbanisland.domain.rooftop.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class RooftopSearchCond {

    // 이용 가능 시간
    private String startTime;
    private String endTime;

    // 인원
    private Integer adultCount;
    private Integer kidCount;
    private Integer petCount;

    // 지역
    private String city;
    private String district;

    // 가격
    private Integer maxPrice;
    private Integer minPrice;

    // 편의 요소
    private List<Integer> contentNum;

    // 넓이
    private Double maxWidth;
    private Double minWidth;

    // 단가
    private Integer minWidthPrice;
    private Integer maxWidthPrice;

    // 시공기한
    private Integer deadLineType;

    // 조건
    private Integer cond;

}
