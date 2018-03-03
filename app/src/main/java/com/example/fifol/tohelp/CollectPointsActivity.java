package com.example.fifol.tohelp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.fifol.tohelp.Adapters.CollectPointsAdapter;

import java.util.ArrayList;

/**
 * Created by fifol on 21/02/2018.
 */

    public class CollectPointsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_points_screen);

        final ListView listview = findViewById(R.id.listview);

        //TODO שליפת הנתונים מתוך הדאטאבייס
        String[] values = new String[]{
                "מרכז קהילתי נאות שושנים.\nסיגלון 12, חולון.\nטלפון: 03-5503977",
                "מרכז קהילתי ומרכז הספורט בן גוריון.\nקרסל 6, חולון.\nטלפון: 03-5528490",
                "מרכז קהילתי נווה ארזים.\nישעיהו 16, חולון.\nטלפון: 03-5506772",
                "מרכז קהילתי וולפסון.\nצבי ש\"ץ 29, חולון.\nטלפון: 03-6519181",
                "מרכז קהילתי לזרוס.\nסנהדרין 27, חולון.\nטלפון: 03-5030068",
                "מרכז קהילתי נאות רחל.\nחצרים 2, חולון.\nטלפון: 03-5035499",
                "מרכז קהילתי קליין.\nפילדפיה 16, חולון.\nטלפון: 03-5038083",
                "מרכז קהילתי תורני.\nפילדפיה 5, חולון.\nטלפון: 03-5015529",
                "מרכז פסגות.\nסרלין 21, חולון.\nטלפון: 03-6530300",
                "מרכז חנקין.\nחנקין 109, חולון.\nטלפון: 03-5590021",
                "רעים מרכז למחול ותנועת הגוף.\nהופיין 44, חולון.\nטלפון: 03-5035299",
                "מרכז שטיינברג החדש -\nבמה למוסיקה חיה.\nגבעת התחמושת 21, חולון.\nטלפון: 03-5500012"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final CollectPointsAdapter adapter = new CollectPointsAdapter(this, values);
        listview.setAdapter(adapter);
    }

    public void waze_btn_click(View view) {
        String address = view.getTag().toString();
        String uri = "https://waze.com/ul?q="+address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void goBack(View view) {
        finish();
    }
}
