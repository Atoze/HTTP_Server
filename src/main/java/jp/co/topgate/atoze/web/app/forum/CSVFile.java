package jp.co.topgate.atoze.web.app.forum;import org.jetbrains.annotations.Contract;import org.jetbrains.annotations.NotNull;import java.io.*;import java.util.ArrayList;import java.util.List;/** * CSVファイルの読み書きを行います. * * @author atoze */public class CSVFile {    private boolean endsLineFeed = true;    private final static String LINE_FEED = System.getProperty("line.separator");    /**     * CSVファイルに書き込んで保存します.     * 指定されたファイルがない場合は、新しく作成します.     *     * @param text   書き込むデータ     * @param file   書き込むファイル     * @param append 上書き保存（trueの場合は,既存のデータの後ろに新しい行を追加し書き込みます.     *               falseの場合は、ファイルに存在したデータは破棄されて上書きされます.     *               指定しない場合はfalseです.）     * @throws IOException 書き込みエラー     */    @Contract("_, null, _ -> fail; _, _, null ->fail; null, _ ,_ -> _")    public void writeData(String text, File file, boolean append) throws IOException {        if (text == null) {            text = "";        }        File outputFile = file;        if (!outputFile.exists()) {            outputFile.createNewFile();        }        if (!endsLineFeed && append) {            text = LINE_FEED + text;        }        FileOutputStream output = new FileOutputStream(outputFile, append);        PrintWriter writer = new PrintWriter(output, true);        writer.print(text);        writer.close();        output.close();    }    public void writeData(String text, File file) throws IOException {        writeData(text, file, false);    }    /**     * CSVファイルを行ごとに読み込みます.     * 列はutil.List,行はString配列で保管されています.     * 読み込み時に指定した列数に一致しない行は、破棄されます.     *     * @param file      ファイル     * @param columnNum 列数     * @return CSVデータ     * @throws IOException 読み込みエラー     */    @NotNull    @Contract("null, _ -> fail")    public List<String[]> readCSV(File file, int columnNum) throws IOException {        List<String[]> list = new ArrayList<>();        if (!file.exists()) {            return list;        }        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));        String line = readLine(br);        if (line == null) {            return list;        }        while (line != null) {            String[] data = line.split(",");            if (data.length == columnNum) list.add(data);            line = readLine(br);        }        if (list.size() == 0) {            return list;        }        String[] last = list.get(list.size() - 1);        if (!(last.length > columnNum && last[columnNum - 1].endsWith(LINE_FEED))) {            endsLineFeed = false;        }        return list;    }    /**     * CSVファイルを行ごとに読み込みます.     * 行はutil.List,列はString配列で保管されています.     *     * @param file ファイル     * @return CSVデータ     * @throws IOException 読み込みエラー     */    @NotNull    @Contract("null -> fail")    public List<String[]> readCSV(File file) throws IOException {        List<String[]> list = new ArrayList<>();        if (!file.exists()) {            return list;        }        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));        String line = readLine(br);        if (line == null) {            return list;        }        while (line != null) {            String[] data = line.split(",");            list.add(data);            line = readLine(br);        }        String[] last = list.get(list.size() - 1);        if (!(last.length > 0 && last[last.length - 1].endsWith(LINE_FEED))) {            endsLineFeed = false;        }        return list;    }    /**     * 読み込まれたストリームデータから、一行(改行コードまで)を読み込んで返します.     * 返される値には改行コードが含まれています.     *     * @param input 　ストリームデータ     * @return 改行含めた行     */    @Contract(pure = true, value = "null -> fail")    private static String readLine(BufferedInputStream input) {        int num = 0;        boolean skipLF = false;        StringBuffer sb = new StringBuffer();        try {            while ((num = input.read()) >= 0) {                switch ((char) num) {                    case '\r':                        skipLF = true;                        input.mark(1);                        break;                    case '\n':                        sb.append((char) num);                        return sb.toString();                    default:                        if (skipLF) {                            input.reset();                            return sb.toString();                        }                        break;                }                sb.append((char) num);            }        } catch (IOException e) {            throw new RuntimeException(e);        }        if (sb.length() == 0) {            return null;        } else {            return sb.toString();        }    }}