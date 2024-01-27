package mk.ukim.finki.main_service.model.exception;

public class InvalidReviewIdException extends RuntimeException{
    public InvalidReviewIdException(){
        super("Invalid review id");
    }
}
