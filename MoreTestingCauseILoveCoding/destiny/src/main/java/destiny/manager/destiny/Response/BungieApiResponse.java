package destiny.manager.destiny.Response;

public class BungieApiResponse<T> {
    private int errorCode;
    private String message;
    private T response;

    public BungieApiResponse() {}

    public BungieApiResponse(int errorCode, String message, T response) {
        this.errorCode = errorCode;
        this.message = message;
        this.response = response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
