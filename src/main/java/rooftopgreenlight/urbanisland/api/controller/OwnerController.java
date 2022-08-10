package rooftopgreenlight.urbanisland.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.api.common.annotation.PK;
import rooftopgreenlight.urbanisland.api.controller.dto.APIResponse;
import rooftopgreenlight.urbanisland.domain.owner.service.OwnerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "옥상지기 등록하기",
            notes = "요청 데이터(form-data) - key : confirmationFile(Multipart)")
    public APIResponse createOwner(@PK Long memberId,
                                   @RequestParam(name = "confirmationFile", required = false) MultipartFile file){

        ownerService.saveOwner(memberId, file);

        return APIResponse.createEmpty();
    }
}
