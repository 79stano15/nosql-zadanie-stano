package sk.upjs.nosql;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelIncrement {
    public static void main(String[] args) {
        RedisConnectionFactory factory = RedisFactory.INSTANCE.redisConnectionFactory();
        RedisAtomicInteger counter1 = new RedisAtomicInteger("counter", factory);
        RedisAtomicInteger counter2 = new RedisAtomicInteger("counter", factory);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new IncrementTask(counter1, "workerA"));
        executorService.submit(new IncrementTask(counter2, "workerB"));
    }

    private static class IncrementTask implements Runnable {
        private final RedisAtomicInteger counter;
        private final String name;
        public IncrementTask(RedisAtomicInteger counter, String name) {
            this.counter = counter;
            this.name = name;
        }
        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                System.out.println(name + ": " + counter.incrementAndGet());
                try {
                    Thread.sleep(100 + random.nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
