//package com.dust.paxos;
//
//import static org.junit.Assert.*;
//
//import java.util.concurrent.CountDownLatch;
//
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class AcceptorTest {
//
//    private Acceptor acceptor;
//
//
//    @Before
//    public void setAcceptor() {
//        acceptor = Acceptor.create("001");
//        rnd = 1;
//    }
//
//    @Test
//    public void get() {
//        String key = "Ben";
//        ResMessage resMessage = acceptor.get(rnd, key);
//        assertEquals(resMessage.getLastRnd(), 0);
//        System.out.println(resMessage.toString());
//    }
//
//    @Test
//    public void set() {
//        String key = "Ben";
//        boolean ok = acceptor.set(key, "Jack", rnd);
//        assertEquals(ok, false);
//    }
//
//    @Test
//    public void OneTaskRun() {
//        String key = "Ben";
//        ResMessage res = acceptor.get(rnd, key);
//        assertEquals(res.getLastRnd(), 0);
//        boolean ok = acceptor.set(key, "AbAbAb", rnd);
//        assertTrue(ok);
//        res = acceptor.get(0, key);
//        assertEquals(res.getLastRnd(), 1);
//        res = acceptor.get(2, key);
//        assertEquals(res.getValue(), "AbAbAb");
//    }
//
//    @Test
//    public void TwoTaskRun() {
//        CountDownLatch latch = new CountDownLatch(1);
//        CountDownLatch latch1 = new CountDownLatch(2);
//        String key = "Ben";
//
//        Thread thread1 = new Thread(() -> {
//            try {
//                latch.await();
//                ResMessage res = acceptor.get(1, key);
//                assertEquals(res.getLastRnd(), 0);
//                boolean ok = acceptor.set(key, "value", 1);
//                // 此时这里可能成功，可能失败
//                assertFalse(ok);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                latch1.countDown();
//            }
//        });
//        Thread thread2 = new Thread(() -> {
//            try {
//                latch.await();
//                ResMessage res = acceptor.get(2, key);
//                assertEquals(res.getLastRnd(), 0);
//                boolean ok = acceptor.set(key, "value2", 2);
//                assertTrue(ok);
//                res = acceptor.get(2, key);
//                System.out.println(res.toString());
//                assertEquals(res.getValue(), "value2");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                latch1.countDown();
//            }
//        });
//
//        thread1.start();
//        thread2.start();
//
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        latch.countDown();
//
//        try {
//            latch1.await();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//}
