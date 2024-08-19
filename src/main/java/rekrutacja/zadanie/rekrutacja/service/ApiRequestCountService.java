package rekrutacja.zadanie.rekrutacja.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rekrutacja.zadanie.rekrutacja.model.entity.ApiRequestCount;
import rekrutacja.zadanie.rekrutacja.repository.ApiRequestCountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiRequestCountService {

    private final ApiRequestCountRepository apiRequestCountRepository;

    public void incrementRequestCount(String login) {
        Optional<ApiRequestCount> existingRecord = apiRequestCountRepository.findById(login);

        if (existingRecord.isPresent()) {
            ApiRequestCount apiRequestCount = existingRecord.get();
            apiRequestCount.incrementRequestCount();
            apiRequestCountRepository.save(apiRequestCount);
        } else {
            apiRequestCountRepository.save(new ApiRequestCount(login, 1));
        }
    }
}

