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

    public GreeningApplyPageDto getGreenBeeWaitingList(int page, Long memberId) {
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC,"applyTime"));
        Page<RooftopGreeningApply> byGreenBeeCompleted = greeningApplyRepository.getGreenBeeWaitingList(memberId, pageRequest);
        return GreeningApplyPageDto.of(byGreenBeeCompleted.getTotalPages(),
                byGreenBeeCompleted.getTotalElements(), byGreenBeeCompleted.getContent());
    }

    public List<RooftopGreeningApply> getNotSelectedApply(Long rooftopId) {
        return greeningApplyRepository.getRooftopApply(rooftopId);
    }

    public List<GreeningApplyDto> getRooftopOfGreenBee(Long memberId, String type) {
        List<RooftopGreeningApply> greeningRooftops =
                type.equals("ACCEPTED") ? greeningApplyRepository.getGreeningRooftopOfGreenBee(memberId, Progress.GREENING_ACCEPTED)
                        : type.equals("SELECTED") ?
                        greeningApplyRepository.getSelectedRooftopOfGreenBee(memberId, List.of(Progress.GREENBEE_COMPLETED, Progress.GREENING_REJECTED))
                        : greeningApplyRepository.getGreeningRooftopOfGreenBee(memberId, Progress.ADMIN_COMPLETED);

        return greeningRooftops.stream().map(greeningRooftop -> GreeningApplyDto.of(greeningRooftop, type)).collect(Collectors.toList());
    }

    public RooftopGreeningApply completeGreeningRooftop(Long rooftopId, Long memberId) {
        return greeningApplyRepository.getRooftopApplyByGreenBeeId(rooftopId, memberId)
                .orElseThrow(NotFoundRooftopException::new);
    }
}
