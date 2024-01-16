package com.example.MyProject;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.units.Temperature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class MainActivity1 extends AppCompatActivity implements WeatherAsyncTask.WeatherCallback,WeatherAsyncTask2.WeatherCallback{
    EditText cityname;
    TextView temptext;
    TextView temptext1;
    TextView temptext2;
    TextView temptext3;
    TextView desc;
    Button reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = findViewById(R.id.city);
        temptext = findViewById(R.id.temp);
        temptext1 = findViewById(R.id.temp1);
        temptext2 = findViewById(R.id.temp2);
        temptext3 = findViewById(R.id.temp3);
        desc = findViewById(R.id.descripton);
        reload = findViewById(R.id.dataload);
        String city = cityname.getText().toString();
        String apikey = "b58e782d8c5a53386137f197053c672a";
        String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";
        String link2 = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=" + apikey + "&units=metric&cnt=4";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=karachi&appid=b58e782d8c5a53386137f197053c672a&units=metric"; // Replace with your API URL
        String apiUrl2 = "https://api.openweathermap.org/data/2.5/forecast?q=Karachi&appid=b58e782d8c5a53386137f197053c672a&units=metric&cnt=3";
        WeatherAsyncTask weatherapi = new WeatherAsyncTask(this);
        WeatherAsyncTask2 weatherapi2 = new WeatherAsyncTask2(this);
        weatherapi.execute(apiUrl);
        weatherapi2.execute(apiUrl2);
    }

    @Override
    public void onWeatherDataFetched(double temperature, String description) {
        // Handle the fetched temperature and description here
        Log.d("Weather", "Temperature: " + temperature + "°C, Description: " + description);
        System.out.println(temperature + " " + description);
    }
    @Override
    public void onWeatherDataFetched2(double temp1, double temp2, double temp3) {
        System.out.println(temp1+"\n"+temp2+"\n"+temp3+"\n");

    }

}

