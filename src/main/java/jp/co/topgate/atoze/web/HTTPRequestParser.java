package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
        HTTPRequestLine requestLine = new HTTPRequestLine(line, host);
        String method = requestLine.getMethod();
        HTTPRequest request = new HTTPRequest(method, requestLine.getPath(), requestLine.getProtocolVer(), host);
        request.setHeaderQueryParam(requestLine.getQueryParam());

        //RequestHeader
        Map<String, String> headerField = new HashMap<>();
        line = readLine(input);
        StringBuffer header = new StringBuffer();
        while (line != null && !line.isEmpty()) {
            header.append(line).append(LINE_FEED);
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                headerField.put(headerLineData[0], headerLineData[1].trim());
            }
            line = readLine(input);
        }
        request.setHeader(header.toString(), headerField);

        //RequestBody
        if (input.available() <= 0) {
            return request;
        }
        request.setBody(input);
        return request;
    }
}
