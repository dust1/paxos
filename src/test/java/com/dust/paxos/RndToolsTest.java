package com.dust.paxos;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

import com.dust.paxos.utils.RndTools;

import org.junit.Test;

public class RndToolsTest {

    @Test
    public void getRndTest() {
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch1 = new CountDownLatch(2);
        Set<Integer> set = new ConcurrentSkipListSet<>();
        Thread thread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            boolean ok = false;
            for (int i = 0; i < 100000; i++) {
                int rnd = RndTools.getRnd();
                ok = set.add(rnd);
                assertTrue(ok);
            }
            latch1.countDown();
        });
        Thread thread1 = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            boolean ok = false;
            for (int i = 0; i < 100000; i++) {
                int rnd = RndTools.getRnd();
                ok = set.add(rnd);
                assertTrue(ok);
            }
            latch1.countDown();
        });
        thread.start();
        thread1.start();

        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        latch.countDown();

        try {
            latch1.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(set.size());
    }

}
