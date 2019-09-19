package com.noname.demo;

import lombok.*;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by noname on 2019/8/13.
 *
 * 当所有任务都调用了同一个CyclicBarrier对象的await方法，开始阻塞，直到达到它指定的count，才会放行
 * 形象的比喻：该类像是一个屏障，当所有的线程都到达屏障后屏障才会打开，否则所有的任务都一直被阻塞
 *
 * CyclicBarrier一般来说是针对的多个线程阻塞，然后一起放行
 */
public class Demo23 {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Runner implements Runnable {
        private CyclicBarrier barrier;
        private String name;

        @Override
        public void run() {
            try {
                Thread.sleep(1000 * new Random().nextInt(5));
                System.out.println(name + " 准备ok");
                //在CyclicBarrier的计数未到达指定计数前，都将阻塞，直到所有线程都到达await方法，则全部放行
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println(name + " GO!");
        }
    }

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);      //指定计数，与CountDownLatch类似
        executorService.submit(new Runner(barrier, "noname"));
        executorService.submit(new Runner(barrier, "binbin"));
        executorService.submit(new Runner(barrier, "pingtou"));

        executorService.shutdown();
    }
}
