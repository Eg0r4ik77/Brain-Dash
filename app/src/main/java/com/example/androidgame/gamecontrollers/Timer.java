package com.example.androidgame.gamecontrollers;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class Timer{

    private final int timerDurationInMillis;
    private final int countDownInterval = 1;
    private int timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private final TextView timerText;
    private ProgressBar progressBar;

    public Timer(int timerDurationInMillis, TextView timerText) {
        this.timerDurationInMillis = timerDurationInMillis;
        this.timerText = timerText;
        timeLeftInMillis = this.timerDurationInMillis;
    }

    public Timer(int timerDurationInMillis, TextView timerText, ProgressBar progressBar) {
        this.timerDurationInMillis = timerDurationInMillis;
        this.timerText = timerText;
        this.progressBar = progressBar;

        timeLeftInMillis = this.timerDurationInMillis;
    }

    public void run(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, countDownInterval) {
            @Override
            public void onTick(long l) {
                if(progressBar != null){
                    progressBar.setProgress((timerDurationInMillis-timeLeftInMillis)*100/timerDurationInMillis);
                }
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
        countDownTimer.cancel();
    }

    public abstract void finish();
}
