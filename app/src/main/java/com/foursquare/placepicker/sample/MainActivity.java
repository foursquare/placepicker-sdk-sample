package com.foursquare.placepicker.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foursquare.api.types.Venue;
import com.foursquare.placepicker.PlacePicker;
import com.foursquare.placepicker.PlacePickerSdk;

public class MainActivity extends AppCompatActivity {

    // User your own keys. This is just a sample app.
    private static final String CONSUMER_KEY = "VC4SCKEQ1TUN4OGQQ4H0UC1ZZ0MBUF1KPWJGZB4ERZXK1L02";
    private static final String CONSUMER_SECRET = "NUJMHDQQOBJW5W0ZRKS4J2GV5RVQM24GTPY3RTMGLRDZ3GJ4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlacePickerSdk.with(new PlacePickerSdk.Builder(this)
                .consumer(CONSUMER_KEY, CONSUMER_SECRET)
                .imageLoader(new PlacePickerSdk.ImageLoader() {
                    @Override
                    public void loadImage(Context context, ImageView v, String url) {
                        Glide.with(context)
                                .load(url)
                                .placeholder(com.foursquare.snaptoplace.sample.R.drawable.category_none)
                                .dontAnimate()
                                .into(v);
                    }
                })
                .build());

        setContentView(com.foursquare.snaptoplace.sample.R.layout.activity_main);

        Button findPlace = (Button) findViewById(com.foursquare.snaptoplace.sample.R.id.btnPlacePick);
        findPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPlace();
            }
        });

        Button currentPlace = (Button) findViewById(com.foursquare.snaptoplace.sample.R.id.btnCurrentPlace);
        currentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClosestPlace();
            }
        });

        Button starbucks = (Button) findViewById(com.foursquare.snaptoplace.sample.R.id.btnStarbucksSearch);
        starbucks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookForStarbucks();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PlacePicker.PLACE_PICKED_RESULT_CODE) {
            Venue place = data.getParcelableExtra(PlacePicker.EXTRA_PLACE);
            Toast.makeText(this, place.getName(), Toast.LENGTH_LONG).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void pickPlace() {
        Intent i = new Intent(this, PlacePicker.class);
        startActivityForResult(i, 9001);
    }

    private void lookForStarbucks() {
        Intent i = new Intent(this, PlacePicker.class);
        i.putExtra(PlacePicker.EXTRA_QUERY, "starbucks");
        startActivityForResult(i, 9001);
    }

    private void getClosestPlace() {
        PlacePickerSdk.get().getCurrentPlace(new PlacePickerSdk.CurrentPlaceResult() {
            @Override
            public void success(Venue venue, boolean confident) {
                Toast.makeText(MainActivity.this,"Got closest place " + venue.getName() + " Confident? " + confident, Toast.LENGTH_LONG).show();
            }

            @Override
            public void fail() {
            }
        });
    }
}
