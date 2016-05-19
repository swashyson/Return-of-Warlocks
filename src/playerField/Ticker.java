package playerField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Swashy
 */
import java.util.HashSet;
import java.util.Set;

public class Ticker {
    private long lastTick;
    private long nextTick;
    private int tickRate;

    private Set<TickListener> tickListeners = new HashSet<>();

    public Ticker(int tickRate) {
        this.tickRate = tickRate;
    }

    public void addTickListener(TickListener listener) {
        tickListeners.add(listener);
    }

    public void removeTickListener(TickListener listener) {
        tickListeners.remove(listener);
    }

    public void setTickRate(int tickRate) {
        this.tickRate = tickRate;
    }

    public int getTickRate() {
        return tickRate;
    }

    public void reset() {
        lastTick = 0;
        nextTick = 0;
    }

    public boolean update() {
        long currentTime = System.currentTimeMillis();

        if (currentTime >= nextTick) {
            long targetTimeDelta = 1000L / tickRate;

            if (lastTick == 0 || nextTick == 0) {
                lastTick = currentTime - targetTimeDelta;
                nextTick = currentTime;
            }

            float deltaTime = (float) (currentTime - lastTick) / targetTimeDelta;

            for (TickListener listener : tickListeners) {
                listener.onTick(deltaTime);
            }

            lastTick = currentTime;
            nextTick = currentTime + targetTimeDelta;

            return true;
        }

        return false;
    }
}
