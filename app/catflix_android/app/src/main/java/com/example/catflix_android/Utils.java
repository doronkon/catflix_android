package com.example.catflix_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.provider.MediaStore;

public class Utils {
    public static int VIDEO_TYPE = 69;
    public static int IMAGE_TYPE = 420;
    public static String bitmapToBase64(Bitmap bitmap, int type) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String data = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String filePostfix = type == IMAGE_TYPE ? "png" : type == VIDEO_TYPE ? "mp4" : null;
        String typeString = type == IMAGE_TYPE ? "image" : type == VIDEO_TYPE ? "video" : null;
        return "data:" + typeString + "/" + filePostfix + ";base64," + data;
    }
    public static String videoUriToBase64(Context context, Uri filePath) {
        byte[] byteArray;
        try (InputStream inputStream = context.getContentResolver().openInputStream(filePath)) {
            assert inputStream != null;
            byteArray = new byte[inputStream.available()];
            inputStream.read(byteArray);
            return "data:video/mp4;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String imageUriToBase64(Context context, Uri filePath) {
        return Utils.bitmapToBase64(imageUriToBitmap(context, filePath), Utils.IMAGE_TYPE);
    }

    public static Bitmap imageUriToBitmap(Context context, Uri filePath) {
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
}
