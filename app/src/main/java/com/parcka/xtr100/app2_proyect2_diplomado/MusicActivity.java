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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parcka.xtr100.app2_proyect2_diplomado.tools.RegistrationService;
import com.parcka.xtr100.app2_proyect2_diplomado.tools.ParseTime;
import com.parcka.xtr100.app2_proyect2_diplomado.tools.Utils;

import java.io.IOException;
import java.util.HashMap;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MusicActivity.class.getSimpleName();
    TextView textViewDuration;
    MediaPlayer mediaPlayer;
    Button buttonPlay;
    private ProgressBar progressBarBuffer;
    private ProgressBar spinner;
    TextView editTextUrl;
    TextView textViewBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        //Progress bar
        progressBarBuffer = (ProgressBar) findViewById(R.id.progressBarBuffer);
        progressBarBuffer.setVisibility(View.GONE);
        spinner = (ProgressBar) findViewById(R.id.progressBarSpinner);
        spinner.setVisibility(View.GONE);


        //Instancia de mi toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        editTextUrl = (TextView) findViewById(R.id.editTextUrl);
        textViewBuffer = (TextView) findViewById(R.id.textViewBuffer);
        textViewBuffer.setVisibility(View.GONE);


        buttonPlay = (Button) findViewById(R.id.buttonExecute);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        spinner.setVisibility(View.GONE);
                        setEnabledBufferInformation(true);

                        progressBarBuffer.setProgress(percent);

                        if (percent == 100) {

                            setEnabledBufferInformation(false);


                        }
                    }
                });
                HashMap time = (HashMap) ParseTime.getMinutesWithSeconds(mp.getDuration() / 1000);
                textViewDuration.setText(time.get("minutes").toString() + ":" + time.get("seconds").toString() + " Duracion");

                mp.start();
            }
        });

        if (Utils.CheckPlayServices(this)) {
            Log.d(TAG, "Existe Google Play");
            Intent i = new Intent(this, RegistrationService.class);
            startService(i);

        } else {
            Log.d(TAG, "NO Existe Google Play");
        }

        //Si se genera la llamada mediante Notificacion, Reproducir la url de la notificacion

        if (isCallingFromNotification()) {
            Bundle data = getIntent().getExtras().getBundle("data");
            String url = data.getString("url");
            playMusic(url);
        }


    }

    private void setEnabledBufferInformation(boolean state) {

        progressBarBuffer.setEnabled(state);
        textViewBuffer.setEnabled(state);

        if (state) {
            progressBarBuffer.setVisibility(View.VISIBLE);
            textViewBuffer.setVisibility(View.VISIBLE);

        } else {
            progressBarBuffer.setVisibility(View.GONE);
            textViewBuffer.setVisibility(View.GONE);
        }

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
        finish();
    }

    public void executeMusicPlayer(View view) {
        playMusic(null);
    }

    private void playMusic(String url) {
        spinner.setVisibility(View.VISIBLE);
        String urlExample = "https://mp3skull.onl/api/soundcloud/dl/?d=eyJ0aXRsZSI6IlJpaGFubmEgLSBXb3JrIChMb3N0IEtpbmdzIFJlbWl4KSIsInVybCI6Imh0dHBzOi8vYXBpLnNvdW5kY2xvdWQuY29tL3RyYWNrcy8yNTM5MDg3NzMvc3RyZWFtP2NsaWVudF9pZD05OTdmNzk1MWE0NjhmNjU0ZDUxZjJjM2VhYTM5OTY0NCMubXAzIn0=";

        if (url != null) {
            urlExample = url;
        } else if (!editTextUrl.getText().toString().equals("")) {
            urlExample = editTextUrl.getText().toString();
        } else {
            //default
        }

        if (mediaPlayer.isPlaying()) {
            Toast.makeText(getApplicationContext(), "Ya se encuentra sonando una cancion", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            mediaPlayer.reset();

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

    private boolean isCallingFromNotification() {

        if (getIntent().getExtras() != null) {
            return true;
        }

        return false;
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

                    progressBarBuffer.setProgress(0);
                    effectActivateButton(buttonPlay);
                    setEnabledBufferInformation(false);
                    spinner.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void effectActivateButton(Button buttonPlay) {
        buttonPlay.setEnabled(true);
//        buttonPlay.setBackgroundColor(Color.);
    }


}
