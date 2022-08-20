package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopSearchCond;

public interface RooftopRepositoryCustom {

    Page<Rooftop> searchRooftopByCond(Pageable pageable, RooftopSearchCond searchCond);
    Page<Rooftop> searchNGRooftopByCond(Pageable pageable, RooftopSearchCond searchCond);
}
