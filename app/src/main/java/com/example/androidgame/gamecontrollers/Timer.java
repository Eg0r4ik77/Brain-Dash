package com.example.androidgame.gamecontrollers;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer{

    private final int timerDurationInMillis = 60000;
    private final int countDownInterval = 1000;
    private int timeLeftInMillis = timerDurationInMillis;
    private CountDownTimer countDownTimer;
    private final TextView timerText;

    public Timer(TextView textView){
        timerText = textView;
    }

    public void run(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, countDownInterval) {
            @Override
            public void onTick(long l) {
                timerText.setText(""+l/1000);
                timeLeftInMillis = (int)l;
            }
            @Override
            public void onFinish() {
                    timerText.setText("Time is up! Points: ");
            }
        }.start();
    }

    public void pause(){
        this.countDownTimer.cancel();
    }

}
