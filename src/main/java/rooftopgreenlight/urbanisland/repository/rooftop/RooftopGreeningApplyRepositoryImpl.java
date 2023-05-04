package rooftopgreenlight.urbanisland.repository.rooftop;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static rooftopgreenlight.urbanisland.domain.rooftop.QRooftopGreeningApply.rooftopGreeningApply;

public class RooftopGreeningApplyRepositoryImpl implements RooftopGreeningApplyRepositoryCustom {
    private final JPAQueryFactory factory;

    public RooftopGreeningApplyRepositoryImpl(EntityManager em) {
        this.factory = new JPAQueryFactory(em);
    }

    @Override
    public boolean isExistGreeningApplyByGreenBeeId(Long rooftopId, Long memberId) {
        Integer result = factory.selectOne()
                .from(rooftopGreeningApply)
                .where(rooftopGreeningApply.rooftop.id.eq(rooftopId)
                        .and(rooftopGreeningApply.greenBeeMemberId.eq(memberId)))
                .fetchFirst();
        return result != null;
    }

}
