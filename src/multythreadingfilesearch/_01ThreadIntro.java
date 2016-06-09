package multythreadingfilesearch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by r.popov on 6/9/2016.
 */
public class _01ThreadIntro { private static final Logger LOG = Logger.getLogger(_01ThreadIntro.class.getName());

    public static void main(String[] args) {

        LOG.setLevel(Level.INFO);
        LOG.addHandler(new ConsoleHandler());

        IContainer container = new Container();

        long start = System.currentTimeMillis();


        ExecutorService service = Executors.newFixedThreadPool(20);

        int operationCount = 1000000;

        for (int i = 0; i < 2; i++) {
            Future future = service.submit(new IncThread(container,operationCount));
            service.submit(new DecThread(container,operationCount));
        }

        service.shutdown();
        while(!service.isTerminated()){
            try {
                service.awaitTermination(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        long end = System.currentTimeMillis();

        System.out.println("time");
        System.out.println(end - start);
        System.out.println("count");
        System.out.println(container.getCount());
    }

}


class IncThread implements Runnable {

    private IContainer container;
    private int operationCount;


    public IncThread(IContainer container, int operationCount) {
        this.container = container;
        this.operationCount = operationCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < operationCount; i++) {
            container.inc();
        }
    }
}

class DecThread implements Runnable {

    private IContainer container;
    private int operationCount;


    public DecThread(IContainer container, int operationCount) {
        this.container = container;
        this.operationCount = operationCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < operationCount; i++) {
            container.decr();
        }
    }
}


interface IContainer {
    void inc();
    void decr();

    int getCount();
}

// TODO show logging
class Container implements IContainer {

    private volatile int count;

    private Lock lock = new ReentrantLock(true);
    private Condition producerCondition = lock.newCondition();
    private Condition consumerCondition = lock.newCondition();

    // sync(){sync(){sycn}}
    public void inc() {// monitor this

        try {

            lock.lock();
            System.out.printf("inc %d thread id %s, takes monitor\n", count, Thread.currentThread().getId());

            while (count != 0) {
                try {
                    System.out.printf("inc %d thread id %s, wait\n", count, Thread.currentThread().getId());
                    producerCondition.await();// release monitor
                    System.out.printf("inc %d thread id %s, awake\n", count, Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            count++;
            consumerCondition.signal();
            System.out.printf("inc %d thread id %s, releas monitor\n", count, Thread.currentThread().getId());

        } finally {
            lock.unlock();
        }

    }

    public void decr() {
        try {
            lock.lock();
            System.out.printf("decr %d thread id %s, takes monitor\n", count,Thread.currentThread().getId());

            while(count != 1){
                try {
                    System.out.printf("decr %d thread id %s, wait\n", count,Thread.currentThread().getId());
                    consumerCondition.await();
                    System.out.printf("decr %d thread id %s, awake\n", count,Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            count--;
            producerCondition.signal();
            System.out.printf("decr %d thread id %s, release monitor\n", count, Thread.currentThread().getId());
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }

}


