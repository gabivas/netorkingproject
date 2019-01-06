package utils;

import java.io.Serializable;

public class ClientResponse implements Serializable {

    private int type;
    private String message;

    public ClientResponse(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessfullyLoggedIn() {
        return type==Constants.SUCCESSFULLY_LOG_IN;
    }

    public boolean isSuccessfullyLoggedOut() {
        return type==Constants.SUCCESSFULLY_LOG_OUT;
    }

    public boolean isInvalidNumber() {
        return type==Constants.INVALID_NUMBER;
    }

    public boolean isTryResponse() {
        return type==Constants.TRY_RESPONSE;
    }

    public boolean isNumberFound() {
        return type==Constants.NUMBER_FOUND;
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isUserExits() {
        return type==Constants.USER_EXISTS;
    }
}
