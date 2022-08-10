package rooftopgreenlight.urbanisland.domain.owner.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.owner.service.dto.OwnerDto;
import rooftopgreenlight.urbanisland.domain.owner.service.dto.QOwnerDto;

import javax.persistence.EntityManager;
import java.util.List;

import static rooftopgreenlight.urbanisland.domain.file.entity.QOwnerImage.ownerImage;
import static rooftopgreenlight.urbanisland.domain.owner.entity.QOwner.owner;

public class OwnerRepositoryImpl implements OwnerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OwnerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OwnerDto> getWaitInfoWithCfImage(PageRequest pageRequest) {

        List<OwnerDto> content = queryFactory
                .select(new QOwnerDto(owner.member.id, owner.ownerImage))
                .from(owner)
                .leftJoin(owner.ownerImage)
                .where(
                        owner.progress.eq(Progress.WAIT)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(owner.count())
                .from(owner)
                .where(
                        owner.progress.eq(Progress.WAIT)
                );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

}
