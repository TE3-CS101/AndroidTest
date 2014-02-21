package com.sharparam.android.AndroidTest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sharparam.android.AndroidTest.components.Cube;
import com.sharparam.android.AndroidTest.components.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by on 2014-02-21.
 *
 * @author Sharparam
 */
public class FlappyCubeView extends View implements View.OnTouchListener {
    private static final Random RNG = new Random();
    private static final long UPDATE_DELAY = 10;
    private static final float WALL_WIDTH = 100.0f;
    private static final long WALL_DELAY = 5000;

    private static final int GAME_STATE_START = 0;
    private static final int GAME_STATE_RUNNING = 1;
    private static final int GAME_STATE_GAMEOVER = 2;

    private static final Paint debugTextStyle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Paint bigTextStyle = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int backgroundColor;

    private int canvasWidth;
    private int canvasHeight;

    private long lastUpdate;
    private long elapsed;

    private long wallTimer;

    private ArrayList<Wall> walls;

    private Cube cube;

    private UpdateHandler updateHandler = new UpdateHandler();

    private int gameState;

    private int points;

    class UpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            FlappyCubeView.this.onUpdate();
            FlappyCubeView.this.invalidate();
        }

        public void sleep(long ms) {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), ms);
        }
    }

    public FlappyCubeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundColor = Color.rgb(100, 149, 237);

        debugTextStyle.setColor(Color.WHITE);
        debugTextStyle.setTextSize(26.0f);

        bigTextStyle.setColor(Color.WHITE);
        bigTextStyle.setTextSize(50.0f);

        walls = new ArrayList<Wall>();

        setOnTouchListener(this);

        gameState = GAME_STATE_START;

        lastUpdate = System.currentTimeMillis();
        onUpdate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasWidth = w;
        canvasHeight = h;

        initCube();
        generateWall();
    }

    private void initCube() {
        cube = new Cube(canvasWidth / 4, canvasHeight / 2);
    }

    private void generateWall() {
        Wall top = new Wall(canvasWidth, 0, WALL_WIDTH, (canvasHeight - 500) * RNG.nextFloat() + 200.0f);
        Wall bottom = new Wall(new RectF(canvasWidth, top.getRect().bottom + 250.0f, canvasWidth + WALL_WIDTH, canvasHeight));

        walls.add(top);
        walls.add(bottom);
    }

    protected void onUpdate() {
        long now = System.currentTimeMillis();
        long delay = now - lastUpdate;
        elapsed += delay;

        if (elapsed >= UPDATE_DELAY) {
            switch (gameState) {
                case GAME_STATE_RUNNING:
                    runningUpdate(elapsed);
                    break;
            }

            elapsed -= UPDATE_DELAY;
        }

        lastUpdate = now;

        updateHandler.sleep(0);
    }

    private void runningUpdate(long elapsed) {
        wallTimer += elapsed;

        if (wallTimer >= WALL_DELAY) {
            generateWall();
            wallTimer -= WALL_DELAY;
        }

        for (int i = 0; i < walls.size(); i++) {
            Wall wall = walls.get(i);
            wall.addX(-2.0f);

            if (wall.getRect().right < 0)
                walls.set(i, null);
            else {
                if (cube != null) {
                    if (RectF.intersects(cube.getRect(), wall.getRect()))
                        gameState = GAME_STATE_GAMEOVER;
                    else if (!wall.getCleared() && wall.getRect().top > 0 && wall.getRect().right < cube.getRect().left) {
                        points++;
                        wall.setCleared(true);
                    }
                }
            }
        }

        walls.removeAll(Collections.singleton(null));

        if (cube == null || gameState != GAME_STATE_RUNNING)
            return;

        cube.update(elapsed);

        RectF rect = cube.getRect();

        if (rect.top < 0) {
            cube.setY(0);
            cube.setYSpeed(-cube.getYSpeed());
            //gameState = GAME_STATE_GAMEOVER;
        } else if (rect.bottom > canvasHeight) {
            cube.setY(canvasHeight - cube.getHeight());
            gameState = GAME_STATE_GAMEOVER;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        String debug = String.format("Elapsed: %d; Wall delay: %d; Walls: %d", elapsed, wallTimer, walls.size());

        for (Wall wall : walls)
            wall.draw(canvas);

        if (cube != null)
            cube.draw(canvas);

        canvas.drawText(Integer.toString(points), 20.0f, 50.0f, bigTextStyle);

        canvas.drawText(debug, 20.0f, 150.0f, debugTextStyle);

        switch (gameState) {
            case GAME_STATE_START:
                canvas.drawText("PRESS TO START", canvasWidth / 2 - 200.0f, canvasHeight / 2, bigTextStyle);
                break;
            case GAME_STATE_GAMEOVER:
                canvas.drawText("GAME OVER", canvasWidth / 2 - 200.0f, canvasHeight / 2, bigTextStyle);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
            return false;

        switch (gameState) {
            case GAME_STATE_START:
                initCube();
                walls.clear();
                gameState = GAME_STATE_RUNNING;
                points = 0;
            case GAME_STATE_RUNNING:
                if (cube != null)
                    cube.setYSpeed(-8.0f);
                break;
            case GAME_STATE_GAMEOVER:
                initCube();
                walls.clear();
                points = 0;
                gameState = GAME_STATE_START;
                break;
        }

        return true;
    }
}
