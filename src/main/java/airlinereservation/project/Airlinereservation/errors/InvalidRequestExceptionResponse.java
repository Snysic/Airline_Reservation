package airlinereservation.project.Airlinereservation.errors;

public class InvalidRequestExceptionResponse {

    private int status;
    private String message;

    public InvalidRequestExceptionResponse() {
    }

    public InvalidRequestExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "InvalidRequestExceptionResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
