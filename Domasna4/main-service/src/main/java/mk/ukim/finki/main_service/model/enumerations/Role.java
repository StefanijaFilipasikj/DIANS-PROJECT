package mk.ukim.finki.main_service.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER;
    @Override
    public String getAuthority() {
        return name();
    }
}
