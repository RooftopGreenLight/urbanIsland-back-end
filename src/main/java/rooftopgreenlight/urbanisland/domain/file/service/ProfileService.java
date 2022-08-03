package rooftopgreenlight.urbanisland.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rooftopgreenlight.urbanisland.domain.common.exception.NotFoundProfileException;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;
import rooftopgreenlight.urbanisland.domain.file.entity.QProfile;
import rooftopgreenlight.urbanisland.domain.file.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile getProfileByMemberId(Long memberId) {
        return profileRepository.findOne(QProfile.profile.member.id.eq(memberId)).orElseThrow(() -> {
            throw new NotFoundProfileException("프로필을 찾을 수 없습니다.");
        });
    }
}
