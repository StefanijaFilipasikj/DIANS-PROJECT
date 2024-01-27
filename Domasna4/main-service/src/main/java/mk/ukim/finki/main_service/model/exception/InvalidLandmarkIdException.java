package mk.ukim.finki.main_service.model.exception;

public class InvalidLandmarkIdException extends RuntimeException{
    public InvalidLandmarkIdException(){
        super("Invalid landmark id");
    }
}
