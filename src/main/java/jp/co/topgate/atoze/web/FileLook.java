package jp.co.topgate.atoze.web;

import java.io.File;

/**
 * Created by atoze on 2017/04/12.
 */
class FileLook {
    public static File file;

    public void readFile(File file) {

    }

    public boolean ifcheckFile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }
        return false;
    }

    public void getcheckFile(File file) {


    }


}
