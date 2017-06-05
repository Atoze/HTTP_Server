package jp.co.topgate.atoze.web.util;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ファイル関連のユーティリティ
 */
public class FileUtil {
    /**
     * ファイルのエンコード値を求めます.
     *
     * @param file 判別するファイル
     * @return エンコード値
     */
    public static String detectFileEncoding(File file) {
        String result = null;
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        try {
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        detector.dataEnd();

        result = detector.getDetectedCharset();
        detector.reset();

        return result;
    }

}
