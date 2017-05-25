package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.forum.CSVFile;
import jp.co.topgate.atoze.web.app.forum.ForumData;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/25.
 */
public class ForumDataTest {
    @Test
    public void 正常CSVテスト() throws IOException {
        File file = new File("src/test/Document/forumData.csv"); //実データに近いもの
        ForumData forum = new ForumData(file);
        assertThat(forum.getData().size(), is(3));
    }

    @Test
    public void 空データCSVテスト() throws IOException {
        File file = new File("src/test/Document/forumNoData.csv");
        ForumData forum = new ForumData(file);
        assertThat(forum.getData().size(), is(0));
    }

    @Test
    public void IDか番号から始まらないデータをcheckDataで省くテスト() throws IOException {
        File file = new File("src/test/Document/forumDataWithIdBug.csv");
        CSVFile csv = new CSVFile();
        assertThat((csv.readCSV(file)).size(), is(4));
        ForumData forum = new ForumData(file);
        assertThat(forum.getData().size(), is(2));
    }

    @Test
    public void 重複IDをcheckDataで省くテスト() throws IOException {
        File file = new File("src/test/Document/forumDoubledId.csv");
        CSVFile csv = new CSVFile();
        assertThat((csv.readCSV(file)).size(), is(3));
        ForumData forum = new ForumData(file);
        assertThat(forum.getData().size(), is(2));
    }
}
