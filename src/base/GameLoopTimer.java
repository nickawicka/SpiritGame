package base;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class GameLoopTimer extends AnimationTimer {

    long pause_start;
    long animation_start;
    DoubleProperty animation_duration = new SimpleDoubleProperty(0L);

    long last_frame_time_nanos;

    boolean is_paused;
    boolean is_active;

    boolean pause_scheduled;
    boolean play_scheduled;
    boolean restart_scheduled;

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
            pause_scheduled = true;
        }
    }

    public void play() {
        if (is_paused) {
            play_scheduled = true;
        }
    }

    @Override
    public void start() {
        super.start();
        is_active = true;
        restart_scheduled = true;
    }

    @Override
    public void stop() {
        super.stop();
        pause_start = 0;
        is_paused = false;
        is_active = false;
        pause_scheduled = false;
        play_scheduled = false;
        animation_duration.set(0);
    }

    @Override
    public void handle(long now) {
        if (pause_scheduled) {
            pause_start = now;
            is_paused = true;
            pause_scheduled = false;
        }

        if (play_scheduled) {
            animation_start += (now - pause_start);
            is_paused = false;
            play_scheduled = false;
        }

        if (restart_scheduled) {
        	is_paused = false;
            animation_start = now;
            restart_scheduled = false;
        }

        if (!is_paused) {
            long anim_dur = now - animation_start;
            animation_duration.set(anim_dur / 1e9);

            float seconds_since_last_frame = (float) ((now - last_frame_time_nanos) / 1e9);
            last_frame_time_nanos = now;
            tick(seconds_since_last_frame);
        }
    }

    public abstract void tick(float seconds_since_last_frame);
}