package com.qcri.mango;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class LayAdapter extends RecyclerView.Adapter<LayAdapter.Holder> {

    private List<Models> models;
    private LayoutInflater layoutInflater;
    private MySingleton mySingleton;
    private ImageLoader imageLoader;
    private Context cont;



    public LayAdapter(Context context, List<Models> models) {

        this.models = models;
        layoutInflater = LayoutInflater.from(context);
        mySingleton = MySingleton.getInstance(context);
        imageLoader = mySingleton.getImageLoader();
        cont = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.elem, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    //This function is like a for loop, where it uses the getItemCount as its limit
    //and goes the through your content holder, and pass the information the layout you are
    //inflating in onCreateViewHolder
    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Models pos = models.get(position); //models is a list containing data on each index
        //getting date from the list models
        final double lat = pos.getLat();
        final double lon = pos.getLon();
        final String loc = pos.getLocation();
        holder.lat.setText(pos.getLocation());
        holder.lon.setText(pos.getLikes() + " Likes");
        holder.loc.setImageResource(R.drawable.icon);

        //when you click the navigation icon it sends you to the MapsActivity class
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cont, MapsActivity.class);
                //Data we are sending to the mapsActivity class
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("loc", loc);
                //cont is the activity we are starting the next class from
                cont.startActivity(intent);
            }
        });
        holder.pb.setVisibility(View.VISIBLE);
        String url = pos.getPic();
        loadImages(url, holder, position);
        holder.pb.setVisibility(View.INVISIBLE);

    }


    private void loadImages(final String url, final Holder holder, final int i) {
        if (url != null) {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.pic.setImageBitmap(response.getBitmap());

                    //This is what make the image clickable
                    holder.pic.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //when the image is clicked, it is converted into  ByteArray so
                            //it can be sent to the other class
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            Intent intent = new Intent(cont, PicActivity.class);
                            //These are the data we are sending to the picActivity class
                            intent.putExtra("img", stream.toByteArray());
                            intent.putExtra("location", models.get(i).getLocation());
                            intent.putExtra("likes", models.get(i).getLikes());
                            intent.putExtra("lat", models.get(i).getLat());
                            intent.putExtra("lon", models.get(i).getLon());
                            //cont is the activity we are starting the next class from
                            cont.startActivity(intent);


                /*Log.i("fayad", "" + a[0]);
                a[0] += 1;*/
                /*if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ){
                    holder.pic.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                }
                else if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
                    holder.pic.setSystemUiVisibility( View.STATUS_BAR_HIDDEN );
                else{}*/


                /*if(zoomOut[0]) {
                    Toast.makeText(cont, "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                    holder.pic.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    holder.pic.setAdjustViewBounds(true);
                    zoomOut[0] =false;
                }else{
                    Toast.makeText(cont, "FULLSCREEN!", Toast.LENGTH_LONG).show();
                    holder.pic.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    holder.pic.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut[0] = true;
                }*/ // your bitmap

                        }

                    });
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

    }


    //get the size of the list so you know for how long you need to go
    // in the onBindViewHolder
    @Override
    public int getItemCount() {
        return models.size();
    }


    //This is where you need to initialize your content that show on the
    //screen to be compatible with the RecyclerView
    static class Holder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView pic;
        TextView lat;
        TextView lon;
        ProgressBar pb;
        ImageView loc;

        public Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            pic = (ImageView) itemView.findViewById(R.id.imageView);
            lat = (TextView) itemView.findViewById(R.id.t1);
            lon = (TextView) itemView.findViewById(R.id.t2);
            pb = (ProgressBar) itemView.findViewById(R.id.progressBar);
            loc = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }
}
