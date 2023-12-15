package mk.ukim.finki.historicLandmarks.model.exception;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {
        super("Passwords Do Not Match");
    }
}
