package com.example.MyProject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity1 extends AppCompatActivity {
    EditText cityname;
    TextView temptext;
    TextView temptext1;
    TextView temptext2;
    TextView temptext3;
    TextView desc;
    Button reload;
    ImageView weather;
    ImageView weather1;
    ImageView weather2;
    ImageView weather3;
    TextView time1;
    TextView time2;
    TextView time3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Drawable morning = getDrawable(R.drawable.morning);
        Drawable evening = getDrawable(R.drawable.evening);
        Drawable night = getDrawable(R.drawable.night);
        RelativeLayout layout = findViewById(R.id.relative);
        cityname = findViewById(R.id.city);
        temptext = findViewById(R.id.temp);
        temptext1 = findViewById(R.id.temp1);
        temptext2 = findViewById(R.id.temp2);
        temptext3 = findViewById(R.id.temp3);
        desc = findViewById(R.id.descripton);
        reload = findViewById(R.id.dataload);
        weather = findViewById(R.id.weather);
        weather1 = findViewById(R.id.weather1);
        weather2 = findViewById(R.id.weather2);
        weather3 = findViewById(R.id.weather3);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        String apikey = "b58e782d8c5a53386137f197053c672a";
        if (timecheck() >= 7 && timecheck() < 13) {
            // Morning
            layout.setBackground(morning);
        } else if (timecheck() >= 13 && timecheck() < 19) {
            // Evening
            layout.setBackground(evening);
        } else if (timecheck() >= 19 || timecheck() < 7) {
            // Night
            layout.setBackground(night);
        }
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cityname.getText().toString().isEmpty()) {
                    String city = cityname.getText().toString();
                    String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";
                    String apiUrl2 = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apikey + "&units=metric&cnt=4";
                    WeatherTask weatherapi = new WeatherTask(MainActivity1.this);
                    Weatherforcast weatherapi2 = new Weatherforcast(MainActivity1.this);
                    weatherapi.execute(apiUrl);
                    weatherapi2.execute(apiUrl2);
                } else {
                    temptext.setText("No city");
                    desc.setText("NaN");
                    temptext1.setText("--");
                    temptext2.setText("--");
                    temptext3.setText("--");
                    weather.setImageResource(R.drawable.cloudysunnybg);
                }
            }
        });
    }

    public void onWeatherDataFetched(double temp, String description) {
        if (!Double.isNaN(temp)) {
            weather.setImageResource(R.drawable.cloudysunny);
            temptext.setText(String.format("%.1f°C", temp));
            String capitalizedString = description.substring(0, 1).toUpperCase() + description.substring(1);
            desc.setText(capitalizedString);
        } else {
            temptext.setText("Wrong City");
            desc.setText("NaN");
        }
    }

    public void onWeathernoFetched(String text) {
        desc.setText("NaN");
        temptext.setText(text);
        weather.setImageResource(R.drawable.cloudysunnybg);
        temptext1.setText("--");
        time1.setText("NaN");
        weather1.setImageResource(R.drawable.cloudybg);
        temptext2.setText("--");
        time2.setText("NaN");
        weather2.setImageResource(R.drawable.fullcloudbg);
        temptext3.setText("--");
        time3.setText("NaN");
        weather3.setImageResource(R.drawable.rainingbg);
    }

    public void onWeatherDataFetched2(double temp1, double temp2, double temp3, String dt1, String dt2, String dt3) {
        if (!Double.isNaN(temp1)) {
            temptext1.setText(String.format("%.1f°C", temp1));
            time1.setText(dateformat(dt1));
            weather1.setImageResource(R.drawable.cloudy);
            temptext2.setText(String.format("%.1f°C", temp2));
            time2.setText(dateformat(dt2));
            weather2.setImageResource(R.drawable.fullcloud);
            temptext3.setText(String.format("%.1f°C", temp3));
            time3.setText(dateformat(dt3));
            weather3.setImageResource(R.drawable.raining);
        } else {
            temptext1.setText("--");
            time1.setText("NaN");
            temptext2.setText("--");
            time2.setText("NaN");
            temptext3.setText("--");
            time3.setText("NaN");
        }
    }

    public String dateformat(String date) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("hh:mma");
        String time12HourFormat = null;
        try {
            Date date1 = inputFormat.parse(date);
            time12HourFormat = outputFormat.format(date1);
            System.out.println(time12HourFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time12HourFormat;
    }

    public int timecheck() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
