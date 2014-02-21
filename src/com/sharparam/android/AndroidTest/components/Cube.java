package com.sharparam.android.AndroidTest.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by on 2014-02-21.
 *
 * @author Sharparam
 */
public class Cube extends RectangleComponent {
    private static final float DEFAULT_SIZE = 50.0f;
    private static final float HALF_SIZE = DEFAULT_SIZE / 2;
    private static final float GRAVITY_CONSTANT = 9.82f;
    private static final float GRAVITY_MODIFIER = 0.25f;

    private final float width;
    private final float height;

    private float ySpeed;

    public Cube(float x, float y) {
        super(new RectF(x - HALF_SIZE, y - HALF_SIZE, x + HALF_SIZE, y + HALF_SIZE), new Paint(Paint.ANTI_ALIAS_FLAG));
        setColor(Color.RED);

        width = rect.right - rect.left;
        height = rect.bottom - rect.top;
    }

    @Override
    public void update(long elapsed) {
        ySpeed += GRAVITY_MODIFIER;
        if (ySpeed > GRAVITY_CONSTANT)
            ySpeed = GRAVITY_CONSTANT;

        addY(ySpeed);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, style);
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(float speed) {
        ySpeed = speed;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
