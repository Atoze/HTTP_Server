package jp.co.topgate.atoze.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by atoze on 2017/05/16.
 */
class HTTPRequestBody {
    private String bodyText;
    private byte[] bodyFile;
    private Map<String, String> queryData = new HashMap<>();

    private int length;


    HTTPRequestBody(InputStream input, String contentType, int length) throws IOException {
        this.length = length;
        handler(input, contentType);
    }

    private void handler(InputStream input, String contentType) throws IOException{
        String[] contentTypeValue = contentType.split(";");
        switch (contentTypeValue[0]) {
            case "application/x-www-form-urlencoded":
                bodyText = readBodyText(input, length);
                queryData = readQueryData(bodyText);
                break;

            case "multipart/form-data":
                /* //TODO完成させる
                String[] boundary = contentTypeValue[1].split("=", 2);
                MultiFormData multiFormData = new MultiFormData(input,length);
                bodyFile = multiFormData.getByteData(0);
                break;
                */

            default:
                bodyFile = readBodyFile(input, length);
        }

    }

    private String readBodyText(InputStream input, int length) throws IOException {
        byte[] bodyTextByte = new byte[length];
        input.read(bodyTextByte, 0, length);
        String body = new String(bodyTextByte);
        return body.trim();
    }

    private Map<String, String> readQueryData(String body) {
        Map<String, String> queryData = new HashMap<>();
        String[] data = body.split("&");
        for (int i = 0; i <= data.length - 1; i++) {
            String[] queryValue = data[i].split("=", 2);
            if (queryValue.length >= 2) {
                queryData.put(queryValue[0], queryValue[1]);
            }
        }
        return queryData;
    }

    public byte[] readBodyFile(InputStream input, int length) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[length];
        while (true) {
            int len = input.read(buffer);
            if (len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        return bout.toByteArray();
    }

    public String getBodyText() {
        if (bodyText == null) {
            return new String(bodyFile);
            //return "";
        }
        return bodyText;
    }

    public byte[] getBodyFile() {
        return bodyFile;
    }

    public Map<String, String> getQueryData() {
        return queryData;
    }
}
