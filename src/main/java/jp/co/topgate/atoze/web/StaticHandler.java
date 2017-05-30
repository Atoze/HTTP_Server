package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;

import java.io.File;
import java.util.Arrays;

import static jp.co.topgate.atoze.web.util.FileUtil.detectFileEncoding;

/**
 * リクエストが静的コンテンツの場合のサーバーの挙動を制御します.
 */
public class StaticHandler extends HTTPHandler {
    private final File file;

    StaticHandler(HTTPRequest request) {
        String filePath = request.getPath();
        if (filePath.endsWith("/")) {
            filePath += "index.html";
        }
        file = new File(Server.ROOT_DIRECTORY, filePath);
    }

    @Override
    public HTTPResponse generateResponse() {
        int statusCode = checkStatusCode(file);
        if (statusCode == 200) {
            HTTPResponse response = new HTTPResponse();
            if (Arrays.asList("html", "txt").contains(ContentType.getFileExtension(file.toString()))) {
                response.addResponseHeader("Content-Type", ContentType.getContentType(file.toString()) + "; charset=" + detectFileEncoding(file));
            } else {
                response.addResponseHeader("Content-Type", ContentType.getContentType(file.toString()));
            }
            response.setResponseBody(file);
            return response;

        } else {
            return this.generateErrorResponse(statusCode);
        }
    }

    /**
     * HTTPリクエストに応じてステータスコードを設定します.
     *
     * @param file 要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(File file) {
        if (!file.exists() || !file.isFile()) {
            return 404;
        }
        if (!file.canRead()) {
            return 403;
        }
        return 200;
    }
}