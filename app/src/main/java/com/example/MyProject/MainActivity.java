package com.example.MyProject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
    Button reload;


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
        reload = findViewById(R.id.dataload);
        String city = cityname.getText().toString();
        String apikey = "b58e782d8c5a53386137f197053c672a";
        String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";
        String link2 = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=" + apikey + "&units=metric&cnt=4";
        class syncData extends AsyncTask<String, String, List<String[]>> {
            String city = cityname.getText().toString();
            String link2 = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=" + apikey + "&units=metric&cnt=4";
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
                        temptext1.setText(String.format("%.1f°C", Double.parseDouble(loopDataArray[1])));
                        temptext2.setText(String.format("%.1f°C", Double.parseDouble(loopDataArray[2])));
                        temptext3.setText(String.format("%.1f°C", Double.parseDouble(loopDataArray[3])));

                    }
                } else {
                    temptext1.setText("--");
                    temptext2.setText("--");
                    temptext3.setText("--");
                }
            }

            @Override
            public List<String[]> doInBackground(String... strings) {

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
        class syncData1 extends AsyncTask<String, String, String> {
            String city = cityname.getText().toString();
            String link = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey + "&units=metric";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String temp) {
                if (temptext != null) {
                    temptext.setText(temp);
                } else {
                    temptext.setText("Failed to fetch text");
                }

//            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... strings) {

                StringBuilder builder = new StringBuilder();
                try {
                    URL url = new URL(link);
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
            private String parseJsonText(String json) throws JSONException {
                JSONObject jsonObject = new JSONObject(json);
                // Check if the API response contains the "main" object
                if (jsonObject.has("main")) {
                    JSONObject mainObject = jsonObject.getJSONObject("main");

                    // Check if the "temp" field is available
                    if (mainObject.has("temp")) {
                        double temperature = mainObject.getDouble("temp");
                        System.out.println(temperature);

                        // Format the temperature as a string and return
                        return String.format("%.1f°C", temperature);
                    }
                }
                // Return an error message if the expected data is not found
                return "Failed to parse weather data";
            }
        }
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cityname.getText().toString().isEmpty()){
                    new syncData().execute();
                    new syncData1().execute();
                }
                else{
                    temptext.setText("No city");
                    temptext1.setText("--");
                    temptext2.setText("--");
                    temptext3.setText("--");
                }
            }
        }
        );
    }
}

