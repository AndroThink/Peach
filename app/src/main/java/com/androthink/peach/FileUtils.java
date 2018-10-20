package com.androthink.peach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

    public static File getFileFromDrawable(Context context, int drawableId, String fileNameWithoutExtension, String mimeType){
        try
        {
            File file = createTempFile(context,
                    (getCurrentTimeStamp() + "_" + fileNameWithoutExtension)
                            .replace(" ","")
                            .replace("-",""),
                    mimeType);

            InputStream inputStream = context.getResources().openRawResource(drawableId);
            OutputStream out=new FileOutputStream(file);
            byte buf[]=new byte[1024];
            int len;
            while((len=inputStream.read(buf))>0)
                out.write(buf,0,len);
            out.close();
            inputStream.close();

            return file;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static File getFileFromBitmap(Context context, Bitmap bitmap, String fileNameWithoutExtension, String mimeType){
        try
        {
            File file = createTempFile(context,
                    (getCurrentTimeStamp() + "_" + fileNameWithoutExtension)
                            .replace(" ","")
                            .replace("-",""),
                    mimeType);

            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();

            return file;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static File createTempFile(Context context, String fileNameWithoutExtension,String mimeType)
            throws IOException {
        File externalCacheDir = context.getExternalCacheDir();
        File internalCacheDir = context.getCacheDir();
        File cacheDir;
        if (externalCacheDir == null && internalCacheDir == null) {
            throw new IOException("No cache directory available");
        }
        if (externalCacheDir == null) {
            cacheDir = internalCacheDir;
        }
        else if (internalCacheDir == null) {
            cacheDir = externalCacheDir;
        } else {
            cacheDir = externalCacheDir.getFreeSpace() > internalCacheDir.getFreeSpace() ?
                    externalCacheDir : internalCacheDir;
        }
        return File.createTempFile(fileNameWithoutExtension, mimeType, cacheDir);
    }

    private static String getCurrentTimeStamp() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm_aa", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }
}
