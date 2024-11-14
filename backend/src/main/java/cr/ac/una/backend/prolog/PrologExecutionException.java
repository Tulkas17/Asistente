package cr.ac.una.backend.prolog;

public class PrologExecutionException extends RuntimeException {
    public PrologExecutionException(String message) {
        super(message);
    }

    public PrologExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
