package rekrutacja.zadanie.rekrutacja.exceptions;

public class ApiServerException extends RuntimeException {
    public ApiServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
