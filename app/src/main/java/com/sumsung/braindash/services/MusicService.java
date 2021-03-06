package com.sumsung.braindash.services;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.sumsung.braindash.R;
import com.sumsung.braindash.Strings;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    int musicId;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicId = intent.getIntExtra(Strings.MUSIC, R.raw.music_background_menu);
        if(mediaPlayer != null) mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, musicId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
