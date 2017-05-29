package jp.co.topgate.atoze.web.app.forum;

import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPRequestParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by atoze on 2017/05/26.
 */

public class ForumAppTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void createThreadテスト() throws IOException {
        Files.copy(new File("src/test/Document/", "forumAppData.csv").toPath(), new File(tempFolder.getRoot(), "forumAppData.csv").toPath());
        //File file = new File("src/test/Document/forumAppData.csv");
        File file = new File(tempFolder.getRoot(), "forumAppData.csv");
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

        app.createThread(request.getQuery(), "UTF-8");
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
        app.findThread("test", "UTF-8");
        assertThat(app.getMainData().size(), is(3));
    }

    @Test
    public void createThreadSHIFT_JISテスト() throws IOException {
        String ENCODER = "UTF-8";
        HTTPRequest request = HTTPRequestParser.parse(new FileInputStream("src/test/Document/forumAppRequestJIS"), "localhost:8080");
        if (request.getHeaderParam("Content-Type") != null) {
            String[] encode = request.getHeaderParam("Content-Type").split(";");
            if (encode.length >= 2 && encode[1].trim().startsWith("charset=")) {
                ENCODER = checkEncode(encode[1].substring("charset=".length() + 1).trim());
            }
        }

        File file = new File(tempFolder.getRoot(), "forumAppData.csv");
        ForumApp app = new ForumApp();
        app.setForumDataFile(file);
        CSVFile csv = new CSVFile();
        app.createThread(request.getQuery(), ENCODER);
        List<String[]> data = csv.readCSV(file);
        assertThat("ほげ", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.NAME.getKey())));
        assertThat("test", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.TITLE.getKey())));
        assertThat("document", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.TEXT.getKey())));
        assertThat("foo", is(ForumData.getParameter(data, data.size() - 1, ForumDataPattern.PASSWORD.getKey())));
        data.remove(data.size() - 1);
        new File(tempFolder.getRoot(), "forumAppData.csv").delete();
    }

    private String checkEncode(String encode) {
        try {
            URLDecoder.decode("", encode);
            return encode;
        } catch (UnsupportedEncodingException e) {
            return "UTF-8";
        }
    }
}
