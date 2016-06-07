package liormic.com.onewheater.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import liormic.com.onewheater.Adapters.DayAdapter;
import liormic.com.onewheater.Adapters.HourAdapter;
import liormic.com.onewheater.R;
import liormic.com.onewheater.wheater.Day;
import liormic.com.onewheater.wheater.Hour;

public class HourlyForecastActivity extends AppCompatActivity {
    private Hour[] mHours;
    @BindView(R.id.reyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
       mHours = Arrays.copyOf(parcelables,parcelables.length,Hour[].class);
        HourAdapter adapter = new HourAdapter(mHours);
         mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);



            }


        }


