
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(testPattern(EagerInitilization::getInstance));
        System.out.println(testPattern(LazyInitialization::getInstance));
        System.out.println(testPattern(SynchronizedMethod::getInstance));
        System.out.println(testPattern(DoubleCheckedLocking::getInstance));
        System.out.println(testPattern(StaticHolder::getInstance));
        System.out.println(testPattern(()->EnumSingleton.INSTANCE));
    }


    public static int testPattern(Supplier<?> getter) throws InterruptedException, ExecutionException{
        int threadCount = 100;

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);

        Set<Object> instances = ConcurrentHashMap.newKeySet();
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(pool.submit(() -> {
                ready.countDown();          // signal: this thread is ready
                try {
                    start.await();          // wait for the starting gun
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                instances.add(getter.get());
            }));
        }

        ready.await();      // all threads are waiting at the gate
        start.countDown();  // release them all at once

        for (Future<?> f : futures) f.get();
        pool.shutdown();

        return instances.size();
    }
}
