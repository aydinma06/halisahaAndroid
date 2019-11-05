package halisahaandroid.mammy.halisahaandroid.utils;



import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import halisahaandroid.mammy.halisahaandroid.R;

public class ColorUtils {

    public static int getViewHolderBackgroundColorFromInstance(Context context, int instanceNum) {
        switch (instanceNum) {
            case 0:
                return ContextCompat.getColor(context, R.color.white);
            case 1:
                return ContextCompat.getColor(context, R.color.white);//material200Green
            case 2:
                return ContextCompat.getColor(context, R.color.white);//material200Green
            case 3:
                return ContextCompat.getColor(context, R.color.white);//material200Green
            case 4:
                return ContextCompat.getColor(context, R.color.white);//material250Green
            case 5:
                return ContextCompat.getColor(context, R.color.white);//material300Green
            case 6:
                return ContextCompat.getColor(context, R.color.white);//material350Green
            case 7:
                return ContextCompat.getColor(context, R.color.white);//material400Green
            case 8:
                return ContextCompat.getColor(context, R.color.white);//material450Green
            case 9:
                return ContextCompat.getColor(context, R.color.white);//material500Green
            case 10:
                return ContextCompat.getColor(context, R.color.white);//material550Green
            case 11:
                return ContextCompat.getColor(context, R.color.white);//material600Green
            case 12:
                return ContextCompat.getColor(context, R.color.white);
            case 13:
                return ContextCompat.getColor(context, R.color.white);
            case 14:
                return ContextCompat.getColor(context, R.color.white);
            case 15:
                return ContextCompat.getColor(context, R.color.limegreen);
            case 16:
                return ContextCompat.getColor(context, R.color.tomato);
            case 17:
                return ContextCompat.getColor(context, R.color.lightgoldenrodyellow);//material600Green

            default:
                return Color.WHITE;
        }
    }
}
