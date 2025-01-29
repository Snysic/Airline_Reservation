package airlinereservation.project.Airlinereservation.errors;

public class NotFoundException extends RuntimeException {

    private int status;
    private String message;

    public NotFoundException() {
        super();
    }

    public NotFoundException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotFoundException{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
