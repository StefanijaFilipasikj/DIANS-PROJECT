package mk.ukim.finki.historicLandmarks.model.exception;

public class InvalidLandmarkIdException extends RuntimeException{
    public InvalidLandmarkIdException(){
        super("Invalid landmark id");
    }
}
