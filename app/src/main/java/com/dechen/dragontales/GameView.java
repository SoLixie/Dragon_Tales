package com.dechen.dragontales;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private boolean isGameOver;
    private List<Point> dragon;
    private Paint paint;
    private Bitmap backgroundBitmap;
    private Bitmap dragonBitmap;
    private Bitmap foodBitmap;
    private int screenWidth;
    private int screenHeight;
    private final int cellSize = 100; // Increased cell size for dragon and food
    private Point foodPosition;
    private int direction;
    private int score;

    private float initialX, initialY;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int eatSound;
    private int gameOverSound;

    // Listener for score changes
    public interface OnScoreChangeListener {
        void onScoreChanged(int newScore);
    }

    private OnScoreChangeListener scoreChangeListener;

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.scoreChangeListener = listener;
    }

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Get screen size
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Load and scale bitmaps from resources
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenWidth, screenHeight, false);

        dragonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mc);
        dragonBitmap = Bitmap.createScaledBitmap(dragonBitmap, cellSize, cellSize, false);

        foodBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food);
        foodBitmap = Bitmap.createScaledBitmap(foodBitmap, cellSize, cellSize, false);

        // Initialize dragon starting position
        dragon = new ArrayList<>();
        dragon.add(new Point(screenWidth / 2, screenHeight / 2)); // Start in the center

        spawnFood();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);

        direction = 3; // Start moving right
        score = 0;
        isGameOver = false;

        mediaPlayer = MediaPlayer.create(context, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();

        eatSound = soundPool.load(context, R.raw.eat, 1);
        gameOverSound = soundPool.load(context, R.raw.die, 1);
    }

    private void spawnFood() {
        foodPosition = new Point(
                (int) (Math.random() * (screenWidth / cellSize)) * cellSize,
                (int) (Math.random() * (screenHeight / cellSize)) * cellSize
        );
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            drawGame();
            sleep();
        }
    }

    private void update() {
        if (isGameOver) return;

        if (!dragon.isEmpty()) {
            Point head = dragon.get(0);
            Point newHead = new Point(head.x, head.y);

            // Move the dragon's head
            switch (direction) {
                case 0: newHead.y -= cellSize; break; // Up
                case 1: newHead.y += cellSize; break; // Down
                case 2: newHead.x -= cellSize; break; // Left
                case 3: newHead.x += cellSize; break; // Right
            }

            // Check boundaries
            if (newHead.x < 0 || newHead.x >= screenWidth || newHead.y < 0 || newHead.y >= screenHeight) {
                gameOver();
                return;
            }

            // Check for self-collision
            if (dragon.contains(newHead)) {
                gameOver();
                return;
            }

            // Add the new head to the dragon
            dragon.add(0, newHead);

            // Check if food is eaten
            if (Math.abs(newHead.x - foodPosition.x) < cellSize && Math.abs(newHead.y - foodPosition.y) < cellSize) {
                score++;
                spawnFood();
                soundPool.play(eatSound, 1, 1, 0, 0, 1); // Play eat sound
                updateScore(score); // Notify the score listener
            } else {
                dragon.remove(dragon.size() - 1); // Remove last segment if not eaten
            }
        }
    }

    private void gameOver() {
        isGameOver = true;
        mediaPlayer.pause();
        soundPool.play(gameOverSound, 1, 1, 0, 0, 1);
    }

    private void drawGame() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);

            // Draw the dragon
            for (Point point : dragon) {
                canvas.drawBitmap(dragonBitmap, point.x, point.y, null);
            }

            // Draw the food
            canvas.drawBitmap(foodBitmap, foodPosition.x, foodPosition.y, null);

            // Draw the score
            canvas.drawText("Score: " + score, 10, 50, paint);

            if (isGameOver) {
                paint.setTextSize(100);
                paint.setColor(Color.RED);
                canvas.drawText("GAME OVER", screenWidth / 4, screenHeight / 2, paint);
                paint.setTextSize(50);
                canvas.drawText("Score: " + score, screenWidth / 3, screenHeight / 2 + 70, paint);
                canvas.drawText("Tap to Restart", screenWidth / 5, screenHeight / 2 + 140, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(100); // Faster game speed for smoothness
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        isPlaying = true;
        isGameOver = false;
        direction = 3;
        score = 0;
        dragon.clear();
        dragon.add(new Point(screenWidth / 2, screenHeight / 2));
        spawnFood();
        mediaPlayer.start();
        thread = new Thread(this);
        thread.start();
    }

    public void stopGame() {
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        soundPool.release();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Notify the listener when the score changes
    private void updateScore(int newScore) {
        if (scoreChangeListener != null) {
            scoreChangeListener.onScoreChanged(newScore);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                startGame();
            } else {
                initialX = event.getX();
                initialY = event.getY();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float finalX = event.getX();
            float finalY = event.getY();
            float deltaX = finalX - initialX;
            float deltaY = finalY - initialY;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0 && direction != 2) direction = 3; // Right
                else if (deltaX < 0 && direction != 3) direction = 2; // Left
            } else {
                if (deltaY > 0 && direction != 0) direction = 1; // Down
                else if (deltaY < 0 && direction != 1) direction = 0; // Up
            }
        }
        return true;
    }
}
