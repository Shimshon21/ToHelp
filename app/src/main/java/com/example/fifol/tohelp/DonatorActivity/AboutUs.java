package com.example.fifol.tohelp.DonatorActivity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.fifol.tohelp.R;

public class AboutUs extends AppCompatActivity {

    MediaController mediaController;
    TextView scrollTxt;
    VideoView video;
    Uri videoPath;
    int videoLenght;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        video = findViewById(R.id.videoView);
        videoPath = Uri.parse("android.resource://" +getPackageName() + "/" + R.raw.songexample);
        scrollTxt = findViewById(R.id.scrollTxt);
        mediaController = new MediaController(this);
        videoLenght = 0;
        scrollTxt.setText("TO HELP : \n\n " +
                "אפליקציה חברתית אשר מטרתה לקשר \n\n בין הורים אשר אין ביכולתם  \n\n למספק לתינוק שלהם מזון אשר    \n\nיענה על כל צרכיו התזונתיים  \n\n לבין כל מי שרוצה לתרום לאותם הורים"
                );
        showVid();
    }

    //TODO pause the video and remember the exact time that the video was paused
    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
        videoLenght = video.getCurrentPosition();

    }


    //shows the video when the screen is coming up
    public void showVid(){
        Uri uri = Uri.parse(String.valueOf(videoPath));
        video.setVideoURI(uri);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);

        //TODO resume the video from the last point of view (or 0 if not watched yet)
        video.start();
        video.seekTo(videoLenght);


    }
    public void goBack(View view) {
        finish();
    }
}

