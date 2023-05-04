package rooftopgreenlight.urbanisland.repository.greenbee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import rooftopgreenlight.urbanisland.dto.greenbee.GreenBeeDto;

public interface GreenBeeRepositoryCustom {

    Page<GreenBeeDto> getWaitInfoWithCfImage(PageRequest pageRequest);
}
