package com.me.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toolbar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = true;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String uriPath = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            uriPath = extras.getString("urlVideo");
        }

        progressBar = (ProgressBar) findViewById(R.id.progrss);
        videoView = (VideoView) findViewById(R.id.videoView);

        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        uri = Uri.parse(uriPath);
        progressBar.setVisibility(View.VISIBLE);
        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isContinuously){
                    mp.start();
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.start();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
