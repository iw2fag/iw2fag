package com.iw2fag.lab.algorithm.blockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 11/1/2017
 * Time: 10:40 AM
 */
public class ArrayBlockingQueue<E> {

    private Object[] array;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private int size;

    private int count;

    private int putIndex;
    private int getIndex;


    public ArrayBlockingQueue(int size) {
        this.size = size;
        array = new Object[size];
    }

    private void enqueue(E val) {
        Object[] array = this.array;
        array[putIndex] = val;
        if (++putIndex == size) {
            putIndex = 0;
        }
        count++;
        notEmpty.signal();
    }

    public void produce(E val) {
        try {
            lock.lockInterruptibly();
            while (array.length == count) {
                notFull.await();
            }
            enqueue(val);
            System.out.println("Produce:" + val);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    private void consume() {
        try {
            lock.lockInterruptibly();
            while (array.length == 0) {
                notEmpty.await();
            }
            E dequeue = dequeue();
            System.out.println("dequeue:" + dequeue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private E dequeue() {
        Object[] array = this.array;
        E getValue = (E) array[getIndex];
        array[getIndex] = 0;
        if (++getIndex == size) {
            getIndex = 0;
        }
        count--;
        notFull.signal();
        return getValue;
    }

}
