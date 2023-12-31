package net.studio1122.boilerplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.studio1122.boilerplate.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty @Size(min = 4)
    @Pattern(regexp = "[a-zA-Z\\d_]+", message = "Username Rule : only alphabet + number + _")
    private String username;

    @Column(unique = true, nullable = false)
    @NotEmpty
    private String nickname;

    @Column(nullable = false)
    @NotEmpty
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @NotEmpty @Pattern(regexp = ".+@.+", message = "Invalid email address")
    private String email;

    private String profileImage;

    @Column(nullable = false)
    private boolean isLocked;
    @Column(nullable = false)
    private boolean isBanned;
    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private OffsetDateTime createdDate;
    private OffsetDateTime bannedDate;
    private OffsetDateTime deletedDate;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return (!isLocked && !isBanned && !isDeleted);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return (!isLocked && !isBanned && !isDeleted);
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> posts = new ArrayList<>();

    public void addPost(Board post) {
        this.posts.add(post);

        if (post.getUser() != this) {
            post.setUser(this);
        }
    }
}
