package mk.ukim.finki.main_service.model.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
