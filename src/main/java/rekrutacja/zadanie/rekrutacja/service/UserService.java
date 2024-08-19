package rekrutacja.zadanie.rekrutacja.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import rekrutacja.zadanie.rekrutacja.exceptions.ApiClientException;
import rekrutacja.zadanie.rekrutacja.exceptions.ApiServerException;
import rekrutacja.zadanie.rekrutacja.exceptions.InternalServerErrorException;
import rekrutacja.zadanie.rekrutacja.exceptions.UserNotFoundException;
import rekrutacja.zadanie.rekrutacja.model.dto.UserDto;
import rekrutacja.zadanie.rekrutacja.model.entity.User;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties
@Slf4j
public class UserService {

    private final ApiRequestCountService apiRequestCountService;
    private final RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String GITHUB_API_URL;

    @Transactional
    public UserDto findUserInGithub(String login) {
        try {
            User apiResult = restTemplate.getForObject(GITHUB_API_URL + login, User.class);
            if (apiResult != null) {
                apiRequestCountService.incrementRequestCount(login);
                return UserDto.toDto(apiResult);
            } else {
                throw new UserNotFoundException("No user found in Github for login : " + login);
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("No user found in Github for login : " + login, e);
            } else {
                throw new ApiClientException("Client Error: " + e.getStatusCode(), e);
            }
        } catch (HttpServerErrorException e) {
            throw new ApiServerException("Server Error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error: " + e.getMessage(), e);
        }
    }

}
