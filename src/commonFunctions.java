import java.io.*;

public class commonFunctions {
    public void writingToFile(String filename, StringBuilder sb){
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
