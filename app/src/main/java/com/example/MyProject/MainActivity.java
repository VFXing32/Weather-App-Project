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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityname;
    TextView temptext;
    String apikey = "b58e782d8c5a53386137f197053c672a";
    String city = "Karachi";
    String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";
    String link2 = "https://api.openweathermap.org/data/2.5/forecast?q=karachi&appid=b58e782d8c5a53386137f197053c672a&units=metric&cnt=8";

    public void onclick(View v) {
        Drawable back = (Drawable) getDrawable(R.drawable.back2);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative);
        layout.setBackground(back);
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = findViewById(R.id.city);
        temptext = findViewById(R.id.temp);
        new syncData().execute();
    }

    public class syncData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String temp) {
//            if (temptext != null) {
//                temptext.setText(temp);
//            } else {
//                temptext.setText("Failed to fetch text");
//            }

            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder builder = new StringBuilder();
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
                    data = parseJsonText(data);
                    builder.append(data);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return builder.toString();
        }
//        private String parseJsonText(String json) throws JSONException {
//            JSONObject jsonObject = new JSONObject(json);
//            if (jsonObject.has("list")) {
//                JSONObject listObject = jsonObject.getJSONObject("list");
//
//                // Check if the API response contains the "main" object
//                if (listObject.has("main")) {
//                    JSONObject mainObject = listObject.getJSONObject("main");
//
//                    // Check if the "temp" field is available
//                    if (mainObject.has("temp")) {
//                        double temperature = mainObject.getDouble("temp");
//                        System.out.println(temperature);
//
//                        // Format the temperature as a string and return
//                        return String.format("%.1f°C", temperature);
//                    }
//                }
//            }
        private String parseJsonText(String json) throws JSONException {
            JSONObject jsonObject = new JSONObject(json);
            int numberOfIterations = 8;
            double[] loopDataArray = new double[numberOfIterations];

            if (jsonObject.has("list")) {
                JSONArray listArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listItem = listArray.getJSONObject(i);

                    // Check if the "main" object is available in each list item
                    if (listItem.has("main")) {
                        JSONObject mainObject = listItem.getJSONObject("main");

                        // Check if the "temp" field is available
                        if (mainObject.has("temp")) {
                            double temperature = mainObject.getDouble("temp");
                            System.out.println(String.format("Temperature at index %d: %.1f°C", i, temperature));
                            loopDataArray[i] = temperature;

                        }
                    }
                }
                return String.format("%.1f°C", loopDataArray[0]);
            }
            // Return an error message if the expected data is not found
            return "Failed to parse weather data";
        }
    }
}