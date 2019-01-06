package utils;

import java.io.Serializable;

public class ClientRequest implements Serializable {

    private int type;
    private String name;
    private String message;

    public ClientRequest(int type, String name, String message) {
        this.type = type;
        this.name = name;
        this.message = message;
    }


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLogin() {
        return type==Constants.LOG_IN;
    }

    public boolean isTry() {
        return type==Constants.TRY;
    }

    public boolean isLogout() {
        return type==Constants.LOG_OUT;
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
