package jp.co.topgate.atoze.web;

import java.io.*;

/**
 * Created by atoze on 2017/04/12.
 */
public class HTTPResponse {
    private String headerText;
    private String bodyText;
    private File bodyFile;

    HTTPResponse(OutputStream out) throws IOException {

    }

    public String getResponseHeader() {
        return "";
    }

    public StringBuilder getResponseBody() {
        StringBuilder paipai = new StringBuilder();
        return paipai;
    }


}
