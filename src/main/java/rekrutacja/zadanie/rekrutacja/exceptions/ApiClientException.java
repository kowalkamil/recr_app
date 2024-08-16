package rekrutacja.zadanie.rekrutacja.exceptions;

public class ApiClientException extends RuntimeException {
    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
