package liormic.com.onewheater.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;

import liormic.com.onewheater.Adapters.DayAdapter;
import liormic.com.onewheater.R;
import liormic.com.onewheater.wheater.Day;
import liormic.com.onewheater.wheater.Hour;

public class HourlyForecastActivity extends AppCompatActivity {
    private Hour[] mHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
       mHours = Arrays.copyOf(parcelables,parcelables.length,Hour[].class);

            }


        }


