package rooftopgreenlight.urbanisland.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import rooftopgreenlight.urbanisland.domain.file.entity.Profile;

public interface ProfileRepository extends
        JpaRepository<Profile, Long>,
        QuerydslPredicateExecutor<Profile> {
}
