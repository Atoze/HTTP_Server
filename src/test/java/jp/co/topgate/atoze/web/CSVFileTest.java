package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.forum.CSVFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/19.
 */
public class CSVFileTest {

    @Test
    public void CSVファイル読み書きテスト() throws IOException {
        CSVFile csv = new CSVFile();

        //空データテスト
        File file = new File("");
        List<String[]> data = csv.readCSV(file);
        assertThat(data, is(new ArrayList<String[]>()));

        //書き込みテスト
        String testFilePath = "src/test/Document/csv_test.txt";
        file = new File(testFilePath);

        String testText = "HOGE";
        csv.writeData(testText, file);

        //読み込みテスト
        file = new File(testFilePath);
        data = csv.readCSV(file);
        assertThat(data.size(), is(1));

        //二行書き込みテスト
        testText = "FOO";
        csv.writeData(testText, file,true);

        //改行入り読み込みテスト
        data = csv.readCSV(file);
        assertThat(data.size(), is(2));

        //改行終わりテスト

    }
}
