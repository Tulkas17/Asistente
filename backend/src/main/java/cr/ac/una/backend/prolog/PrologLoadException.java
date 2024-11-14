package cr.ac.una.backend.prolog;

public class PrologLoadException extends PrologExecutionException {
    public PrologLoadException(String message) {
        super(message);
    }

    public PrologLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
