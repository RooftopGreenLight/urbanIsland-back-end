package rooftopgreenlight.urbanisland.repository.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import rooftopgreenlight.urbanisland.dto.owner.OwnerDto;

public interface OwnerRepositoryCustom {

    Page<OwnerDto> getWaitInfoWithCfImage(PageRequest pageRequest);
}
