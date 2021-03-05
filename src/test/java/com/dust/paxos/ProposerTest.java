package com.dust.paxos;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import com.dust.paxos.component.Acceptor;
import com.dust.paxos.component.Proposer;
import org.junit.Before;
import org.junit.Test;

public class ProposerTest {

    private Proposer p1;
    private Proposer p2;

    private Acceptor a1;
    private Acceptor a2;
    private Acceptor a3;

    @Before
    public void init() {
        a1 = Acceptor.create("003");
        a2 = Acceptor.create("004");
        a3 = Acceptor.create("005");

        p1 = Proposer.create("001");
        p2 = Proposer.create("002");

        p1.appendAcceptor(a1).appendAcceptor(a2).appendAcceptor(a3);
        p2.appendAcceptor(a1).appendAcceptor(a2).appendAcceptor(a3);
    }

    @Test
    public void concurrentTest() {
        CountDownLatch threadLatch = new CountDownLatch(2);
        CountDownLatch proposerLatch = new CountDownLatch(1);
        String key = "name";

        Thread t1 = new Thread(() -> {
            try {
                proposerLatch.await();
                boolean ok = p1.set(key, "Bob");
                assertTrue(ok);
                String name = p1.get(key);
                System.out.println(name);
                assertEquals(name, "Bob");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                threadLatch.countDown();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                proposerLatch.await();
                boolean ok = p2.set(key, "Bob2");
                assertTrue(ok);
                String name = p2.get(key);
                System.out.println(name);
                assertEquals(name, "Bob2");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                threadLatch.countDown();
            }
        });

        t1.start();
        t2.start();

        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
        proposerLatch.countDown();
        try {
            threadLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a1.toString());
        System.out.println(a2.toString());
        System.out.println(a3.toString());
    }

}
