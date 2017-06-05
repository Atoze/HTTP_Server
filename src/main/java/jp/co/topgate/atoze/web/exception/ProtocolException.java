package jp.co.topgate.atoze.web.exception;

/**
 * Created by atoze on 2017/05/30.
 */
public class ProtocolException extends Exception {
    public ProtocolException() {
        super("対応していないプロトコルバージョンです");
    }
}
