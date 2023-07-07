package net.studio1122.boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.enums.UserRole;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String nickname;
    private UserRole role;
    private String email;
    private String profileImage;
    private boolean isDeleted;
    private OffsetDateTime createdDate;
    private OffsetDateTime deletedDate;

    public static UserDTO build(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .isDeleted(user.isDeleted())
                .createdDate(user.getCreatedDate())
                .deletedDate(user.getDeletedDate())
                .build();
    }

}
