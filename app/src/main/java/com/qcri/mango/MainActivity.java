package com.qcri.mango;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    String url;
    List<Models> apiElem;
    private RecyclerView rv;
    public RequestQueue queue;
    GPSTracker gps;
    private boolean zoomOut =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the recyclerView ready to get content (Initialize it)
        rv=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //calling the GPS to activity to get lat  and long later
        gps = new GPSTracker(MainActivity.this);

        //chech if the geolocation is enabled on the phone
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            url = "https://www.googleapis.com/fusiontables/v2/query?sql=SELECT+*+FROM+1FG18OCEbikEwaHc6x5HOjhtcA2yky9oo20NsPCmb+ORDER+BY+ST_DISTANCE(latitude%2C+LATLNG(" + latitude + ",%20" + longitude + "))+LIMIT+30&hdrs=true&typed=true&key=AIzaSyD8fwsnJkLuj7_BqTvO94aqmCq0rt_6vrA";
            getData(url);
        } else {
            //show error if the geolocation is not enabled
            gps.showSettingsAlert();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //update Button, it does the same thing as "onCreate"
        if (id == R.id.action_get_data) {
            rv=(RecyclerView)findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);

            gps = new GPSTracker(MainActivity.this);

            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                url = "https://www.googleapis.com/fusiontables/v2/query?sql=SELECT+*+FROM+1FG18OCEbikEwaHc6x5HOjhtcA2yky9oo20NsPCmb+ORDER+BY+ST_DISTANCE(latitude%2C+LATLNG(" + latitude + ",%20" + longitude + "))+LIMIT+30&hdrs=true&typed=true&key=AIzaSyD8fwsnJkLuj7_BqTvO94aqmCq0rt_6vrA";
                getData(url);
            } else {
                gps.showSettingsAlert();
            }
        }
        return false;
    }

    //This calls the lay adapter to show the content (images and text)
    public void updateText(){
        if (apiElem != null){
            LayAdapter adapter = new LayAdapter(this, apiElem);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Sorry, something went wrong:(", Toast.LENGTH_LONG).show();
        }

    }

    public void toastError(){
        Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
    }

    //this is where the httpRequest gets called
    public void getData(String inp){

        // Instantiate the RequestQueue.
        queue = MySingleton.getInstance(this).getRequestQueue();

        //Send the request to get back the data as a JSON Object
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, inp, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        apiElem = JsonParser.parse(response);
                        updateText();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toastError();
                    }
                });
        queue.add(jsObjRequest);
    }
}