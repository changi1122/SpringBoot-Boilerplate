package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.dto.LoginDTO;
import net.studio1122.boilerplate.enums.UserRole;
import net.studio1122.boilerplate.repository.UserRepository;
import net.studio1122.boilerplate.security.JwtTokenProvider;
import net.studio1122.boilerplate.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String PASSWORD_RULE = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*#?&]{8,}$";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(User user) throws Exception {
        if (!userRepository.existsByUsername(user.getUsername()) && !user.getUsername().equals("anonymousUser")
            && Pattern.matches(PASSWORD_RULE, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(UserRole.USER);
            user.setProfileImage("");
            user.setBanned(false);
            user.setBanned(false);
            user.setDeleted(false);
            user.setCreatedDate(OffsetDateTime.now());
            userRepository.save(user);
        } else {
            throw new Exception("username is not unique / password rule not matched");
        }
    }

    @Transactional
    public void update(Long id, User updated) throws Exception {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (updated.getNickname() != null)
                user.setUsername(updated.getUsername());
            if (updated.getPassword() != null)
                user.setPassword(passwordEncoder.encode(updated.getPassword()));
            if (updated.getEmail() != null)
                user.setEmail(updated.getEmail());
        } else {
            throw new Exception("User not found");
        }
    }

    @Transactional
    public void delete(Long id) throws Exception {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setDeleted(true);
            user.setDeletedDate(OffsetDateTime.now());
        } else {
            throw new Exception("User not found");
        }
    }

    public String tryLogin(LoginDTO loginDTO) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        if (user.isBanned() || user.isLocked() || user.isDeleted())
            throw new UsernameNotFoundException("User not present");

        if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            Authentication authentication = new UserAuthentication(
                    loginDTO.getUsername(), loginDTO.getPassword(), user.getAuthorities()
            );
            return JwtTokenProvider.generateToken(authentication);
        } else {
            throw new IllegalArgumentException("Password not matched");
        }
    }

    public Optional<User> read(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
    }

    public Boolean canUseAsUsername(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean canUseAsNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

}
