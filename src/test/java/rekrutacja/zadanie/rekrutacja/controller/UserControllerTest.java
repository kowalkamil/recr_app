package rekrutacja.zadanie.rekrutacja.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rekrutacja.zadanie.rekrutacja.exceptions.UserNotFoundException;
import rekrutacja.zadanie.rekrutacja.model.dto.UserDto;
import rekrutacja.zadanie.rekrutacja.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserSuccess() throws Exception {
        String login = "testuser";
        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setName("Test User");

        Mockito.when(userService.findUserInGithub(login)).thenReturn(userDto);

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        String login = "nonexistentuser";

        Mockito.when(userService.findUserInGithub(login)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testInternalServerError() throws Exception {
        String login = "testuser";

        Mockito.when(userService.findUserInGithub(login)).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get("/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
