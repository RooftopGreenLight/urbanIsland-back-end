package rooftopgreenlight.urbanisland.domain.greenbee.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.QGreenBeeDto;

import javax.persistence.EntityManager;
import java.util.List;

import static rooftopgreenlight.urbanisland.domain.file.entity.QGreenBeeImage.greenBeeImage;
import static rooftopgreenlight.urbanisland.domain.greenbee.entity.QGreenBee.greenBee;

public class GreenBeeRepositoryImpl implements GreenBeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GreenBeeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GreenBeeDto> getWaitInfoWithCfImage(PageRequest pageRequest) {


        List<GreenBeeDto> content = queryFactory
                .select(new QGreenBeeDto(greenBee.member.id, greenBee.officeNumber, greenBee.content, greenBeeImage))
                .from(greenBeeImage)
                .leftJoin(greenBeeImage.greenBee, greenBee)
                .where(
                        greenBee.progress.eq(Progress.ADMIN_WAIT)
                                .and(greenBeeImage.greenBeeImageType.eq(ImageType.CONFIRMATION))
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(greenBeeImage.count())
                .from(greenBeeImage)
                .where(
                        greenBee.progress.eq(Progress.ADMIN_WAIT)
                                .and(greenBeeImage.greenBeeImageType.eq(ImageType.CONFIRMATION))
                );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

}
