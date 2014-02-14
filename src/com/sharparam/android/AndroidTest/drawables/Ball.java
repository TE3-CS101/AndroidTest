package com.sharparam.android.AndroidTest.drawables;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by on 2014-02-14.
 *
 * @author Sharparam
 */
public class Ball extends Drawable implements View.OnTouchListener {
    private static final float GRAVITY = 9.82f;
    private static final float GRAVITY_MOD = 0.0982f;

    private final Random rng;
    private float x;
    private float y;
    private float radius;
    private Paint style;

    private float dx;
    private float dy;

    private float xSpeed;
    private float ySpeed;

    private float canvasWidth;
    private float canvasHeight;

    public Ball(float x, float y, float radius) {
        this(x, y, radius, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    public Ball(float x, float y, float radius, Paint style) {
        this(x, y, radius, style, new Random());
    }

    public Ball(float x, float y, float radius, Random rng) {
        this(x, y, radius, new Paint(Paint.ANTI_ALIAS_FLAG), rng);
    }

    public Ball(float x, float y, float radius, Paint style, Random rng) {
        this.rng = rng;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.style = style;

        dx = rng.nextFloat() * 10;
        setySpeed(GRAVITY);
    }

    private float getVerticalSpeed(float ySpeed) {
        float result = ySpeed + GRAVITY_MOD;
        return result > GRAVITY  ? GRAVITY : result;
    }

    private float clamp(float val, float min, float max) {
        if (val < min)
            return min;
        else if (val > max)
            return max;
        return val;
    }

    public void update(long elapsed) {
        ySpeed = getVerticalSpeed(ySpeed);

        float newX = x + xSpeed;
        float newY = y + ySpeed;

        if ((newX - radius) < 0 || (newX + radius) > canvasWidth) {
            // Wall hit
            setxSpeed(-xSpeed * 0.75f);
            newX = clamp(newX, radius, canvasWidth - radius);
        }

        if ((newY - radius) < 0) {
            setySpeed(-ySpeed);
            newY = radius;
        } else if ((newY + radius) > canvasHeight) {
            setxSpeed(xSpeed * 0.9f);
            if (ySpeed <= 0) {
                newY = canvasHeight - radius;
                setxSpeed(0.0f);
            } else {
                setySpeed(-ySpeed * 0.85f);
                newY -= ((newY + radius) - canvasHeight);
            }
        }

        x = newX;
        y = newY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, style);
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
        return 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getStyle() {
        return style;
    }

    public void setColor(int color) {
        style.setColor(color);
    }

    @Override
    public String toString() {
        return String.format("[Ball: [%.2f, %.2f]]", x, y);
    }

    public void setCanvasWidth(float canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public void setCanvasHeight(float canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() != MotionEvent.ACTION_UP)
            return false;

        float pressX = event.getX();
        final float threshold = canvasWidth / 3;

        if (pressX < threshold)
            setxSpeed(xSpeed + 10);
        else if (pressX > threshold * 2)
            setxSpeed(xSpeed - 10);
        else
            setySpeed(ySpeed - 10);

        return true;
    }
}
