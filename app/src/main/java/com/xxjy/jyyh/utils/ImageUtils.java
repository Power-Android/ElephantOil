package com.xxjy.jyyh.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_DCIM;
import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

public class ImageUtils {

//    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void saveImage(Context context, String data) {
        try {
            Bitmap bitmap = webData2bitmap(data);
            if (bitmap != null) {
                save2Album(context,bitmap, new Date().getTime() + ".jpg");
//                save2Album(context,bitmap, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()) + ".jpg");
            } else {
                runOnUiThread(() -> Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show());
            }

        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show());
            e.printStackTrace();
        }
    }

    public static  Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static  Bitmap webData2bitmap(String data) {
        byte[] imageBytes = Base64.decode(data.split(",")[1], Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes , 0, imageBytes.length,null);
    }

    public static  void save2Album(Context context,Bitmap bitmap, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            runOnUiThread(() -> {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            runOnUiThread(() ->
                    Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show());
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception ignored) {
            }
        }
    }



}