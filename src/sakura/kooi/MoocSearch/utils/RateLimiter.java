package sakura.kooi.MoocSearch.utils;

import java.util.concurrent.atomic.AtomicLong;

public class RateLimiter {
    private AtomicLong delay = new AtomicLong(-1L);
    public void limit(long time) {
        while (System.currentTimeMillis() < delay.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) { }
        }
        delay.set(System.currentTimeMillis() + time);
    }
}
