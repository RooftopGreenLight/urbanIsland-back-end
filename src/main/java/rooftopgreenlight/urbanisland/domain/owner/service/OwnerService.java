package rooftopgreenlight.urbanisland.domain.owner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rooftopgreenlight.urbanisland.domain.common.constant.Progress;
import rooftopgreenlight.urbanisland.domain.file.entity.OwnerImage;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageName;
import rooftopgreenlight.urbanisland.domain.file.entity.constant.ImageType;
import rooftopgreenlight.urbanisland.domain.file.service.FileService;
import rooftopgreenlight.urbanisland.domain.greenbee.service.dto.GreenBeeDto;
import rooftopgreenlight.urbanisland.domain.member.entity.Member;
import rooftopgreenlight.urbanisland.domain.member.service.MemberService;
import rooftopgreenlight.urbanisland.domain.owner.entity.Owner;
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
        Member member = memberService.findById(memberId);

        Owner owner = createOwner((OwnerImage) fileService
                .createImage(file, ImageType.CONFIRMATION, ImageName.Owner));

        owner.changeMember(member);
        owner.changeProgress(Progress.WAIT);

        ownerRepository.save(owner);
    }

    /**
     * 승인 대기 중인 옥상지기 정보 가져오기
     */
    public Page<OwnerDto> getWaitOwnerWaits(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "createdDate"));

        return ownerRepository.getWaitInfoWithCfImage(pageRequest);
    }

    private Owner createOwner(OwnerImage ownerImage) {
        return Owner.createOwner()
                .ownerImage(ownerImage)
                .build();
    }
}