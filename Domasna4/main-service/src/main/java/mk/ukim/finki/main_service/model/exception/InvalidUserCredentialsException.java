package mk.ukim.finki.main_service.model.exception;

public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException() {
        super("Invalid Credentials");
    }
}
