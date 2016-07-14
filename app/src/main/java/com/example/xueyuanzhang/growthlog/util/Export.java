package com.example.xueyuanzhang.growthlog.util;

/**
 * Created by xueyuanzhang on 16/7/14.
 */



        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.drawable.Drawable;
        import android.os.Environment;
        import android.util.Log;

        import java.io.File;

        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;




public class Export {
    private static final int height=200;
    private static final int width=300;
    private static Bitmap bitmap;
    public static void createImage(String text){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLUE);
        p.setTextSize(20f);

        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawText(text,0,20,p);
        canvas.drawBitmap(bitmap,0,0,p);

            saveBitmap("ZZX.png");






    }
    private static void saveBitmap(String bitName)
    {
        try {
            File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/GrowthLog");
            file1.mkdirs();
            File file = new File(file1.getPath(),bitName);
            if(!file.exists()) {
                file.createNewFile();
            }
            Log.i("PATH", file.getPath());
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
