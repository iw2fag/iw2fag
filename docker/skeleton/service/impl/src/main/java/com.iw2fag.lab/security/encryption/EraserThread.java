package com.iw2fag.lab.security.encryption;

/**
 * Created with IntelliJ IDEA.
 * User: davidofv
 * Date: 06/08/15
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class EraserThread implements Runnable {
    private boolean stop;

    /**
     *@param prompt displayed to the user
     */
    public EraserThread(String prompt) {
        System.out.print(prompt);
    }

    /**
     * Begin masking...display asterisks (*)
     */
    public void run () {
        stop = true;
        while (stop) {
            System.out.print("\010 ");
            try {
                Thread.currentThread().sleep(1);
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * Instruct the thread to stop masking
     */
    public void stopMasking() {
        this.stop = false;
    }
}
