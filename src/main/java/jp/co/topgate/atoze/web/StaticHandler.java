package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;
import jp.co.topgate.atoze.web.util.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static jp.co.topgate.atoze.web.util.FileUtil.detectFileEncoding;

/**
 * リクエストが静的コンテンツの場合のサーバーの挙動を制御します.
 */
public class StaticHandler extends HTTPHandler {
    private final String filePath;

    //TODO:text/*形式も含んだ方がいいのか否か（cssは独自に設定できるからいらない？）
    private final static List<String> CHARSET_REQUIRED_FILE_EXTENSION = Arrays.asList("html", "htm", "xml", "txt");

    public StaticHandler(HTTPRequest request) {
        String filePath = request.getPath();
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }
        this.filePath = filePath;
    }

    @Override
    public HTTPResponse generateResponse() {
        File file = new File(Server.ROOT_DIRECTORY, filePath);
        Status status = checkStatusCode(file);
        if (status == Status.OK) {
            ExtendedHTTPResponse response = new ExtendedHTTPResponse(status.getCode(), status.getMessage());
            String contentType = ContentType.getContentType(filePath);
            try {
                if (CHARSET_REQUIRED_FILE_EXTENSION.contains(contentType)) {
                    response.setResponseBody(file, contentType, detectFileEncoding(file));
                } else {
                    response.setResponseBody(file, contentType);
                }
            } catch (FileNotFoundException e) {
                status = Status.NOT_FOUND;
                return generateErrorResponse(status);
            }
            return response;
        } else {
            return this.generateErrorResponse(status);
        }
    }

    /**
     * HTTPリクエストに応じてステータスを返します.
     *
     * @param file 要求されたファイルパス
     * @return ステータスコード
     */
    private Status checkStatusCode(File file) {
        if (!file.exists()) {
            return Status.NOT_FOUND;
        }
        if (!file.canRead()) {
            return Status.FORBIDDEN;
        }
        return Status.OK;
    }
}