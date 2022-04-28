package com.example.androidgame.gamecontrollers;

import android.os.CountDownTimer;
import android.widget.TextView;

public abstract class Timer{

    private final int timerDurationInMillis;
    private final int countDownInterval = 1000;
    private int timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private final TextView timerText;


    public Timer(int timerDurationInMillis, TextView timerText) {
        this.timerDurationInMillis = timerDurationInMillis - 1000;
        this.timerText = timerText;
        timeLeftInMillis = this.timerDurationInMillis;
    }

    public void run(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, countDownInterval) {
            @Override
            public void onTick(long l) {
                timerText.setText(""+(l+1000)/1000);
                timeLeftInMillis = (int)l;
            }
            @Override
            public void onFinish() {
                   finish();
            }
        }.start();
    }

    public void pause(){
        this.countDownTimer.cancel();
    }

    public abstract void finish();
}
