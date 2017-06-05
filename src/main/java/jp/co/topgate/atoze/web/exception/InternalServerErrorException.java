package jp.co.topgate.atoze.web.exception;

/**
 * Created by atoze on 2017/05/26.
 */
public class InternalServerErrorException extends Exception {
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}