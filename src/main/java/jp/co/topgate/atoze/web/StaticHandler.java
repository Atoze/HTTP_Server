package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * リクエストが静的コンテンツの場合のサーバーの挙動を制御します.
 */
public class StaticHandler extends HTTPHandler {
    private String filePath;
    private String HOST;

    StaticHandler(String filePath, String host) throws IOException {
        this.HOST = host;
        this.filePath = filePath;
    }

    public void generateResponse(){
        File file = new File(Server.ROOT_DIRECTORY, filePath);
        statusCode = checkStatusCode(request, file);
        if (statusCode == 200) {
            if (Arrays.asList("html", "txt").contains(ContentType.getFileExtension(file.toString()))) {
                response.addResponseHeader("Content-Type", ContentType.getContentType(file.toString()) + "; charset=" + detectFileEncoding(file));
            } else {
                response.addResponseHeader("Content-Type", ContentType.getContentType(file.toString()));
            }
            response.setResponseBody(file);

        } else {
                this.generateErrorPage(statusCode);
        }
    }

    /**
     * HTTPリクエストに応じてステータスコードを設定します.
     *
     * @param request 　HTTPリクエスト
     * @param file    要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(HTTPRequest request, File file) {
        String host = request.getHeaderParam("HOST");
        if (host == null || !host.startsWith(HOST)) {
            return 400;
        }
        if (!file.exists() || !file.isFile()) {
            return 404;
        }
        if (!file.canRead()) {
            return 403;
        }
        return 200;
    }

}
