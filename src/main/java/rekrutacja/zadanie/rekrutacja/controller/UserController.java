package rekrutacja.zadanie.rekrutacja.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rekrutacja.zadanie.rekrutacja.model.dto.UserDto;
import rekrutacja.zadanie.rekrutacja.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{login}")
    public ResponseEntity<UserDto> userData(@PathVariable("login") String login) {
        log.info("getUser({})", login);
        return ResponseEntity.ok(userService.findUserInGithub(login));
    }

}
