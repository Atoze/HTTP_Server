package jp.co.topgate.atoze.web;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by atoze on 2017/04/18.
 */

public class MultiFormTest {

    @Test
    public void 画像データ取得のテスト() throws Exception {
        String lineFeed = "\r\n";//System.getProperty("line.separator");
        String boundary = "-----------------------------146617270317";
        byte[] bound = (boundary).getBytes();
        byte[] lineFeedByte = (lineFeed).getBytes();

        //MultiFormRequestを作る
        StringBuilder sb = new StringBuilder();
        sb.append("Content-Disposition: name=hoge").append(lineFeed);
        sb.append("Content-Type: image/plain").append(lineFeed).append(lineFeed);
        byte[] header = (lineFeed + sb.toString()).getBytes();

        File img = new File("src/test/Document/loading.gif");
        BufferedImage originalImage = ImageIO.read(img);
        byte[] imgByte = getBytesFromImage(originalImage, "gif");

        File file = new File("src/test/Document/test_imagePOST.txt");
        OutputStream output = new FileOutputStream(file);
        int length = 0;
        output.write(bound);
        length += bound.length;
        output.write(header);
        length += header.length;
        output.write(imgByte);
        length += imgByte.length;
        output.write(lineFeedByte);
        length += lineFeedByte.length;
        output.write(bound);
        length += bound.length;
        output.write(header);
        length += header.length;
        output.write(imgByte);
        length += imgByte.length;
        output.write(lineFeedByte);
        length += lineFeedByte.length;
        output.write(bound);
        length += bound.length;

        InputStream input = new FileInputStream(file);
        MultiFormData multi = new MultiFormData(input, length);
        byte[] test = multi.getByteData(1);

        // 出力バイト
        BufferedImage outputImage = getImageFromBytes(test);
        ImageIO.write(outputImage, "gif", new File("src/test/Document/loading2.gif"));
    }

    public static byte[] getBytesFromImage(BufferedImage img, String format) throws IOException {
        if (format == null) {
            format = "png";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, format, baos);
        return baos.toByteArray();
    }

    public static BufferedImage getImageFromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(baos);
        return img;
    }
}
