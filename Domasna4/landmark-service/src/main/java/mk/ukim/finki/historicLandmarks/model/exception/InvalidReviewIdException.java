package mk.ukim.finki.historicLandmarks.model.exception;

public class InvalidReviewIdException extends RuntimeException{
    public InvalidReviewIdException(){
        super("Invalid review id");
    }
}
