package mk.ukim.finki.landmark_microservice.model.exception;

public class InvalidReviewIdException extends RuntimeException{
    public InvalidReviewIdException(){
        super("Invalid review id");
    }
}
