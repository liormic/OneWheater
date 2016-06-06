package liormic.com.onewheater.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.OnClick;
import liormic.com.onewheater.R;
import liormic.com.onewheater.wheater.Current;
import liormic.com.onewheater.wheater.Day;
import liormic.com.onewheater.wheater.Forecast;
import liormic.com.onewheater.wheater.Hour;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;
    TextView mTemp;
    TextView mPercip;
    TextView mTime;
    TextView mHumid;
    TextView mSumm;
    ImageView mIcon;
    ImageView mRefresh;
    ProgressBar mProgressBar;
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTemp = (TextView) findViewById(R.id.TempLabel);
        mPercip = (TextView) findViewById(R.id.PercipLevel);
        mTime = (TextView) findViewById(R.id.timeLabel1);
        mHumid = (TextView) findViewById(R.id.HUMIDITY);
        mSumm = (TextView) findViewById(R.id.Summary);
        mIcon = (ImageView) findViewById(R.id.iconImage);
        mRefresh = (ImageView) findViewById(R.id.RefreshImageView);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar2) ;

        mProgressBar.setVisibility(View.INVISIBLE);


        getForcast();
    }

    private void getForcast() {



        double latitude = 32.073578;
        double longitude = 34.820312;
        String apiKey = "bb1e15a6fd38f87d66b6df482f917dd1";
        String forecastUrl = "https://api.forecast.io/forecast/" +
                apiKey + "/" + latitude + "," + longitude;
        if (IsNetworkAvailable()) {

            toggleRefresh();
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    alertAboutError();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });


                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception catched: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception catched: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailble, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility()== View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefresh.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefresh.setVisibility(View.VISIBLE);
        }

    }

    // Log.d(TAG,"Main UI code is running!");



    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemp.setText(formatFartoCel(current.getTemperature())+"");
        mTime.setText("At " + current.getFormattedTime()+" it will be");
        mHumid.setText(current.getHumidity()+"");
        mPercip.setText(current.getPercipChance()+"");
        mSumm.setText(current.getSummary());
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIcon.setImageDrawable(drawable);
    }
    
    
    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject((jsonData));
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        Day[] days = new Day[data.length()];


        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);

            Day day = new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            days[i] = day;
        }
return days;
    }
    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast= new JSONObject((jsonData));
        String timezone =forecast.getString("timezone");
        JSONObject hourly=forecast.getJSONObject("hourly");
        JSONArray data= hourly.getJSONArray("data");
        Hour hours[]= new Hour[data.length()];

        for(int i=0;i<data.length();i++){
           JSONObject jsonHour = data.getJSONObject(i);

            Hour hour=new Hour();
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i]=hour;

        }
return  hours;
    }

    private Current getCurrentDetails(String jsonData)throws JSONException{
        JSONObject forecast= new JSONObject((jsonData));
        String timezone =forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPercipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getInt("temperature"));
        current.setmTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());


        return current;
    }

    private int formatFartoCel(int temp){

        temp = (int) ((temp-32)*(5.0 / 9.0));


   return  temp;
    }
    private boolean IsNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =manager.getActiveNetworkInfo();
        boolean isAvailable =false;
        if(networkInfo!=null&& networkInfo.isConnected()){
         isAvailable=true;
            return  isAvailable;
        }
        return isAvailable;
    }

    private void alertAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }


    public void getForcastButton(View view) {
        getForcast();
    }


    @OnClick(R.id.dailyButton)
    public void StartDailyActivity(View view) {
        Intent intent=new Intent(this,DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);
    }
   @OnClick(R.id.Hourlybtn)
    public void StartHourlyActivity(View view){
       Intent intent = new Intent(this,HourlyForecastActivity.class);
       intent.putExtra(HOURLY_FORECAST,mForecast.getHourlyForecast());
   }

}



































