package com.anyonavinfo.bluetoothphone.bpclient.custom;

/**
 * Created by shijj on 2016/9/22.
 */
public interface OnAnimateListener {

    /**
     * interface for view animation
     *
     * @author kyle
     *
     */
        /**
         * invoked when the animation start
         */
        public void onAnimationStart();

        /**
         * ask view whether continue Animating
         *
         * @return boolean true for continueAnimating
         */
        public boolean continueAnimating();

        /**
         * a new frame is ready.
         *
         * @param frame next step of the animation, for linear animation, it is equal to velocity
         */
        public void onFrameUpdate(int frame);

        /**
         * invoked when the animation complete
         */
        public void onAnimateComplete();
    }

