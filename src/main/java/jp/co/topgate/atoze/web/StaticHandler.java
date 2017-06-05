package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;
import jp.co.topgate.atoze.web.util.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        this.filePath = normalizeFilePath(filePath);
    }

    @Override
    public HTTPResponse generateResponse() {
        Status status;
        status = checkStatusCode(filePath);
        if (status == Status.OK) {
            File file = new File(".", filePath);
            ExtendedHTTPResponse response = new ExtendedHTTPResponse(status.getCode(), status.getMessage());
            String contentType = ContentType.getContentType(filePath.toString());
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
     * @param filePath 要求されたファイルパス
     * @return ステータスコード
     */
    private Status checkStatusCode(String filePath) {
        if (filePath == null) {
            return Status.FORBIDDEN;
        }
        File file = new File(".", filePath);
        if (!file.exists()) {
            return Status.NOT_FOUND;
        }
        if (!file.canRead()) {
            return Status.FORBIDDEN;
        }
        return Status.OK;
    }

    /**
     * URIのパスを元に実際のファイルパスを返す
     *
     * @return ファイルのパス
     */
    private static String normalizeFilePath(String uri) {
        Path normalizedFilePath = Paths.get(Server.ROOT_DIRECTORY, uri).normalize();
        if (!normalizedFilePath.startsWith(Server.ROOT_DIRECTORY)) {
            return null;
        }
        return normalizedFilePath.toString();
    }
}