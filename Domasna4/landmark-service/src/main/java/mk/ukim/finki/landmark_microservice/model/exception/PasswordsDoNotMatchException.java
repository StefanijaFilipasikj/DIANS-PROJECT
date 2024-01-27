package mk.ukim.finki.landmark_microservice.model.exception;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("Passwords do not match");
    }
}
