package jp.co.topgate.atoze.web.util;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

/**
 * ファイルの名前から拡張子を取得し,それに合わせた適切なContent-Typeを返します.
 *
 * @author atoze
 */
public class ContentType {
    private static final Map<String, String> CONTENT_TYPE_LIST = new HashMap<String, String>() {
        {
            put("octet-stream", "application/octet-stream");
            put("plain", "text/plain");
            put("text", "text/plain");
            put("txt", "text/plain");
            put("html", "text/html");
            put("htm", "text/html");
            put("css", "text/css");
            put("xml", "text/xml");

            put("js", "application/javascript");
            put("json", "application/json");

            put("jpeg", "image/jpeg");
            put("jpg", "image/jpeg");
            put("png", "image/png");
            put("gif", "image/gif");

            put("mp4", "video/mp4");
        }
    };

    /**
     * ファイルの拡張子を返します.
     *
     * @param fileName ファイル名
     * @return ファイル拡張子
     */

    @Contract(pure = true, value = "null -> null")
    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(point + 1, fileName.length());
        }
        return null;
    }

    /**
     * ファイルからContent-Typeを返します.
     *
     * @param fileName ファイル名
     * @return Content-Type
     */

    @Contract(pure = true, value = "null -> null")
    public static String getContentType(String fileName) {
        String extension = getFileExtension(fileName);
        if (fileName == null || extension == null) {
            return null;
        }

        if (CONTENT_TYPE_LIST.containsKey(extension)) {
            return CONTENT_TYPE_LIST.get(extension);
        } else {
            //DefaultContentType
            return CONTENT_TYPE_LIST.get("octet-stream");
        }
    }
}
