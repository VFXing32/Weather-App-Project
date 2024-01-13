package com.example.MyProject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText cityname;
    TextView temptext;
    TextView temptext1;
    TextView temptext2;
    TextView temptext3;
    String apikey = "b58e782d8c5a53386137f197053c672a";
    String city = "Karachi";
    String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";
    String link2 = "https://api.openweathermap.org/data/2.5/forecast?q=karachi&appid=b58e782d8c5a53386137f197053c672a&units=metric&cnt=4";

    public void onclick(View v) {
        Drawable back = getDrawable(R.drawable.back2);
        RelativeLayout layout = findViewById(R.id.relative);
        layout.setBackground(back);
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = findViewById(R.id.city);
        temptext = findViewById(R.id.temp);
        temptext1 = findViewById(R.id.temp1);
        temptext2 = findViewById(R.id.temp2);
        temptext3 = findViewById(R.id.temp3);
        new syncData().execute();
    }

    public class syncData extends AsyncTask<String, String, List<String[]>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String[]> temperaturesList) {
            if (temperaturesList != null && !temperaturesList.isEmpty()) {
                // Do something with the list of temperatures
                // For example, display them in a TextView
                StringBuilder tempBuilder = new StringBuilder();
                for (String[] loopDataArray : temperaturesList) {
                    temptext.setText(loopDataArray[0]);
                    temptext1.setText(loopDataArray[1]);
                    temptext2.setText(loopDataArray[2]);
                    temptext3.setText(loopDataArray[3]);
                }
            } else {
                temptext.setText("Failed to fetch temperatures");
            }
        }

        @Override
        protected List<String[]> doInBackground(String... strings) {
            List<String[]> temperaturesList = new ArrayList<>();

            try {
                URL url = new URL(link2);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while (true) {
                    String readLine = reader.readLine();
                    String data = readLine;
                    if (data == null) {
                        break;
                    }
                    String[] loopDataArray = parseJsonText(data);
                    temperaturesList.add(loopDataArray);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return temperaturesList;
        }

        private String[] parseJsonText(String json) throws JSONException {
            JSONObject jsonObject = new JSONObject(json);
            int numberOfIterations = 8;
            String[] loopDataArray = new String[numberOfIterations];

            if (jsonObject.has("list")) {
                JSONArray listArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listItem = listArray.getJSONObject(i);

                    if (listItem.has("main")) {
                        JSONObject mainObject = listItem.getJSONObject("main");

                        if (mainObject.has("temp")) {
                            String temperature = mainObject.getString("temp");
                            loopDataArray[i] = temperature;
                        }
                    }
                }
                return loopDataArray;
            }
            return new String[]{"Failed to parse weather data"};
        }
    }
}
