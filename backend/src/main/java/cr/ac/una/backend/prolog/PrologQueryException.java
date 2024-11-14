package cr.ac.una.backend.prolog;

public class PrologQueryException extends PrologExecutionException {
    public PrologQueryException(String message) {
        super(message);
    }

    public PrologQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
