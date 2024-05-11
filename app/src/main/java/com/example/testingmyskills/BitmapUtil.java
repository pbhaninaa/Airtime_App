package com.example.testingmyskills;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {

    public static Drawable resizeBitmapAndApplyShape(Context context, Bitmap bitmap, int width, int height, int cornerRadius) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), resizedBitmap);
        BitmapShader shader = new BitmapShader(resizedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        return new BitmapDrawable(context.getResources(), output);
    }
}
