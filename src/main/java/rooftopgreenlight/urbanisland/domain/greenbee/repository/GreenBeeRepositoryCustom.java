package rooftopgreenlight.urbanisland.domain.greenbee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;

public interface GreenBeeRepositoryCustom {

    Page<GreenBeeDto> getWaitInfoWithCfImage(PageRequest pageRequest);
}
