package jp.co.topgate.atoze.web.exception;

/**
 * Created by atoze on 2017/05/26.
 */
public class StatusBadRequestException extends Exception {
    public StatusBadRequestException() {
        super();
    }

    public StatusBadRequestException(String msg) {
        super(msg);
    }
}