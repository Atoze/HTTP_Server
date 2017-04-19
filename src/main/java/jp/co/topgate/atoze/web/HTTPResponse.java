package jp.co.topgate.atoze.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/04/12.
 */
class HTTPResponse {
    private String bodyText;
    private File bodyFile;
    private StringBuilder response = new StringBuilder();
    private Map<String, String> headers = new HashMap<>();

    public void setResponseBody(String text) {
        this.bodyText = text;
    }

    public void setResponseBody(File file) {
        this.bodyFile = file;
    }

    public void addText(String type, String name) {
        this.headers.put(type, name);
    }

    public void writeTo(OutputStream out, Status status) throws IOException {
        PrintWriter writer = new PrintWriter(out, true);

        this.response.append("HTTP/1.1 " + status.getStatus() + "\n");

        this.headers.forEach((key, value) -> {
            this.response.append(key + ": " + value + "\n");
        });

        if (this.bodyText != null) {
            this.response.append("\n").append(this.bodyText + "\n");
        }
        writer.println(this.response.toString());

        if (this.bodyFile != null) {
            BufferedInputStream bi
                    = new BufferedInputStream(new FileInputStream(this.bodyFile));
            try {
                for (int c = bi.read(); c >= 0; c = bi.read()) {
                    out.write(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (bi != null) {
                    bi.close();
                }
            }
        }
    }

    public String getResponse() {
        return response.toString();
    }
}