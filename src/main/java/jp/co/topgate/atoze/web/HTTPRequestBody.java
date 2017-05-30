package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ParseUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 　HTTPリクエストボディを処理するクラス.
 */
class HTTPRequestBody {
    private String bodyText;
    private byte[] bodyFile;
    InputStream bodyInput;
    private Map<String, String> query = new HashMap<>();

    HTTPRequestBody(InputStream input, String contentType, int length) throws IOException {
        readRequest(input, contentType, length);
    }

    private void readRequest(InputStream input, String contentType, int length) throws IOException {
        String[] contentTypeValue = contentType.split(";");
        switch (contentTypeValue[0]) {
            case "application/x-www-form-urlencoded":
                bodyText = readBodyText(input, length);
                query = ParseUtil.parseQueryData(bodyText);
                break;

            //TODO 完成させる
            case "multipart/form-data":
                /*
                String[] boundary = contentTypeValue[1].split("=", 2);
                MultiFormData multiFormData = new MultiFormData(input,length);
                bodyFile = multiFormData.getByteData(0);
                break;
                */

            default:
                //bodyInput = input;
                bodyFile = readBodyFile(input, length);
        }
    }

    private String readBodyText(InputStream input, int length) throws IOException {
        byte[] bodyTextByte = new byte[length];
        input.read(bodyTextByte, 0, length);
        String body = new String(bodyTextByte);
        return body.trim();
    }

    private byte[] readBodyFile(InputStream input, int length) throws IOException {
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

    String getBodyText() {
        if (bodyText == null) {
            return new String(bodyFile);
        }
        return bodyText;
    }

    byte[] getBodyFile() {
        return bodyFile;
    }

    InputStream getBodyInput() {
        return bodyInput;
    }

    Map<String, String> getQuery() {
        return query;
    }
}
