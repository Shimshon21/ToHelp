package com.example.fifol.tohelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        TextView scrollTxt = findViewById(R.id.scrollTxt);


        scrollTxt.setText("TO HELP : \n\n " +
                "אפליקציה חברתית אשר מטרתה לקשר \n\n בין הורים אשר אין ביכולתם  \n\n למספק לתינוק שלהם מזון אשר    \n\nיענה על כל צרכיו התזונתיים  \n\n לבין כל מי שרוצה לתרום לאותם הורים"
                );
    }

    public void goBack(View view) {
        finish();
    }
}
