package liormic.com.onewheater.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Arrays;

import liormic.com.onewheater.Adapters.DayAdapter;
import liormic.com.onewheater.R;
import liormic.com.onewheater.wheater.Day;

public class DailyForecastActivity extends ListActivity {

 private Day[] mDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
       Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables,parcelables.length,Day[].class);
        DayAdapter adapter= new DayAdapter(this,mDays);
        setListAdapter(adapter);
    }

}
