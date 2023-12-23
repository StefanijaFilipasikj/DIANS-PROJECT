package mk.ukim.finki.historicLandmarks.model.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super("Username Not Found");
    }
}
