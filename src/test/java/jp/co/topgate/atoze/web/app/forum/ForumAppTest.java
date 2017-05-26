package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPRequestParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/26.
 */

public class ForumAppTest {
    @Test
    public void createThreadテスト() throws IOException {
        File file = new File("src/test/Document/forumAppData.csv");
        CSVFile csv = new CSVFile();
        List<String[]> data = csv.readCSV(file);
        int currentLastID = Integer.parseInt(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.ID.getKey()));

        ForumApp app = new ForumApp();
        app.setForumDataFile(file);

        StringBuilder sb = new StringBuilder();
        sb.append("POST /program/board/ HTTP/1.1\n");
        sb.append("Content-Type: application/x-www-form-urlencoded\n");
        String name = RandomStringUtils.randomAlphabetic(20);
        String query = "&title=test&text=document&password=foo";
        int length = ForumDataPattern.NAME.getQueryKey().getBytes().length + 1 + name.getBytes().length + query.getBytes().length;
        sb.append("Content-Length:").append(length).append("\n");
        sb.append("\n");

        sb.append(ForumDataPattern.NAME.getQueryKey());
        sb.append("=");
        sb.append(name);
        sb.append("&title=test&text=document&password=foo");

        //InputStream input = new FileInputStream(new File("src/test/Document/forumAppRequest"));
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
        HTTPRequest request = HTTPRequestParser.parse(input, "localhost:8080");

        app.createThread(request.getQuery());
        data = csv.readCSV(file);
        int newID = Integer.parseInt(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.ID.getKey()));
        assertThat(newID, is(currentLastID + 1));
        assertThat(name, is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.NAME.getKey())));
        assertThat("test", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.TITLE.getKey())));
        assertThat("document", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.TEXT.getKey())));
        assertThat("foo", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.PASSWORD.getKey())));
    }

    @Test
    public void findThreadテスト() throws IOException {
        ForumApp app = new ForumApp();
        File file = new File("src/test/Document/forumAppData.csv");
        app.setForumDataFile(file);
        app.findThread("test");
        assertThat(app.getMainData().size(),is(3));
    }
}
