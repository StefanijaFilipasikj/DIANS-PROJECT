package mk.ukim.finki.landmark_microservice.model.exception;

public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException() {
        super("Invalid Credentials");
    }
}
