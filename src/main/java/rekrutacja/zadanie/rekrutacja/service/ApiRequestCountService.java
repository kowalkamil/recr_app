package rekrutacja.zadanie.rekrutacja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rekrutacja.zadanie.rekrutacja.model.entity.ApiRequestCount;
import rekrutacja.zadanie.rekrutacja.repository.ApiRequestCountRepository;

import java.util.Optional;

@Service
public class ApiRequestCountService {

    private final ApiRequestCountRepository apiRequestCountRepository;

    @Autowired
    public ApiRequestCountService(ApiRequestCountRepository apiRequestCountRepository) {
        this.apiRequestCountRepository = apiRequestCountRepository;
    }

    public void incrementRequestCount(String login) {
        Optional<ApiRequestCount> existingRecord = apiRequestCountRepository.findById(login);

        if (existingRecord.isPresent()) {
            ApiRequestCount apiRequestCount = existingRecord.get();
            apiRequestCount.incrementRequestCount();
            apiRequestCountRepository.save(apiRequestCount);
        } else {
            ApiRequestCount newRecord = new ApiRequestCount(login, 1);
            apiRequestCountRepository.save(newRecord);
        }
    }
}

