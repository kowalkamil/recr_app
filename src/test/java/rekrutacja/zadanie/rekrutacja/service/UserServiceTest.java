package rekrutacja.zadanie.rekrutacja.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import rekrutacja.zadanie.rekrutacja.exceptions.ApiClientException;
import rekrutacja.zadanie.rekrutacja.exceptions.ApiServerException;
import rekrutacja.zadanie.rekrutacja.exceptions.InternalServerErrorException;
import rekrutacja.zadanie.rekrutacja.exceptions.UserNotFoundException;
import rekrutacja.zadanie.rekrutacja.model.dto.UserDto;
import rekrutacja.zadanie.rekrutacja.model.entity.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private UserService userService;

    @Value("${github.api.url}")
    private String GITHUB_API_URL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserInGithub_SuccessWithMockUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setLogin("mockuser");
        mockUser.setName("name");
        mockUser.setType("User");
        mockUser.setAvatarUrl("avatarUrl");
        mockUser.setFollowers(100);
        mockUser.setPublicRepos(50);

        when(restTemplate.getForObject(Mockito.anyString(), eq(User.class))).thenReturn(mockUser);

        UserDto userDto = userService.findUserInGithub("mockuser");

        assertNotNull(userDto);
        assertEquals("mockuser", userDto.getLogin());
        assertEquals("name", userDto.getName());
        assertEquals("User", userDto.getType());
        assertEquals("avatarUrl", userDto.getAvatarUrl());
        assertTrue(userDto.getCalculations() > 0);
    }

    @Test
    void testFindUserInGithub_UserNotFound() {
        when(restTemplate.getForObject(Mockito.anyString(), eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.findUserInGithub("unknownuser"));

        assertEquals("No user found in Github for login : unknownuser", exception.getMessage());
    }

    @Test
    void testFindUserInGithub_ClientError() {
        when(restTemplate.getForObject(Mockito.anyString(), eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(ApiClientException.class, () -> userService.findUserInGithub("mockuser"));

        assertTrue(exception.getMessage().contains("Client Error"));
    }

    @Test
    void testFindUserInGithub_ServerError() {
        when(restTemplate.getForObject(GITHUB_API_URL + "mockuser", User.class))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(ApiServerException.class, () -> userService.findUserInGithub("mockuser"));

        assertTrue(exception.getMessage().contains("Server Error"));
    }

    @Test
    void testFindUserInGithub_InternalError() {
        when(restTemplate.getForObject(GITHUB_API_URL + "mockuser", User.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(InternalServerErrorException.class, () -> userService.findUserInGithub("mockuser"));

        assertTrue(exception.getMessage().contains("Internal Server Error"));
    }
}