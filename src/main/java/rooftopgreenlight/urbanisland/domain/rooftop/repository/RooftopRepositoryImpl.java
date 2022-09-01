package rooftopgreenlight.urbanisland.domain.rooftop.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopType;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.RooftopSearchCond;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;

import static rooftopgreenlight.urbanisland.domain.rooftop.entity.QRooftop.rooftop;
import static rooftopgreenlight.urbanisland.domain.rooftop.entity.QRooftopDetail.rooftopDetail;

public class RooftopRepositoryImpl implements RooftopRepositoryCustom {

    private final JPAQueryFactory query;

    public RooftopRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    /**
     * GREEN + ADMIN_COMPLETED
     */
    @Override
    public Page<Rooftop> searchRooftopByCond(Pageable pageable, RooftopSearchCond searchCond) {
        System.out.println(searchCond.getMaxPrice());
        List<Rooftop> content = query
                .selectFrom(rooftop)
                .distinct()
                .leftJoin(rooftop.rooftopDetails, rooftopDetail)
                .where(timeCond(searchCond.getStartTime(), searchCond.getEndTime()),
                        peopleCountCond(searchCond.getAdultCount(), searchCond.getKidCount(), searchCond.getPetCount()),
                        addressCond(searchCond.getCity(), searchCond.getDistrict()),
                        priceCond(searchCond.getMaxPrice(), searchCond.getMinPrice()),
                        contentNumCond(searchCond.getContentNum()),
                        widthCond(searchCond.getMaxWidth(), searchCond.getMinWidth()),
                        rooftop.rooftopProgress.eq(Progress.ADMIN_COMPLETED)
                )
                .orderBy(sortCond(searchCond.getCond()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(rooftop.count())
                .from(rooftop)
                .distinct()
                .leftJoin(rooftop.rooftopDetails, rooftopDetail)
                .where(timeCond(searchCond.getStartTime(), searchCond.getEndTime()),
                        peopleCountCond(searchCond.getAdultCount(), searchCond.getKidCount(), searchCond.getPetCount()),
                        addressCond(searchCond.getCity(), searchCond.getDistrict()),
                        priceCond(searchCond.getMaxPrice(), searchCond.getMinPrice()),
                        contentNumCond(searchCond.getContentNum()),
                        widthCond(searchCond.getMaxWidth(), searchCond.getMinWidth()),
                        rooftop.rooftopType.eq(RooftopType.GREEN),
                        rooftop.rooftopProgress.eq(Progress.ADMIN_COMPLETED)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * NG + GREENBEE_WAIT
     */
    @Override
    public Page<Rooftop> searchNGRooftopByCond(Pageable pageable, RooftopSearchCond searchCond) {
        List<Rooftop> content = query
                .selectFrom(rooftop)
                .distinct()
                .leftJoin(rooftop.rooftopDetails, rooftopDetail)
                .where(
                        addressCond(searchCond.getCity(), searchCond.getDistrict()),
                        contentNumCond(searchCond.getContentNum()),
                        widthCond(searchCond.getMaxWidth(), searchCond.getMinWidth()),
                        widthPriceCond(searchCond.getMaxWidthPrice(), searchCond.getMinWidthPrice()),
                        deadLineTypeCond(searchCond.getDeadLineType()),
                        rooftop.rooftopType.eq(RooftopType.NOT_GREEN),
                        rooftop.rooftopProgress.eq(Progress.GREENBEE_WAIT)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(rooftop.count())
                .from(rooftop)
                .distinct()
                .leftJoin(rooftop.rooftopDetails, rooftopDetail)
                .where(
                        addressCond(searchCond.getCity(), searchCond.getDistrict()),
                        contentNumCond(searchCond.getContentNum()),
                        widthCond(searchCond.getMaxWidth(), searchCond.getMinWidth()),
                        widthPriceCond(searchCond.getMaxWidthPrice(), searchCond.getMinWidthPrice()),
                        deadLineTypeCond(searchCond.getDeadLineType()),
                        rooftop.rooftopType.eq(RooftopType.NOT_GREEN),
                        rooftop.rooftopProgress.eq(Progress.GREENBEE_WAIT)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public BooleanExpression timeCond(String startTime, String endTime) {
        if (!StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
            return null;
        }

        return rooftop.startTime.loe(LocalTime.parse(startTime)).and(rooftop.endTime.goe(LocalTime.parse(endTime)));
    }

    public BooleanExpression peopleCountCond(Integer adultCount, Integer kidCount, Integer petCount) {
        if (adultCount == null && kidCount == null && petCount == null) {
            return null;
        }

        BooleanExpression booleanExpression = null;
        if (adultCount != null) {
            booleanExpression.and(rooftop.peopleCount.adultCount.goe(adultCount));
        }

        if (kidCount != null) {
            booleanExpression.and(rooftop.peopleCount.kidCount.goe(kidCount));
        }

        if (petCount != null) {
            booleanExpression.and(rooftop.peopleCount.petCount.goe(petCount));
        }

        return booleanExpression;
    }

    private BooleanExpression addressCond(String city, String district) {
        if (!StringUtils.hasText(city) || !StringUtils.hasText(district)) {
            return null;
        }

        return rooftop.address.city.eq(city).and(rooftop.address.district.eq(district));
    }

    private BooleanExpression priceCond(Integer maxPrice, Integer minPrice) {
        if (maxPrice == null && minPrice == null) {
            return null;
        } else if (maxPrice == null) {
            return rooftop.totalPrice.goe(minPrice);
        }

        return rooftop.totalPrice.between(minPrice, maxPrice);
    }

    private BooleanExpression contentNumCond(List<Integer> contentNum) {
        if (contentNum == null) {
            return null;
        }

        return rooftopDetail.contentNum.in(contentNum);
    }

    private BooleanExpression widthCond(Double maxWidth, Double minWidth) {
        if (maxWidth == null && minWidth == null) {
            return null;
        } else if (maxWidth == null) {
            return rooftop.width.goe(minWidth);
        }

        return rooftop.width.between(minWidth, maxWidth);
    }

    private BooleanExpression widthPriceCond(Integer maxWidthPrice, Integer minWidthPrice) {
        if(maxWidthPrice == null && minWidthPrice == null) {
            return null;
        } else if (maxWidthPrice == null) {
            return rooftop.widthPrice.goe(minWidthPrice);
        }
        return rooftop.widthPrice.between(minWidthPrice, maxWidthPrice);
    }

    private BooleanExpression deadLineTypeCond(Integer deadLineType) {
        if(deadLineType == null) {
            return null;
        }
        return rooftop.deadLineType.loe(deadLineType);
    }

    private OrderSpecifier<?> sortCond(Integer cond) {
        if (cond == null) return rooftop.id.desc();

        switch (cond) {
            case 1:
                return rooftop.grade.desc();
            case 2:
                return rooftop.totalPrice.asc();
            case 3:
                return rooftop.totalPrice.desc();
        }

        return rooftop.id.desc();
    }
}
