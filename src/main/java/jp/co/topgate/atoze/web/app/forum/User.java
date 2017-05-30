package jp.co.topgate.atoze.web.app.forum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * NOT USED
 */
class User {

    UserData userData;

    public User() throws IOException {
        userData = new UserData();
    }

    boolean exists(String name) throws UnsupportedEncodingException {
        List<String[]> list = userData.getData();
        if (list == null || list.size() == 0) {
            return false;
        }
        for (int i = 0; i <= list.size() - 1; i++) {
            if (userData.getParameter(list, i, "name").equals(name)) {
                return true;
            }
        }
        return false;
    }

    void saveData(String text) throws IOException {
        userData.saveData(text);
    }

    String newUser(String name, int id) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("id:");
        sb.append(getNewId(userData.getData()));
        sb.append(",name:");
        sb.append(name);
        sb.append(",MessageID:");
        sb.append(id);
        return sb.toString();
    }

    String oldUser(String name, int id) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("id:");
        //sb.append(userData.getQuery());
        sb.append("name:");
        sb.append(name);
        sb.append("MessageID:");
        sb.append(id);
        return sb.toString();
    }

    public int getNewId(List<String[]> list) throws IOException {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(userData.getParameter(list, list.size() - 1, "id")) + 1;
    }
}
