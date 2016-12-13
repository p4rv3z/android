package bd.com.parvez.copy;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import bd.com.parvez.services.MyTag;

/**
 * Created by ParveZ on 08-May-16.
 * Coping files and folder from assets to External storage
 * Make sure you have Smart phone
 * get write and read access
 */
public class Copy {
    private static final String classname = "Copy::";

    /**
     *
     * @param context
     * @param loc Assets Sub folder name here i save as css
     * @param to external storage path
     */
    public static void copyFromAssets(Context context, String loc, String to) {
        File file = new File(to);
        if (!file.exists()){
            file.mkdirs();
        }
        AssetManager assetManager = context.getAssets();
        String[] files = null;

        try {
            files = assetManager.list(loc);
        } catch (IOException e) {
            MyTag.Tag(classname, e.getMessage() + "");
        }
        for (String fileName : files) {
            InputStream in = null;
            OutputStream out = null;
            if (fileName.contains(".")) {
                if (!new File(to+"/"+fileName).exists()) {
                    try {
                        in = assetManager.open((loc + "/" + fileName).toString());
                        out = new FileOutputStream(to + "/" + fileName);
                        MyTag.Tag(classname, "File Name 2: " + loc + "/" + fileName + "\nPath 2: " + to + "/" + fileName);
                        copyFiles(in, out, to);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    } catch (Exception e) {
                        MyTag.Tag(classname, e.getMessage() + "");
                    }
                }else {
                    MyTag.Tag(classname, "File Already exists.");
                }
            } else {
                String path = to +"/"+ fileName;
                copyFromAssets(context, (loc + "/" + fileName).toString(), path);
                MyTag.Tag(classname, "Recursive: File Name:" + loc + "/" + fileName + "\nPath: " + path);
            }
        }
    }

    private static void copyFiles(InputStream in, OutputStream out, String to) throws IOException {
        MyTag.Tag(classname,"Copy Files method: "+ to);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
