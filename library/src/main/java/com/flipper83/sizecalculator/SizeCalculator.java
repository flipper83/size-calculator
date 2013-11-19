package com.flipper83.sizecalculator;

import android.os.Process;
import android.util.Log;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.WeakHashMap;

/**
 * This class process all size requests
 *
 * @author flipper83
 */
public class SizeCalculator {
    final ReferenceQueue<View> referenceQueue;
    private final WeakHashMap<View, SizeProcess> attachedRequests;
    private final ReferenceCheckerThread referenceChecker;

    SizeCalculator(ReferenceQueue<View> referenceQueue) {

        this.referenceQueue = referenceQueue;
        this.attachedRequests = new WeakHashMap<View, SizeProcess>();
        this.referenceChecker = new ReferenceCheckerThread(referenceQueue);
        this.referenceChecker.start();

    }

    public static SizeCalculator create() {
        ReferenceQueue<View> tempPeferenceQueue = new ReferenceQueue<View>();
        return new SizeCalculator(tempPeferenceQueue);
    }

    public void calculateSize(View view, SizeReadyListener listener) {

        if (view == null) {
            throw new IllegalArgumentException("The view cannot be null.");
        }

        SizeProcess process = new SizeProcess(view, this);
        cancelAttachedView(view);

        //add the size request
        attachedRequests.put(view,process);

        process.obtainSize(listener);
    }

    void cancelAttachedView(View view) {
        SizeProcess process = attachedRequests.remove(view);
        if (process != null) {
            process.cancel();
        }
    }

    private class ReferenceCheckerThread extends Thread {
        ReferenceQueue<View> referenceQueue;

        ReferenceCheckerThread(ReferenceQueue<View> referenceQueue) {
            this.referenceQueue = referenceQueue;
            setDaemon(true);
            setName("SizeCalculator/RefChecker");
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            while (true) {
                try {
                    Reference<? extends View> toRemove = referenceQueue.remove();
                    if (toRemove.get() != null) {
                        cancelAttachedView(toRemove.get());
                    }
                } catch (InterruptedException e) {
                    //TODO SEARCH A WAY TO NOTIFY THIS ERRORS
                    break;
                }
            }
        }

        void shutdown() {
            interrupt();
        }
    }

}

