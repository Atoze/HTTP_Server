package jp.co.topgate.atoze.web.exception;

/**
 * Created by atoze on 2017/05/26.
 */
public class BadRequestException extends Exception {
    public BadRequestException(String msg) {
        super(msg);
    }
}