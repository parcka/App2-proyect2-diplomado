package com.parcka.xtr100.app2_proyect2_diplomado;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parcka.xtr100.app2_proyect2_diplomado.tools.ParseTime;

import java.io.IOException;
import java.util.HashMap;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MusicActivity.class.getSimpleName();
    TextView textViewDuration;
    MediaPlayer mediaPlayer;
    Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //Instancia de mi toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        textViewDuration = (TextView) findViewById(R.id.textViewDuration);

        buttonPlay = (Button) findViewById(R.id.buttonExecute);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                HashMap time = (HashMap) ParseTime.getMinutesWithSeconds(mp.getDuration() / 1000);
                textViewDuration.setText(time.get("minutes").toString() + ":" + time.get("seconds").toString() + " Duracion");
                mp.start();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.element_toolbar_music, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.nextarrow:
                openImageActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void openImageActivity() {
        Intent imageActivity = new Intent(getApplicationContext(), ImageActivity.class);
        startActivity(imageActivity);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void executeMusicPlayer(View view) {
        String urlExample = "http://sound.vmusice.net/download/RJZNvlHqkYxQTktq-XvkGSjkaiuhHP5ThQd9zRZdgXUmEAqdf7mhQkA9lsIAiSpvS46MRte2BCe4h9foa8NPD1MZScGk8yqfxxzS6H_Bz2kAoaT9Obt7kjj_Pswekw0NWo-wWY3jMON_h78k76CwV2Mol-wWJ0hPSWMqW0SBEgQ265O21PxaF66xWysnuno7wlQKtN94DHlEWU4CdJNtrA/justin_timberlake_cant_cant_stop_the_feeling_saxity_ft_angie_keilhauer_remix_(vmusice.net).mp3";

        if (mediaPlayer.isPlaying()) {
            Toast.makeText(getApplicationContext(), "Ya se encuentra sonando una cancion", Toast.LENGTH_SHORT).show();
        } else {
            try {

                buttonPlay.setEnabled(false);
//                buttonPlay.setTextColor(Color.GRAY);

                mediaPlayer.setDataSource(urlExample);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.e(TAG, "Error intentando reproducir la musica: " + e);
            }
        }

    }

    @Override
    public void onClick(View v) {

        if (mediaPlayer.isPlaying()) {

            switch (v.getId()) {
                case R.id.buttonPause:
                    mediaPlayer.pause();
                    effectActivateButton(buttonPlay);
                    break;
                case R.id.buttonStop:
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    effectActivateButton(buttonPlay);
                    break;
            }
        }
    }

    private void effectActivateButton(Button buttonPlay) {
        buttonPlay.setEnabled(true);
//        buttonPlay.setBackgroundColor(Color.);
    }
}
