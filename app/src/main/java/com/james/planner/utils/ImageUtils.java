package com.james.planner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtils {

    public static Drawable getVectorDrawable(Context context, int resId) {
        VectorDrawableCompat drawable;
        try {
            drawable = VectorDrawableCompat.create(context.getResources(), resId, context.getTheme());
        } catch (Exception e) {
            e.printStackTrace();
            return new ColorDrawable(Color.TRANSPARENT);
        }

        if (drawable != null) {
            return drawable.getCurrent();
        } else {
            Log.wtf(context.getClass().getName(), "Can't get a vector drawable.");
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) drawable = new ColorDrawable(Color.TRANSPARENT);
        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
        if (drawable instanceof VectorDrawableCompat)
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap blurBitmap(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAlpha(180);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        int blurRadius = Math.max(bitmap.getWidth(), bitmap.getHeight()) / 10;
        for (int row = -blurRadius; row < blurRadius; row += 2) {
            for (int column = -blurRadius; column < blurRadius; column += 2) {
                if (column * column + row * row <= blurRadius * blurRadius) {
                    paint.setAlpha((blurRadius * blurRadius) / ((column * column + row * row) + 1) * 2);
                    canvas.drawBitmap(bitmap, row, column, paint);
                }
            }
        }

        return resultBitmap;
    }

    public static Drawable tintDrawable(Drawable source, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(source, color);
        } else source.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        return source;
    }

    public static Drawable tintDrawable(ImageView imageView, Drawable source, @ColorInt int color) {
        imageView.setImageDrawable(DrawableCompat.wrap(source));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(source, color);
        } else source.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        imageView.invalidate();

        return source;
    }

    public static Bitmap tintBitmap(Bitmap source, @ColorInt int color) {
        Bitmap bitmap = Bitmap.createBitmap(source);

        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        new Canvas(bitmap).drawBitmap(source, new Matrix(), paint);

        return bitmap;
    }
}
