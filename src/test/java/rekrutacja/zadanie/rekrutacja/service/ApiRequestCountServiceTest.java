package rekrutacja.zadanie.rekrutacja.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import rekrutacja.zadanie.rekrutacja.model.entity.ApiRequestCount;
import rekrutacja.zadanie.rekrutacja.repository.ApiRequestCountRepository;

import java.util.Optional;

@SpringBootTest
@Transactional
public class ApiRequestCountServiceTest {

    @Autowired
    private ApiRequestCountService apiRequestCountService;

    @Autowired
    private ApiRequestCountRepository apiRequestCountRepository;

    @Test
    public void testIncrementRequestCount_NewRecord() {
        String login = "newuser";
        Optional<ApiRequestCount> before = apiRequestCountRepository.findById(login);
        Assertions.assertTrue(before.isEmpty());

        apiRequestCountService.incrementRequestCount(login);

        Optional<ApiRequestCount> after = apiRequestCountRepository.findById(login);
        Assertions.assertTrue(after.isPresent());
        Assertions.assertEquals(1, after.get().getRequestCount());
    }

    @Test
    public void testIncrementRequestCount_ExistingRecord() {
        String login = "existinguser";
        ApiRequestCount existingRecord = new ApiRequestCount(login, 5);
        apiRequestCountRepository.save(existingRecord);

        apiRequestCountService.incrementRequestCount(login);

        Optional<ApiRequestCount> updatedRecord = apiRequestCountRepository.findById(login);
        Assertions.assertTrue(updatedRecord.isPresent());
        Assertions.assertEquals(6, updatedRecord.get().getRequestCount());
    }

    @Test
    public void testIncrementRequestCount_MultipleIncrements() {
        String login = "multipleuser";
        apiRequestCountService.incrementRequestCount(login);
        apiRequestCountService.incrementRequestCount(login);
        apiRequestCountService.incrementRequestCount(login);

        Optional<ApiRequestCount> updatedRecord = apiRequestCountRepository.findById(login);

        Assertions.assertTrue(updatedRecord.isPresent());
        Assertions.assertEquals(3, updatedRecord.get().getRequestCount());
    }
}