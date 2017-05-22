package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.util.ContentType;

import java.io.File;
import java.util.Arrays;

/**
 * リクエストが静的コンテンツの場合のサーバーの挙動を制御します.
 */
public class StaticHandler extends HTTPHandler {
    private File file;
    private String HOST;

    StaticHandler(HTTPRequest request) {
        super(request);
        file = new File(Server.ROOT_DIRECTORY, request.getFilePath());
        HOST = request.getHost();
    }

    @Override
    public HTTPResponse generateResponse() {
        statusCode = checkStatusCode(HOST, file);
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
     * @param requestHost 　 要求されたホスト名
     * @param file        要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(String requestHost, File file) {
        if (requestHost == null || !requestHost.startsWith(HOST)) {
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
