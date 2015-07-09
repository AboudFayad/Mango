package com.qcri.mango;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PicActivity extends Activity {

    ImageView locIcon;
    private Context cont;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        cont = this;

        //here is where we get our information from the LayAdapter class
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("img");
        String likes = extras.getString("likes");
        String location = extras.getString("location");
        final String loca = location;
        lat = extras.getDouble("lat");
        lon = extras.getDouble("lon");

        //Initializing content
        locIcon = (ImageView) findViewById(R.id.locIcon);
        locIcon.setImageResource(R.drawable.icon);

        //when you click the navigation icon it sends you to the MapsActivity class
        locIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(cont, MapsActivity.class);
                //Data we are sending to the mapsActivity class
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("loc", loca);
                cont.startActivity(intent);
            }
        });

        //Initializing content
        TextView loc = (TextView) findViewById(R.id.textView);
        TextView lik = (TextView) findViewById(R.id.textView2);
        loc.setText(location);
        lik.setText(likes);

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.fullScreen);

        //setting the pic to its original size
        image.setImageBitmap(bmp);
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
