package com.sharparam.android.AndroidTest.components;

import android.graphics.*;

/**
 * Created by on 2014-02-21.
 *
 * @author Sharparam
 */
public class Wall extends RectangleComponent {
    private static final int DEFAULT_COLOR = Color.rgb(34, 139, 34);

    private boolean cleared;

    public Wall(float x, float y, float width, float height) {
        this(new RectF(x, y, x + width, y + height));
    }

    public Wall(RectF rect) {
        super(rect, new Paint(Paint.ANTI_ALIAS_FLAG));

        setColor(DEFAULT_COLOR);
        cleared = false;
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, style);
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
}
