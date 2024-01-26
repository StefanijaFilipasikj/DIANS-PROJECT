package mk.ukim.finki.historicLandmarks.model.exception;

public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException() {
        super("Invalid Credentials");
    }
}
