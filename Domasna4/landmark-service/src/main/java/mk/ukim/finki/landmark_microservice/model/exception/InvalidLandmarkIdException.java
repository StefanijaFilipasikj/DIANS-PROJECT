package mk.ukim.finki.landmark_microservice.model.exception;

public class InvalidLandmarkIdException extends RuntimeException{
    public InvalidLandmarkIdException(){
        super("Invalid landmark id");
    }
}
