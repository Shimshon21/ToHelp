package com.example.fifol.tohelp.DeliveriesActivities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.Adapters.DeliveryAdapter;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CourierData;
import com.example.fifol.tohelp.Utils.MyOrdersData;

import java.io.IOException;
import java.util.List;

public class MissionsScreen extends AppCompatActivity {
    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final CloudantClient client = ClientBuilder.account(DB_USER_NAME).username(TEXT_API_KEY).password(TEXT_API_SECRET).build();
    String DB_ORDERS = "donaters_delivery_orders";
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_screen);
        loadListFromDB();
      listview = findViewById(R.id.listview);
        loadListFromDB();

        //Example of list
      /*  String[] values = new String[]{
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
        */

    }

    @SuppressLint("StaticFieldLeak")
    private void loadListFromDB() {

        new AsyncTask<Void, Void,List<MyOrdersData>> (){
            @Override
            protected List<MyOrdersData> doInBackground(Void... voids) {
                List<MyOrdersData> allOrders=null ;
                //return databases.toString();
                Database db = client.database(DB_ORDERS, false);
                try {
                    allOrders = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(MyOrdersData.class);
                    for (MyOrdersData item:allOrders){
                        System.out.println(item._id);
                    }
                    //System.out.println( db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(MyProductList.class)+"!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return allOrders;
            }

            @Override
            protected void onPostExecute(List<MyOrdersData> allOrders) {
                super.onPostExecute(allOrders);
                if(allOrders!=null){
                    final DeliveryAdapter adapter = new DeliveryAdapter(MissionsScreen.this, allOrders);
                    listview.setAdapter(adapter);
                }
            }
        }.execute();
    }

    public void wazeClick(View view) {
        CourierData courierData = new CourierData();
    /*    String address = view.getTag().toString();
        String uri = "https://waze.com/ul?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);*/

    }

    public void moreDetailClick(View view) {
        //Todo fix frame layout above the listview.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.deliveryDetails,new MissionDetails());
        transaction.commit();
    }

    public void goBack(View view) {
        finish();
    }
}

