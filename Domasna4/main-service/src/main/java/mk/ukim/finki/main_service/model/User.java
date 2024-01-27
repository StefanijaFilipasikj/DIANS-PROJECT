package mk.ukim.finki.main_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.main_service.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Data
@Entity
@NoArgsConstructor
@Table(name = "App_Users")
public class User implements UserDetails {
    @Id
    private String username;
    private String password;
    private String name;
    private String surname;
    private String photoUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired =  true;
    private boolean isEnabled = true;

    public User(String username, String password, String name, String surname, String photoUrl, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
