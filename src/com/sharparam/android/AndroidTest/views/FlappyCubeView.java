package com.sharparam.android.AndroidTest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sharparam.android.AndroidTest.R;
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
    private static final boolean DEBUG = true;

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

    private SoundPool sounds;
    private int selectSound;
    private int jumpSound;
    private int coinSound;
    private int hurtSound;

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

        sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        selectSound = sounds.load(context, R.raw.select, 1);
        jumpSound = sounds.load(context, R.raw.jump, 1);
        coinSound = sounds.load(context, R.raw.coin, 1);
        hurtSound = sounds.load(context, R.raw.hurt, 1);

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
                    if (RectF.intersects(cube.getRect(), wall.getRect())) {
                        gameState = GAME_STATE_GAMEOVER;
                        playHurt();
                    } else if (!wall.getCleared() && wall.getRect().top > 0 && wall.getRect().right < cube.getRect().left) {
                        points++;
                        wall.setCleared(true);
                        playCoin();
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
        } else if (rect.bottom > canvasHeight) {
            cube.setY(canvasHeight - cube.getHeight());
            gameState = GAME_STATE_GAMEOVER;
            playHurt();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        for (Wall wall : walls)
            wall.draw(canvas);

        if (cube != null)
            cube.draw(canvas);

        canvas.drawText(Integer.toString(points), 20.0f, 50.0f, bigTextStyle);

        if (DEBUG) {
            String debug = String.format("Elapsed: %d; Wall delay: %d; Walls: %d", elapsed, wallTimer, walls.size());
            canvas.drawText(debug, 20.0f, 150.0f, debugTextStyle);
        }

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
                playSelect();
            case GAME_STATE_RUNNING:
                if (cube != null) {
                    cube.setYSpeed(-8.0f);
                    playJump();
                }
                break;
            case GAME_STATE_GAMEOVER:
                initCube();
                walls.clear();
                points = 0;
                gameState = GAME_STATE_START;
                playSelect();
                break;
        }

        return true;
    }

    private void playSound(int id) {
        sounds.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    private void playSelect() {
        playSound(selectSound);
    }

    private void playJump() {
        playSound(jumpSound);
    }

    private void playCoin() {
        playSound(coinSound);
    }

    private void playHurt() {
        playSound(hurtSound);
    }
}
