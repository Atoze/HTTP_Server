package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static jp.co.topgate.atoze.web.util.ParseUtil.readLine;

/**
 * Created by atoze on 2017/05/23.
 */
class HTTPRequestHeader {
    private String header;
    private Map<String, String> headerField = new HashMap<>();

    private final static String LINE_FEED = System.getProperty("line.separator");

    HTTPRequestHeader(InputStream input) throws IOException {
        readRequest(input);
    }

    HTTPRequestHeader(String input) throws IOException {
        readRequest(input);
    }

    private void readRequest(InputStream input) throws IOException {
        String line = readLine(input);
        StringBuilder text = new StringBuilder();
        while (line != null && !line.isEmpty()) {
            text.append(line).append(LINE_FEED);
            String[] headerLineData = line.split(":", 2);
            if (headerLineData.length == 2) {
                this.headerField.put(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
            line = readLine(input);
        }
        this.header = text.toString();
    }

    private void readRequest(String input) throws IOException {
        header = input;
        String[] header = input.split(LINE_FEED);
        for (int i = 0; i < header.length; i++) {
            String[] headerLineData = header[i].split(":", 2);
            if (header.length >= 2) {
                this.headerField.put(headerLineData[0].toUpperCase(), headerLineData[1].trim());
            }
        }
    }

    String getRequestHeader() {
        return header;
    }

    Map<String, String> getHeaderField() {
        return headerField;
    }

}
