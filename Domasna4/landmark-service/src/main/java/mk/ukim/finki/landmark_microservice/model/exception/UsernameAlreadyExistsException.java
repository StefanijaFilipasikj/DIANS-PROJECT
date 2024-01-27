package mk.ukim.finki.landmark_microservice.model.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
