package rekrutacja.zadanie.rekrutacja.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import rekrutacja.zadanie.rekrutacja.model.entity.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private double calculations;

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .type(user.getType())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .calculations(calculate(user))
                .build();
    }

    private static double calculate(User user) {
        int followers = user.getFollowers();
        int publicRepos = user.getPublicRepos();

        if (followers == 0) {
            return 0;
        }

        return (6.0 / followers) * (2 + publicRepos);
    }

}
