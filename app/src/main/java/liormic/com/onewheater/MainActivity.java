package liormic.com.onewheater;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWheater mCurrentWheater;
    TextView mTemp;
    TextView mPercip;
    TextView mTime;
    TextView mHumid;
    TextView mSumm;
    ImageView mIcon;
    ImageView mRefresh;
    ProgressBar mProgressBar;


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
                            mCurrentWheater = getCurrentDetails(jsonData);
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

        mTemp.setText(formatFartoCel(mCurrentWheater.getTemperature())+"");
        mTime.setText("At " + mCurrentWheater.getFormattedTime()+" it will be");
        mHumid.setText(mCurrentWheater.getHumidity()+"");
        mPercip.setText(mCurrentWheater.getPercipChance()+"");
        mSumm.setText(mCurrentWheater.getSummary());
        Drawable drawable = getResources().getDrawable(mCurrentWheater.getIconId());
        mIcon.setImageDrawable(drawable);
    }

    private CurrentWheater getCurrentDetails(String jsonData)throws JSONException{
        JSONObject forecast= new JSONObject((jsonData));
        String timezone =forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentWheater currentWheater = new CurrentWheater();
        currentWheater.setHumidity(currently.getDouble("humidity"));
        currentWheater.setTime(currently.getLong("time"));
        currentWheater.setIcon(currently.getString("icon"));
        currentWheater.setPercipChance(currently.getDouble("precipProbability"));
        currentWheater.setSummary(currently.getString("summary"));
        currentWheater.setTemperature(currently.getInt("temperature"));
        currentWheater.setmTimeZone(timezone);

        Log.d(TAG,currentWheater.getFormattedTime());


        return  currentWheater;
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
}



































