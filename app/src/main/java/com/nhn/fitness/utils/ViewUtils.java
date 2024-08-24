package com.nhn.fitness.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;

public class ViewUtils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * @param context
     * @param path
     * @param imageView
     * @param type      0: small, 1: normal, 2: large
     */
    public static void bindImage(Context context, String path, ImageView imageView, int type) {
        int width, height;
        if (type == 0) {
            width = height = 120;
        } else {
            width = 500;
            height = 280;
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Glide.with(context)
                    .load(path)
                    .apply(new RequestOptions()
                            .override(width, height)
                    )
                    .into(imageView);
        } else {
            Glide.with(imageView).load(path)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(width, height))
                    .into(imageView);
        }
    }

    /**
     * @param context
     * @param path
     * @param imageView
     */
    public static void bindImage(Context context, String path, ImageView imageView) {
        if (Build.VERSION.SDK_INT >= 21) {
            Glide.with(context)
                    .load(path)
                    .apply(new RequestOptions()
                    )
                    .into(imageView);
        } else {
            Glide.with(imageView).load(path)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        }
    }


    public static String getPathSection(String name) {
        return "file:///android_asset/sections/" + name + ".jpg";
    }

    public static String getPathWorkout(String name) {
        return "file:///android_asset/" + name + "/" + name + "_0.gif";
    }

    public static ArrayList<String> getArrayOfImagesFromAsset(Context context, String folderName) {
        AssetManager assetManager = context.getAssets();
        ArrayList<String> arrayList = new ArrayList<>();
        String[] imgPath;
        try {
            imgPath = assetManager.list(folderName);
            if (imgPath != null) {
                for (int i = 0; i < imgPath.length; i++) {
                    arrayList.add("file:///android_asset/" + folderName.replace("(", "_").replace(")", "_") + "/" + imgPath[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }

    public static int getHeightDevicePixel(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getWidthDevicePixel(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
