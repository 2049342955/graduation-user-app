package com.demo.graduationuserapp.common;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {
    public static void uploadFile(byte[] file,File path, String fileName) throws Exception {
//        File targetFile = new File(path.getAbsolutePath(),"static/images/upload/");
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
        FileOutputStream out = new FileOutputStream(path.getPath()+File.separator+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
