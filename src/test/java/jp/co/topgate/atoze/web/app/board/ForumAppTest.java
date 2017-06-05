package jp.co.topgate.atoze.web.app.board;

import jp.co.topgate.atoze.web.HTTPRequest;
import jp.co.topgate.atoze.web.HTTPRequestParser;
import jp.co.topgate.atoze.web.exception.BadRequestException;
import jp.co.topgate.atoze.web.exception.RequestBodyParseException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Before
    public void tempファイル作成() throws IOException {
        Files.copy(new File("src/test/Document/", "forumData.csv").toPath(), new File(tempFolder.getRoot(), "forumAppData.csv").toPath());
    }

    @After
    public void 削除() {
        new File(tempFolder.getRoot(), "forumData.csv").delete();
    }

    @Test
    public void createThreadテスト() throws IOException, BadRequestException, RequestBodyParseException {
        File file = new File(tempFolder.getRoot(), "forumAppData.csv");
        CSVFile csv = new CSVFile();
        List<String[]> data = csv.readCSV(file);
        String[] lastID = (data.get(data.size() - 1))[0].split(":");
        int currentLastID = Integer.parseInt(lastID[1]);

        ForumApp app = new ForumApp();
        app.setForumDataFile(file);

        StringBuilder sb = new StringBuilder();
        sb.append("POST /program/board/ HTTP/1.1\n");
        sb.append("Content-Type: application/x-www-form-urlencoded\n");
        String name = RandomStringUtils.randomAlphabetic(20);
        String queryString = "&title=test&text=document&password=foo";
        int length = ForumDataKey.NAME.getQueryKey().getBytes().length + 1 + name.getBytes().length + queryString.getBytes().length;
        sb.append("Content-Length:").append(length).append("\n");
        sb.append("\n");

        sb.append(ForumDataKey.NAME.getQueryKey());
        sb.append("=");
        sb.append(name);
        sb.append("&title=test&text=document&password=foo");

        //InputStream input = new FileInputStream(new File("src/test/Document/forumAppRequest"));
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
        HTTPRequest request = HTTPRequestParser.parse(input, "localhost:8080");

        app.createThread(request.getFormQueryParam(), "UTF-8");
        data = csv.readCSV(file);
        lastID = data.get(data.size() - 1);
        int newID = Integer.parseInt(lastID[0]);
        ForumData created_thread_data = app.getOutputData().get(app.getOutputData().size() - 1);

        assertThat(newID, is(currentLastID + 1));
        assertThat(name, is(created_thread_data.getName()));
        assertThat("test", is(created_thread_data.getTitle()));
        assertThat("document", is(created_thread_data.getText()));

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String savedPassword = created_thread_data.getPassword();
        assertThat(true, is(bCrypt.matches("foo", savedPassword)));
    }

    @Test
    public void findThreadテスト() throws IOException {
        ForumApp app = new ForumApp();
        File file = new File("src/test/Document/forumAppFind.csv");
        app.setForumDataFile(file);
        app.findThread("test", "UTF-8");
        assertThat(app.getOutputData().size(), is(3));
    }


    @Test
    public void createThreadSHIFT_JISで指定されたときテスト() throws IOException, BadRequestException, RequestBodyParseException {
        String ENCODER = "UTF-8";
        HTTPRequest request = HTTPRequestParser.parse(new FileInputStream("src/test/Document/forumAppRequestJIS"), "localhost:8080");
        if (request.getHeaderParam("Content-Type") != null) {
            String[] encode = request.getHeaderParam("Content-Type").split(";");
            if (encode.length >= 2 && encode[1].trim().startsWith("charset=")) {
                ENCODER = checkEncode(encode[1].substring("charset=".length() + 1).trim());
            }
        }

        File file = new File(tempFolder.getRoot(), "forumData.csv");
        ForumApp app = new ForumApp();
        app.setForumDataFile(file);
        app.createThread(request.getFormQueryParam(), ENCODER);
        ForumData created_thread_data = app.getOutputData().get(app.getOutputData().size()-1);
        assertThat("ほげ", is(URLDecoder.decode(created_thread_data.getName(),ENCODER)));
        assertThat("test", is(URLDecoder.decode(created_thread_data.getTitle(),ENCODER)));
        assertThat("document", is(URLDecoder.decode(created_thread_data.getText(),ENCODER)));

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String savedPassword = URLDecoder.decode(created_thread_data.getPassword(),ENCODER);
        assertThat(true, is(bCrypt.matches("foo", savedPassword)));
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
