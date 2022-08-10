package rooftopgreenlight.urbanisland.domain.owner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import rooftopgreenlight.urbanisland.domain.owner.service.dto.OwnerDto;

public interface OwnerRepositoryCustom {

    Page<OwnerDto> getWaitInfoWithCfImage(PageRequest pageRequest);
}
