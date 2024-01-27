package mk.ukim.finki.main_service.model.exception;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("Passwords do not match");
    }
}
