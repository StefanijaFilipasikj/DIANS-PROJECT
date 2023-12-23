package mk.ukim.finki.historicLandmarks.model.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("Username Already Exists");
    }
}
