package com.sharparam.android.AndroidTest.components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.sharparam.android.AndroidTest.interfaces.Updatable;

/**
 * Created by on 2014-02-21.
 *
 * @author Sharparam
 */
public abstract class GameComponent extends Drawable implements Updatable {
    protected Paint style;

    protected GameComponent() {
        this(new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    protected GameComponent(Paint style) {
        this.style = style;
    }

    @Override
    public abstract void update(long elapsed);

    @Override
    public abstract void draw(Canvas canvas);

    public void setColor(int color) {
        style.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        style.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        style.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return style.getAlpha();
    }

    public Paint getStyle() {
        return style;
    }

    public void setStyle(Paint style) {
        this.style = style;
    }
}
