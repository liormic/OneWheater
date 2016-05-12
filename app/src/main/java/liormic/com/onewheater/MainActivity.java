package liormic.com.onewheater;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    public  static final String TAG=MainActivity.class.getSimpleName();
    private CurrentWheater mCurrentWheater;


    @BindView(R.id.TempLabel) TextView mTemperatureLabel;
    @BindView(R.id.HUMIDITY) TextView mHumidityValue;
    @BindView(R.id.PercipLevel) TextView mPrecipValue;
    @BindView(R.id.Summary) TextView mSummaryLabel;
    @BindView(R.id.iconimageView) ImageView mIconImageView;
    @BindView(R.id.timeLabel1)TextView mTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         double latitude=37.8267;
         double longitude= -122.423;

         String apiKey="bb1e15a6fd38f87d66b6df482f917dd1";
         String forecastUrl="https://api.forecast.io/forecast/"+
                 apiKey +"/"+latitude+","+longitude;
         if(IsNetworkAvailable()) {
             OkHttpClient client = new OkHttpClient();
             Request request = new Request.Builder()
                     .url(forecastUrl)
                     .build();
             Call call = client.newCall(request);
             call.enqueue(new Callback() {

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
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
}
        else{
    Toast.makeText(this, R.string.network_unavailble,Toast.LENGTH_LONG)
            .show();
        }
        Log.d(TAG,"Main UI code is running!");
        }


    private void updateDisplay() {

        mTemperatureLabel.setText(mCurrentWheater.getTemperature()+"");

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


}



































