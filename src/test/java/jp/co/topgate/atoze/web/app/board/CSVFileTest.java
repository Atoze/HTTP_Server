package jp.co.topgate.atoze.web.app.board;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/19.
 */
public class CSVFileTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void tempファイル作成() throws IOException {
        Files.copy(new File("src/test/Document/", "csv_test").toPath(), new File(tempFolder.getRoot(), "csv_test").toPath());
    }

    @After
    public void 削除() {
        new File(tempFolder.getRoot(), "csv_test").delete();
    }

    @Test
    public void CSVファイル読み書きテスト() throws IOException {
        CSVFile csv = new CSVFile();

        //空データテスト
        File file = new File("");
        List<String[]> data = csv.readCSV(file);
        assertThat(data, is(new ArrayList<String[]>()));

        //書き込み
        //String testFilePath = "src/test/Document/csv_test";
        file = new File(tempFolder.getRoot(), "csv_test");
        String testText = "HOGE";
        csv.writeData(testText, file, false);

        //読み込み
        data = csv.readCSV(file);

        String[] readText = data.get(0);
        assertThat(data.size(), is(1));
        assertThat(testText, is(readText[0]));

        //null書き込みテスト
        testText = null;
        csv.writeData(testText, file, false);
        data = csv.readCSV(file);
        assertThat(data.size(), is(0));

        //新規行追加書き込み
        testText = "FOO";
        csv.writeData(testText, file, true);

        //改行入り読み込みテスト
        data = csv.readCSV(file);
        assertThat(data.size(), is(2));
        readText = data.get(1);
        assertThat(testText, is(readText[0]));

        //改行判定テスト
        testText = "HOGE\nFOO,BAR\rHOGE1\r\nHOGE2\n\r";
        csv.writeData(testText, file, false);
        data = csv.readCSV(file);
        assertThat(data.size(), is(5));

        readText = data.get(0);
        assertThat("HOGE\n", is(readText[0]));
        readText = data.get(1);
        assertThat("FOO", is(readText[0]));
        assertThat("BAR\r", is(readText[1]));
        readText = data.get(2);
        assertThat("HOGE1\r\n", is(readText[0]));
        readText = data.get(3);
        assertThat("HOGE2\n", is(readText[0]));
        readText = data.get(4);
        assertThat("\r", is(readText[0]));

        file.delete();
    }

    @Test
    public void CSVファイル行数指定読み込みテスト() throws IOException {
        CSVFile csv = new CSVFile();

        String testFilePath = "src/test/Document/csv_ColumnTest";
        File file = new File(testFilePath);
        //通常読み込み
        List<String[]> data = csv.readCSV(file);
        assertThat(data.size(), is(4));

        //指定読み込み
        data = csv.readCSV(file, 3);
        assertThat(data.size(), is(2));

        //指定読み込み
        data = csv.readCSV(file, 4);
        assertThat(data.size(), is(1));
    }


}
