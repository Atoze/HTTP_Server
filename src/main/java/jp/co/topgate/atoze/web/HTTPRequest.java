package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by atoze on 2017/04/12.
 */
public class HTTPRequest {
    private String bodyText;
    private String headerText;
    private String method;
    private String filepath;

    public HTTPRequest(InputStream input) throws IOException {
        HTTPHeader header = new HTTPHeader(input);
        headerText = header.getHeaderText();
        method = header.getMethod();
        filepath = header.getFilePath();

    }

    /*
        private StringBuilder readRequestBody(InputStream in){
            StringBuilder bodyText = new StringBuilder();
            return bodyText;
        }
    */
    public String getHeaderText() {
        return this.headerText;
    }

    public String getBodyText() {
        return this.bodyText;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFilePath() {
        return this.filepath;
    }


}
