package cr.ac.una.backend.exception;

import cr.ac.una.backend.prolog.PrologLoadException;
import cr.ac.una.backend.prolog.PrologQueryException;
import cr.ac.una.backend.prolog.PrologUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PrologLoadException.class)
    public ResponseEntity<String> handlePrologLoadException(PrologLoadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cargar el archivo de reglas Prolog: " + e.getMessage());
    }

    @ExceptionHandler(PrologQueryException.class)
    public ResponseEntity<String> handlePrologQueryException(PrologQueryException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar consulta en Prolog: " + e.getMessage());
    }

    @ExceptionHandler(PrologUpdateException.class)
    public ResponseEntity<String> handlePrologUpdateException(PrologUpdateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar clima en Prolog: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
    }
}
