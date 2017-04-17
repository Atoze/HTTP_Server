package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPResponse {
    private String bodyText;
    private static String status;
    private StringBuilder response = new StringBuilder();

    private Map<String, String> headers = new HashMap<>();

    ContentType contentType = new ContentType();


    public HTTPResponse() {
    }

    public void setResponseBody(String text) {
        this.bodyText = text;
    }

    public void setResponseBody(File file) {
    }

    public void addLine(String type, String name) {
        this.headers.put(type, name);
    }

    public void writeTo() throws IOException {
        //PrintWriter writer = new PrintWriter(out, true);
        response.append("HTTP/1.1 " + Status.getStatus() + "\n");

        this.headers.forEach((key, value) -> {
            response.append(key + ": " + value + "\n");
        });

        if (this.bodyText != null) {
            response.append("\n");
            response.append(this.bodyText + "\n");
        }
        //writer.print(response.toString());
    }
    public String getResponse() {
        return response.toString();
    }

    public void setError(Integer error) {
        Status.setStatusCode(error);
        File file = new File(error.toString() + ".html");
        if (file.exists() && file.isFile() && file.canRead()) {
            this.setResponseBody(file);
        } else {
            this.setResponseBody("<html><head><title>" + Status.getStatus() + "</title></head><body><h1>" +
                    Status.getStatus() + "</h1></body></html>");
        }
    }
}