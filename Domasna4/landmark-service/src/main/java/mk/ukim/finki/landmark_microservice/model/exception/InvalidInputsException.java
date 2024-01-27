package mk.ukim.finki.landmark_microservice.model.exception;

public class InvalidInputsException extends RuntimeException{
    public InvalidInputsException(String message) {
        super(message);
    }
}
