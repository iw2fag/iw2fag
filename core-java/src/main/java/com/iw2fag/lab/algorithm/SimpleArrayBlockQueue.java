package com.iw2fag.lab.algorithm;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/8/2017
 * Time: 5:04 PM
 */
class SimpleArrayBlockingQueue<E> {

    private Object[] arr;
    private static final int DEFAULT_SIZE = 16;
    private int size;
    private int putIndex;
    private int getIndex;
    private volatile int realSize;

    public SimpleArrayBlockingQueue() {
        this(DEFAULT_SIZE);
    }

    public SimpleArrayBlockingQueue(int size) {
        this.size = size;
        arr = new Object[size];
    }

    public void produce(E i) {

        synchronized (arr) {
            if (realSize == size) {
                try {
                    arr.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                arr[putIndex++] = i;
                if (putIndex == size) {
                    putIndex = 0;
                }
                realSize++;
                System.out.println(Thread.currentThread().getName() + ":produce:" + i);
            }
            arr.notifyAll();
        }

    }

    public void consume() {
        synchronized (arr) {
            if (realSize == 0) {
                try {
                    arr.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Object consume = arr[getIndex];

                arr[getIndex++] = null;
                if (getIndex == size) {
                    getIndex = 0;
                }
                realSize--;
                System.out.println(Thread.currentThread().getName() + ":Consume:" + consume);

            }
            arr.notifyAll();
        }
    }


}
