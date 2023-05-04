package rooftopgreenlight.urbanisland.repository.owner;

import static rooftopgreenlight.urbanisland.domain.owner.QOwner.*;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import rooftopgreenlight.urbanisland.domain.model.Progress;
import rooftopgreenlight.urbanisland.dto.owner.OwnerDto;
import rooftopgreenlight.urbanisland.dto.owner.QOwnerDto;

import javax.persistence.EntityManager;
import java.util.List;

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
                        owner.progress.eq(Progress.ADMIN_WAIT)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(owner.count())
                .from(owner)
                .where(
                        owner.progress.eq(Progress.ADMIN_WAIT)
                );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

}
