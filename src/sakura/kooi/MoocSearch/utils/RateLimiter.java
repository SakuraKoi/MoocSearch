package sakura.kooi.MoocSearch.utils;

public class RateLimiter {
    private long delay = -1L;
    public synchronized void limit(long time) {
        while (System.currentTimeMillis() < delay) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) { }
        }
        delay = System.currentTimeMillis() + time;
    }
}
