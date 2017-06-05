package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static jp.co.topgate.atoze.web.util.ParseUtil.readLine;

/**
 * Created by atoze on 2017/05/24.
 */
public class HTTPRequestParser {
    private final static String LINE_FEED = System.getProperty("line.separator");

    private HTTPRequestParser() {
    }

    public static HTTPRequest parse(InputStream input, String host) throws IOException, BadRequestException {
        //BufferedInputStream bi = new BufferedInputStream(input);

        //RequestLine
        String line = readLine(input);
        HTTPRequestLineParser requestLine = new HTTPRequestLineParser(line, host);
        String method = requestLine.getMethod();
        HTTPRequest request = new HTTPRequest(method, requestLine.getPath(), requestLine.getProtocolVer(), host);
        request.setHeaderQuery(requestLine.getQuery());

        //RequestHeader
        line = readLine(input);
        StringBuilder text = new StringBuilder();

        while (line != null && !line.isEmpty()) {
            text.append(line).append(LINE_FEED);
            line = readLine(input);
        }

        HTTPRequestHeaderParser header = new HTTPRequestHeaderParser(text.toString());
        Map<String, String> headerField = header.getHeaderField();
        request.setHeader(text.toString(), headerField);

        //RequestBody
        if (input.available() <= 0) {
            return request;
        }
        request.setBody(input);
        return request;
    }
}
