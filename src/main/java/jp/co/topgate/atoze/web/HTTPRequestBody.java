package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.exception.RequestBodyParseException;
import jp.co.topgate.atoze.web.util.ParseUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 　HTTPリクエストボディを処理するクラス.
 */
public class HTTPRequestBody {
    private String bodyText;
    private byte[] bodyFile;
    InputStream bodyInput;
    private Map<String, String> query = new HashMap<>();

    HTTPRequestBody(InputStream input, Map<String, String> headerField) throws IOException, RequestBodyParseException {
        parse(input, headerField);
    }

    private void parse(InputStream input, Map<String, String> headerField) throws IOException, RequestBodyParseException {
        String contentType = headerField.get("Content-Type");
        if (input == null || input.available() == 0) {
            return;
        }
        if (contentType == null) {
            throw new RequestBodyParseException("対応していないContent-Typeです");
        }
        String[] contentTypeValue = contentType.split(";");
        switch (contentTypeValue[0]) {
            case "application/x-www-form-urlencoded":
                int length = Integer.parseInt(headerField.get("Content-Length"));
                String text = readBodyText(input, length);
                bodyText = text;
                query = ParseUtil.parseQueryData(text);
                break;

            //TODO 完成させる
            case "multipart/form-data":
                /*
                String[] boundary = contentTypeValue[1].split("=", 2);
                MultiFormData multiFormData = new MultiFormData(input,length);
                bodyFile = multiFormData.getByteData(0);
                break;
                */
                length = Integer.parseInt(headerField.get("Content-Length"));
                bodyFile = readBodyFile(input, length);
                break;

            default:
                bodyInput = input;
                throw new RequestBodyParseException("対応していないContent-Typeです");
        }
    }

    private static String readBodyText(InputStream input, int length) throws IOException {
        byte[] bodyTextByte = new byte[length];
        input.read(bodyTextByte, 0, length);
        String body = new String(bodyTextByte);
        return body.trim();
    }

    private static byte[] readBodyFile(InputStream input, int length) throws IOException {
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
        if (bodyText == null && bodyFile != null) {
            return new String(bodyFile);
        }
        return bodyText;
    }

    byte[] getBodyFile() {
        return bodyFile;
    }

    public InputStream getBodyInput() {
        return bodyInput;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
