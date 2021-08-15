package timer;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class GameLoopTimer extends AnimationTimer {

    long pause_start;
    long animation_start;
    DoubleProperty animation_duration = new SimpleDoubleProperty(0L);

    long lastFrameTimeNanos;

    boolean is_paused;
    boolean is_active;

    boolean pauseScheduled;
    boolean playScheduled;
    boolean restartScheduled;

    public boolean isPaused() {
        return is_paused;
    }

    public boolean isActive() {
        return is_active;
    }

    public DoubleProperty animationDurationProperty() {
        return animation_duration;
    }

    public void pause() {
        if (!is_paused) {
            pauseScheduled = true;
        }
    }

    public void play() {
        if (is_paused) {
            playScheduled = true;
        }
    }

    @Override
    public void start() {
        super.start();
        is_active = true;
        restartScheduled = true;
    }

    @Override
    public void stop() {
        super.stop();
        pause_start = 0;
        is_paused = false;
        is_active = false;
        pauseScheduled = false;
        playScheduled = false;
        animation_duration.set(0);
    }

    @Override
    public void handle(long now) {
        if (pauseScheduled) {
            pause_start = now;
            is_paused = true;
            pauseScheduled = false;
        }

        if (playScheduled) {
            animation_start += (now - pause_start);
            is_paused = false;
            playScheduled = false;
        }

        if (restartScheduled) {
        	is_paused = false;
            animation_start = now;
            restartScheduled = false;
        }

        if (!is_paused) {
            long animDuration = now - animation_start;
            animation_duration.set(animDuration / 1e9);

            float secondsSinceLastFrame = (float) ((now - lastFrameTimeNanos) / 1e9);
            lastFrameTimeNanos = now;
            tick(secondsSinceLastFrame);
        }
    }

    public abstract void tick(float secondsSinceLastFrame);
}