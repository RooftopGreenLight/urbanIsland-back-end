package rooftopgreenlight.urbanisland.domain.rooftop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundRooftopException;
import rooftopgreenlight.urbanisland.domain.greenbee.entity.GreenBee;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.Rooftop;
import rooftopgreenlight.urbanisland.domain.rooftop.entity.RooftopGreeningApply;
import rooftopgreenlight.urbanisland.domain.rooftop.repository.RooftopGreeningApplyRepository;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.GreeningApplyDto;
import rooftopgreenlight.urbanisland.domain.rooftop.service.dto.GreeningApplyPageDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RooftopGreeningApplyService {
    private final RooftopGreeningApplyRepository greeningApplyRepository;

    /**
     * 그린비 -> 옥상 신청 시 신청 정보 저장하기
     */
    @Transactional
    public void saveApply(Rooftop rooftop, GreenBee greenBee, Long memberId) {
        RooftopGreeningApply greeningApply = RooftopGreeningApply.createApply()
                .applyTime(LocalDateTime.now())
                .greenBeeMemberId(memberId)
                .build();
        greeningApply.changeProgress(Progress.GREENBEE_COMPLETED);
        greeningApply.changeRooftop(rooftop);
        greeningApply.changeGreenBee(greenBee);
        greeningApplyRepository.save(greeningApply);
    }

    /**
     * 옥상지기 -> 옥상에 따른 그린비 신청 정보 가져오기
     */
    public GreeningApplyPageDto getGreenBeeWaitingList(int page, Long rooftopId) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC,"applyTime"));
        Page<RooftopGreeningApply> byGreenBeeCompleted = greeningApplyRepository.getGreenBeeWaitingList(rooftopId, pageRequest);
        return GreeningApplyPageDto.of(byGreenBeeCompleted.getTotalPages(),
                byGreenBeeCompleted.getTotalElements(), byGreenBeeCompleted.getContent());
    }

    /**
     * 그린비가 신청한 옥상 정보 가져오기
     * 녹화 중인 옥상 (ACCEPTED)
     * 신청 완료, 신청 거절 (SELECTED)
     * 녹화 확정 옥상 (COMPLETED)
     */
    public List<GreeningApplyDto> getRooftopOfGreenBee(Long memberId, String type) {
        List<RooftopGreeningApply> greeningRooftops =
                type.equals("ACCEPTED") ? greeningApplyRepository.getGreeningRooftopOfGreenBee(memberId, Progress.GREENING_ACCEPTED)
                        : type.equals("SELECTED") ?
                        greeningApplyRepository.getSelectedRooftopOfGreenBee(memberId, List.of(Progress.GREENBEE_COMPLETED, Progress.GREENING_REJECTED))
                        : greeningApplyRepository.getGreeningRooftopOfGreenBee(memberId, Progress.ADMIN_COMPLETED);

        return greeningRooftops.stream().map(greeningRooftop -> GreeningApplyDto.of(greeningRooftop, type)).collect(Collectors.toList());
    }

    /**
     * 옥상지기가 거절한 그린비의 신청 정보 리스트 가져오기
     */
    public List<RooftopGreeningApply> getNotSelectedApply(Long rooftopId) {
        return greeningApplyRepository.getRooftopApply(rooftopId);
    }

    /**
     * 그린비 id와 옥상 id로 녹화 신청 정보 가져오기
     */
    public RooftopGreeningApply getRooftopApplyByGreenBeeId(Long rooftopId, Long memberId) {
        return greeningApplyRepository.getRooftopApplyByGreenBeeId(rooftopId, memberId)
                .orElseThrow(() -> {
                    throw new NotFoundRooftopException("옥상을 찾을 수 없습니다.");
                });
    }

    /**
     * 옥상 녹화 신청 정보 삭제하기
     */
    public void deleteGreeningApplies(Long rooftopId) {
        greeningApplyRepository.deleteGreeningApplies(rooftopId);
    }

    /**
     * 이미 신청 기록이 존재하는지 체크
     */
    public boolean isExistRooftopApplyByGreenBeeId(Long rooftopId, Long memberId) {
       return greeningApplyRepository.isExistGreeningApplyByGreenBeeId(rooftopId, memberId);
    }
}
