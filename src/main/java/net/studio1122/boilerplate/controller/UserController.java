package net.studio1122.boilerplate.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.dto.LoginDTO;
import net.studio1122.boilerplate.dto.UserDTO;
import net.studio1122.boilerplate.service.UserService;
import net.studio1122.boilerplate.utility.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth")
    @ResponseStatus(code = HttpStatus.OK)
    public void login(final HttpServletRequest request, final HttpServletResponse response,
                      @RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.tryLogin(loginDTO);
            Cookie tokenCookie = createTokenCookie(token, 168 * 60 * 60);
            response.addCookie(tokenCookie);

        } catch (Exception e) {
            Cookie emptyCookie = createTokenCookie(null, 0);
            response.addCookie(emptyCookie);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/api/auth")
    @ResponseStatus(code = HttpStatus.OK)
    public void logout(final HttpServletRequest request, final HttpServletResponse response) {
        Cookie tokenCookie = createTokenCookie(null, 0);
        response.addCookie(tokenCookie);
    }

    @GetMapping("/api/auth")
    @ResponseBody
    public UserDTO getCurrent() {
        try {
            User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            return UserDTO.build(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/api/user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@RequestBody User user) {
        try {
            userService.create(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/api/user/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            User currentUser = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            if (!Objects.deepEquals(currentUser.getId(), id)) {
                throw new Exception("User not matched");
            }

            userService.update(id, user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/api/user/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {
        try {
            User currentUser = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            if (!Objects.deepEquals(currentUser.getId(), id)) {
                throw new Exception("User not matched");
            }

            userService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/user/{id}")
    @ResponseBody
    public UserDTO read(@PathVariable("id") Long id) {
        try {
            return UserDTO.build(userService.read(id).orElseThrow());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @GetMapping("/api/user/check/username")
    @ResponseBody
    public Boolean canUseAsUsername(@RequestParam("username") String username) {
        try {
            return userService.canUseAsUsername(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/user/check/nickname")
    @ResponseBody
    public Boolean canUseAsNickname(@RequestParam("nickname") String nickname) {
        try {
            return userService.canUseAsNickname(nickname);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Cookie createTokenCookie(String token, int age) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }
}
