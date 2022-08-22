package rooftopgreenlight.urbanisland.domain.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.common.exception.ExistObjectException;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundOwnerException;
import rooftopgreenlight.urbanisland.domain.file.entity.OwnerImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.member.entity.Authority;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.owner.entity.Owner;
import rooftopgreenlight.urbanisland.domain.owner.entity.QOwner;
import rooftopgreenlight.urbanisland.domain.owner.repository.OwnerRepository;
import rooftopgreenlight.urbanisland.domain.owner.service.dto.OwnerDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {

    private final FileService fileService;
    private final MemberService memberService;
    private final OwnerRepository ownerRepository;

    public void saveOwner(Long memberId, MultipartFile file) {
        isOwnerJoinValid(memberId);

        Member member = memberService.findById(memberId);

        OwnerImage ownerImage = (file == null) ? null : (OwnerImage) fileService
                .createImage(file, ImageType.CONFIRMATION, ImageName.Owner);
        Owner owner = createOwner(ownerImage);

        owner.changeMember(member);
        owner.changeProgress(Progress.ADMIN_WAIT);

        ownerRepository.save(owner);
    }

    /**
     * 승인 대기 중인 옥상지기 정보 가져오기
     */
    public Page<OwnerDto> getWaitOwnerWaits(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "createdDate"));

        return ownerRepository.getWaitInfoWithCfImage(pageRequest);
    }

    /**
     * 승인 대기 중인 옥상지기 승인
     */
    @Transactional
    public void acceptGreenBee(long memberId) {
        Owner owner = ownerRepository.findByMemberIdWithMember(memberId).orElseThrow(() -> {
            throw new NotFoundOwnerException("옥상지기 신청 정보를 찾을 수 없습니다.");
        });

        Member member = owner.getMember();
        if (member.getAuthority() == Authority.ROLE_GREENBEE) {
            member.changeAuthority(Authority.ROLE_ALL);
        } else {
            member.changeAuthority(Authority.ROLE_ROOFTOPOWNER);
        }

        owner.changeProgress(Progress.ADMIN_COMPLETED);
    }

    /**
     * 승인 대기 중인 옥상지기 거절
     */
    @Transactional
    public void rejectGreenBee(long memberId) {
        Owner owner = ownerRepository.findByMemberIdWithImage(memberId).orElseThrow(() -> {
            throw new NotFoundOwnerException("옥상지기 신청 정보를 찾을 수 없습니다.");
        });

        fileService.deleteFileS3(owner.getOwnerImage().getStoreFilename());

        ownerRepository.delete(owner);
    }

    private Owner createOwner(OwnerImage ownerImage) {
        return Owner.createOwner()
                .ownerImage(ownerImage)
                .build();
    }

    private void isOwnerJoinValid(Long memberId) {
        if (ownerRepository.exists(QOwner.owner.member.id.eq(memberId))) {
            throw new ExistObjectException("옥상지기 등록을 할 수 없는 상태입니다.");
        }
    }
}
