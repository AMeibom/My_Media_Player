package com.example.my_media_player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mPlayer;// создание поля медиа-плеера
    Button playButton, pauseButton, stopButton;//Создание кнопок Плей, Пауза, Стоп

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer=MediaPlayer.create(this, R.raw.bis);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });
         // присваивание полям id ресурсов
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);


        pauseButton.setEnabled(false);//Сделать кнопку недоступной
        stopButton.setEnabled(false);//Сделать кнопку недоступной
    }

    private void stopPlay(){
        mPlayer.stop();
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
            playButton.setEnabled(true);
        }
        catch (Throwable t) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // метод кнопки Плей
     public void play(View view){

        mPlayer.start();
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }
    // метод кнопки Пауза
    public void pause(View view){

        mPlayer.pause();
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(true);
    }
    public void stop(View view){
        stopPlay();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        System.out.println("Была нажата кнопка " + keyCode);

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Действия при нажатии кнопки BACK
            System.out.println("Ура, нажали кнопку BACK.");
            mPlayer.stop();
           // clearMediaPlayer();
            return true; // возврат true, чтобы сигнализировать, что событие обработано

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            System.out.println("Звук громче");

            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volume = (float) currentVolume / maxVolume; // текущая громкость (от 0 до 1)

            int newVolume = currentVolume + (int) (0.1 * maxVolume);
            if (newVolume > maxVolume) {
                newVolume = maxVolume;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
            // Обновляем громкость в MediaPlayer:
            float newVolumeFloat = (float) newVolume / maxVolume;
            mPlayer.setVolume(newVolumeFloat, newVolumeFloat);

            return true; // возврат true, чтобы сигнализировать, что событие обработано

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            System.out.println("Звук тише");

            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volume = (float) currentVolume / maxVolume; // текущая громкость (от 0 до 1)

            int newVolume = currentVolume - (int) (0.1 * maxVolume);
            if (newVolume > maxVolume) {
                newVolume = maxVolume;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
            // Обновляем громкость в MediaPlayer:
            float newVolumeFloat = (float) newVolume / maxVolume;
            mPlayer.setVolume(newVolumeFloat, newVolumeFloat);

            return true; // возврат true, чтобы сигнализировать, что событие обработано
        }


        return false; // возврат false, чтобы сигнализировать, что событие не обработано
    }
    // при уничтожении активити вызов метода остановки и очиски MediaPlayer
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            stopPlay();
        }
    }
}