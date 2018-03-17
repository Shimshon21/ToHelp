package com.example.fifol.tohelp.WareHouseActivities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.Adapters.MyWarhouseListAdapter;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyProdutsData;
import com.example.fifol.tohelp.Utils.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WareHouseActivity extends AppCompatActivity {
    private     CloudantClient client = CloudentKeys.getClientBuilder();
    private String ALL_PRODUCTS_DB = "products";
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
     private TextView tableName;
     private   MyWarhouseListAdapter adapter ;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private String mActivityTitle;
   Button hamburgerBtn;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_house);
        setViews();
        adapter = new MyWarhouseListAdapter(WareHouseActivity.this,new ArrayList<MyProdutsData>(0));
        mDrawerList.setAdapter(adapter);
        setupDrawer();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setNavigationOnItemSelected();
        setHamburgerMenuDrawer();
    }
    private void setHamburgerMenuDrawer() {
        hamburgerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                } else {
                    mDrawerLayout.openDrawer(Gravity.END);

                }
            }
        });
    }

    //Set table list by item was picked from navigationView.
    private void setNavigationOnItemSelected() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()){
                    case "סימילק" :
                        getCompanyaData("Similac");
                        tableName.setText("סימילק");
                        break;
                    case "מטרנה":
                        getCompanyaData("materna");
                        tableName.setText("מטרנה");
                        break;
                    case "נוטרילון":
                        getCompanyaData("Nutrilon");
                        tableName.setText("נוטרילון");
                        break;
                    case "שופרסל בייבי":
                        getCompanyaData("Shupershal");
                        tableName.setText("שופרסל בייבי");
                        break;
                    case "פדיאשור":
                        getCompanyaData("Pediasure");
                        tableName.setText("פדיאשור");
                        break;
                    case "התנתק":
                        UserData.logOut();
                        finish();
                }
                return false;
            }
        });
    }

    //Find views by id.
    private void setViews() {
        tableName = findViewById(R.id.tableName);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerList = findViewById(R.id.AllProductsLayout);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        navigationView = findViewById(R.id.nav_view);
        hamburgerBtn = findViewById(R.id.hamburger);
    }

    //Drawer settings.
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
    }

    //Get all documents by company type.
    @SuppressLint("StaticFieldLeak")
    public void getCompanyaData(final String companyName){
        System.out.println("get company data");
        new AsyncTask<Void, Void,List<MyProdutsData>>() {
            @Override
            protected List<MyProdutsData> doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(ALL_PRODUCTS_DB, false);
                String myJson=setQuery(companyName);
                List<MyProdutsData> resultsItems = db.findByIndex(myJson, MyProdutsData.class);
                return resultsItems;
            }

            @Override
            protected void onPostExecute(List<MyProdutsData> resultsItems) {
                adapter.refreshData(resultsItems);
            }
        }.execute();
    }

    //Set search by company from database.
    private String  setQuery(String companyName) {
        String myJson="";
        try {
            JSONObject myQuery = new JSONObject();
            JSONObject myQueryField = new JSONObject();
            myQueryField.put("$eq", companyName);
            JSONObject myData = new JSONObject();
            myData.put("company", myQueryField);
            myQuery.put("selector", myData);
            myQuery.put("fields", "[_id,company,title,desc,count]");
            myQuery.put("sort", "[{_id,asc}]");
            Log.e("JSON", "myJson: " + myQuery.toString());
            myJson = myQuery.toString();
        } catch (JSONException e) {
        }
        return myJson;
    }

}
