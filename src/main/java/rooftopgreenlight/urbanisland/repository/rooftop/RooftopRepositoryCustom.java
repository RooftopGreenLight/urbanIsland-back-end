package rooftopgreenlight.urbanisland.repository.rooftop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import rooftopgreenlight.urbanisland.domain.rooftop.Rooftop;
import rooftopgreenlight.urbanisland.dto.rooftop.RooftopSearchCond;

public interface RooftopRepositoryCustom {

    Page<Rooftop> searchRooftopByCond(Pageable pageable, RooftopSearchCond searchCond);
    Page<Rooftop> searchNGRooftopByCond(Pageable pageable, RooftopSearchCond searchCond);
}
