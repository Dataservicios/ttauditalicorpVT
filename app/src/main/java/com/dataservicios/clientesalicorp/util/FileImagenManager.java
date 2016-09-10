package com.dataservicios.clientesalicorp.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.dataservicios.clientesalicorp.AlbumStorageDirFactory;
import com.dataservicios.clientesalicorp.BaseAlbumDirFactory;
import com.dataservicios.clientesalicorp.FroyoAlbumDirFactory;
import com.dataservicios.clientesalicorp.R;

import java.io.File;




/**
 * Created by Jaime on 30/08/2016.
 */
public class FileImagenManager  {




    /**
     * Scale imagen
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return
     */
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    /**
     * Rotate imagen
     * @param img
     * @param degree
     * @return
     */
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    private static String getAlbunNameTemp(Context context){
        return  context.getString(R.string.album_name_temp);
    }

    public static File getAlbumDirTemp(Context context) {

        AlbumStorageDirFactory mAlbumStorageDirFactory = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }


        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbunNameTemp(context));

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d(getAlbunNameTemp(context), "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(String.valueOf(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
}
