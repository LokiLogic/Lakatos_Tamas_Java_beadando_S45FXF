package hu.lakatostamas.beadando.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(Class<?> entityClass, Long id) {
        super("Nem található " + entityClass.getSimpleName() + " azonosítóval: " + id);
    }
}
