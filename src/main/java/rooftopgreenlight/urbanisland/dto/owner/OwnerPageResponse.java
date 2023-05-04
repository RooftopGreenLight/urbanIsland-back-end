package rooftopgreenlight.urbanisland.dto.owner;

import lombok.Data;

import java.util.List;

@Data
public class OwnerPageResponse {

    private int totalPages;
    private long totalElements;
    private List<OwnerInfoResponse> ownerInfoResponses;

    private OwnerPageResponse(int totalPages, long totalElements, List<OwnerInfoResponse> ownerInfoResponses) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.ownerInfoResponses = ownerInfoResponses;
    }

    public static OwnerPageResponse of(int totalPages, long totalElements, List<OwnerInfoResponse> ownerInfoResponses) {
        return new OwnerPageResponse(totalPages, totalElements, ownerInfoResponses);
    }
}
