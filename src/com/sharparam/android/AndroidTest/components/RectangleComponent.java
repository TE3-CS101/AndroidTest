package com.sharparam.android.AndroidTest.components;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by on 2014-02-21.
 *
 * @author Sharparam
 */
public abstract class RectangleComponent extends GameComponent {
    protected RectF rect;

    protected RectangleComponent(float x, float y, float width, float height) {
        this(x, y, width, height, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    protected RectangleComponent(float x, float y, float width, float height, Paint style) {
        this(new RectF(x, y, x + width, y + height), style);
    }

    protected RectangleComponent(RectF rect) {
        this(rect, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    protected RectangleComponent(RectF rect, Paint style) {
        super(style);

        this.rect = rect;
    }

    public RectF getRect() {
        return rect;
    }

    public void addX(float diff) {
        rect.left += diff;
        rect.right += diff;
    }

    public void setX(float x) {
        float diff = x - rect.left;
        addX(diff);
    }

    public void addY(float diff) {
        rect.top += diff;
        rect.bottom += diff;
    }

    public void setY(float y) {
        float diff = y - rect.top;
        addY(diff);
    }
}
