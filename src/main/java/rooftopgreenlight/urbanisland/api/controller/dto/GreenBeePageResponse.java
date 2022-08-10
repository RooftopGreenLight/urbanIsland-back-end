package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class GreenBeePageResponse {

    private int totalPages;
    private long totalElements;
    private List<GreenBeeInfoResponse> greenBeeInfoResponses;

    private GreenBeePageResponse(int totalPages, long totalElements, List<GreenBeeInfoResponse> greenBeeInfoResponses) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.greenBeeInfoResponses = greenBeeInfoResponses;
    }

    public static GreenBeePageResponse of(int totalPages, long totalElements, List<GreenBeeInfoResponse> greenBeeInfoResponses) {
        return new GreenBeePageResponse(totalPages, totalElements, greenBeeInfoResponses);
    }
}
