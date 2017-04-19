package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPResponse {
    private String bodyText;
    private StringBuilder response = new StringBuilder();
    private Map<String, Object> headers = new HashMap<>();

    public void setResponseBody(String text) {
        this.bodyText = text;
    }

    public void setResponseBody(File file) {


    }

    public void addLine(String type, Object name) {
        this.headers.put(type, name);
    }

    public void writeTo(int statusCode) throws IOException {
        Status status = new Status();
        status.setStatus(statusCode);
        response.append("HTTP/1.1 " + status.getStatus() + "\n");

        this.headers.forEach((key, value) -> {
            response.append(key + ": " + value + "\n");
        });

        if (this.bodyText != null) {
            response.append("\n");
            response.append(this.bodyText + "\n");
        }
    }

    public String getResponse() {
        return response.toString();
    }
}