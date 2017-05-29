package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.StatusBadRequestException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static jp.co.topgate.atoze.web.util.ParseUtil.readLine;

/**
 * Created by atoze on 2017/05/24.
 */
public class HTTPRequestParser {
    private final static String LINE_FEED = System.getProperty("line.separator");
    //private Map<String, String> headerField;

    private HTTPRequestParser() {
    }

    public static HTTPRequest parse(InputStream input, String host) throws IOException, StatusBadRequestException {
        BufferedInputStream bi = new BufferedInputStream(input);

        //RequestLine
        String line = readLine(input);
        HTTPRequestLine requestLine = new HTTPRequestLine(line, host);
        String method = requestLine.getMethod();
        HTTPRequest request = new HTTPRequest(method, requestLine.getFilePath(), requestLine.getProtocolVer(), host);
        request.setHeaderQuery(requestLine.getHeaderQuery());

        //RequestHeader
        line = readLine(input);
        StringBuilder text = new StringBuilder();
        while (line != null && !line.isEmpty()) {
            text.append(line).append(LINE_FEED);
            line = readLine(input);
        }
        HTTPRequestHeader header = new HTTPRequestHeader(text.toString());
        Map<String, String> headerField = header.getHeaderField();
        request.setHeader(text.toString(), headerField);

        //RequestBody
        String contentType = headerField.get("Content-Type");
        if (input.available() <= 0 || contentType == null || headerField.get("Content-Length") == null) {
            return request;
        }
        int contentLength = Integer.parseInt(headerField.get("Content-Length"));
        HTTPRequestBody body = new HTTPRequestBody(bi, contentType, contentLength);
        request.setBody(body.getBodyText(), body.getBodyFile());
        request.setBodyQuery(body.getQueryData());
        return request;

    }
}
