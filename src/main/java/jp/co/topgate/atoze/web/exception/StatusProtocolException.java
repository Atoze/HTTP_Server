package jp.co.topgate.atoze.web.exception;

/**
 * Created by atoze on 2017/05/30.
 */
public class StatusProtocolException extends Exception {
    public StatusProtocolException() {
        super("対応していないプロトコルバージョンです");
    }
}
