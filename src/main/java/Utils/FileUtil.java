package Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {
    public static String readFile(String filePath){
        StringBuilder stringBuilder=new StringBuilder();
            try (FileInputStream fileInputStream=new FileInputStream(filePath);){
                while (true){
                    int ch=fileInputStream.read();
                    if(ch==-1){
                        break;
                    }
                    stringBuilder.append((char)ch);
                }
                
            } catch (IOException e) {
            e.printStackTrace();
        }
            return stringBuilder.toString();
    }
    public static void writeFile(String filePath,String content){
            try(FileOutputStream fileOutputStream=new FileOutputStream(filePath)){
                fileOutputStream.write(content.getBytes());
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
