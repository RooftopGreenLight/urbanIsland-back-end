package rooftopgreenlight.urbanisland.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.ofNullable("MASTER");

        String name = authentication.getName();
        if(name.equals("anonymousUser")) return Optional.ofNullable("MASTER");
        else {
            return Optional.ofNullable(name);
        }
    }
}