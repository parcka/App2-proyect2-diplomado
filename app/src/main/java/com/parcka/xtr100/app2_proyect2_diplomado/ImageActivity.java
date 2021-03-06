package com.parcka.xtr100.app2_proyect2_diplomado;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = ImageActivity.class.getSimpleName();
    ImageView imageView;
    EditText editTextUrl;
    String url = "http://static3.elblogverde.com/wp-content/uploads/2015/03/naturaleza-paisaje.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //Instance of toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_image);
        setSupportActionBar(myToolbar);
        //ImageView for show downloaded image
        imageView = (ImageView) findViewById(R.id.imageView);
        //EditText with url
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);


        String urlGoogleDrive = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B3i-PEe_yIwfUTJId1lJekxMYWM";

        if (isCallingFromNotification()) {
            Bundle data = getIntent().getExtras().getBundle("data");
            String url = data.getString("url");
            this.url = url;
        }

        Glide.with(this)
                .load(url)
                .into(imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.element_toolbar_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.forwardarrow:
                openImageActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void openImageActivity() {
        Intent imageActivity = new Intent(getApplicationContext(), MusicActivity.class);
        startActivity(imageActivity);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void searchImage(View view) {
        //TODO: Validar la url desde el input

        if (Patterns.WEB_URL.matcher(editTextUrl.getText().toString()).matches()) {

            try {
                Glide.with(this)
                        .load(editTextUrl.getText().toString())
                        .into(imageView);
            }catch (Exception e){
                Log.e(TAG,"Error Obteniendo imagen",e);
                Toast.makeText(ImageActivity.this, "Error Obteniendo la Imagen", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(ImageActivity.this, "La url no es valida", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCallingFromNotification() {

        if (getIntent().getExtras() != null) {
            return true;
        }

        return false;
    }

}
