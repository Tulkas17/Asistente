package cr.ac.una.backend.prolog;

public class PrologUpdateException extends PrologExecutionException {
    public PrologUpdateException(String message) {
        super(message);
    }

    public PrologUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
