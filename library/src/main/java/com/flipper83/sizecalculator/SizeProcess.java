package com.flipper83.sizecalculator;

import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * This class obtain the size from a view when this is ready.
 *
 * @author flipper83
 */
class SizeProcess implements ViewTreeObserver.OnPreDrawListener {


    private final SizeCalculator calculator;
    private WeakReference<View> view;
    private SizeReadyListener listener;

    public SizeProcess(View view, SizeCalculator calculator) {
        this.calculator = calculator;
        this.view = new WeakReference<View>(view, calculator.referenceQueue);
    }

    public void obtainSize(SizeReadyListener listener) {
        this.listener = listener;

        View viewObj = view.get();
        if (viewObj != null) {
            viewObj.getViewTreeObserver().addOnPreDrawListener(this);
        }
    }

    @Override
    public boolean onPreDraw() {

        View viewObj = view.get();

        if(viewObj != null){

            if (listener != null) {
                listener.onSizeReady(viewObj, viewObj.getWidth(), viewObj.getHeight());
                viewObj.getViewTreeObserver().removeOnPreDrawListener(this);
            }

            calculator.cancelAttachedView(viewObj);
        }

        listener = null;



        return true;
    }

    public void cancel() {
        this.listener = null;
        View viewObj = view.get();

        if (viewObj != null) {
            viewObj.getViewTreeObserver().removeOnPreDrawListener(this);
        }

        view.clear();

    }
}
