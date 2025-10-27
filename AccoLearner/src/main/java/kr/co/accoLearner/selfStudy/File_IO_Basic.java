package kr.co.accoLearner.selfStudy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * in/out...Stream : 1byte단위로 처리 ( -128 ~ 127)
 * 
 * 채팅같은 경우 ->
 * ...Reader : 2byte단위 (한글 x)
 * ...Writer : 2byte단위 (한글 x)
 */
public class File_IO_Basic {
  
  public void fileIO() {
    File file = new File("C:/Users/SHIN_Arthur/data.txt");
    FileOutputStream fos = null;
    
    System.out.println( "파일 경로 : " + file.getAbsolutePath());

    
    try {
      fos = new FileOutputStream(file);
      fos.write(65);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if(fos != null) {
        try {
          fos.close();
        } catch (IOException e2) {
          // TODO: handle exception
        }
      }
    }
    
  }
}
