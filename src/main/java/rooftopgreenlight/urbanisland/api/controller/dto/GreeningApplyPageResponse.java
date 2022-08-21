package rooftopgreenlight.urbanisland.api.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.GreeningApplyPageDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GreeningApplyPageResponse {
    private int totalPages;
    private long totalElements;
    private List<GreeningApplyResponse> applyDtos;

    public static GreeningApplyPageResponse of(GreeningApplyPageDto applyPageDto) {
        return new GreeningApplyPageResponse(
                applyPageDto.getTotalPages(),
                applyPageDto.getTotalElements(),
                applyPageDto.getApplyDtos().stream().map(GreeningApplyResponse::of).collect(Collectors.toList()));
    }

}
