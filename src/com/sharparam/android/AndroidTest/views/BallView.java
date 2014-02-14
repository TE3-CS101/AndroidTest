package com.sharparam.android.AndroidTest.views;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sharparam.android.AndroidTest.RefreshHandler;
import com.sharparam.android.AndroidTest.drawables.Ball;

import java.util.Random;

/**
 * Created by on 2014-02-13.
 *
 * @author Sharparam
 */
public class BallView extends View implements View.OnTouchListener {
    private static final Random RNG = new Random();
    private static final long UPDATE_DELAY = 10;

    private Paint textStyle;

    private long lastUpdate;

    private Ball ball;

    private RefreshHandler redrawHandler;

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        redrawHandler = new RefreshHandler(this);

        Typeface textType = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

        textStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
        textStyle.setColor(Color.WHITE);
        textStyle.setTypeface(textType);
        textStyle.setTextSize(28.0f);

        ball = new Ball(100.0f, 0.0f, 20.0f);
        ball.setColor(Color.RED);

        setOnTouchListener(this);

        onUpdate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ball.setCanvasWidth(w);
        ball.setCanvasHeight(h);
    }

    public void onUpdate() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;

        if (elapsed >= UPDATE_DELAY) {
            ball.update(elapsed);
            lastUpdate = now;
        }

        redrawHandler.sleep(UPDATE_DELAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        canvas.drawText(ball.toString(), 10.0f, 25.0f, textStyle);
        ball.draw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ball.onTouch(v, event);
        return true;
    }
}
