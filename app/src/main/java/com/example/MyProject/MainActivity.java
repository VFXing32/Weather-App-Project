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

    public void onclick(View v){
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
        FetchTextTask fetchTextTask = new FetchTextTask();
        fetchTextTask.execute();
    }

    public void get(View v){
        String apikey = "b58e782d8c5a53386137f197053c672a";
        String city = "Karachi";
        // Use cityname.getText().toString() if you want to get the city dynamically
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey+"&units=metric";
    }

    private class FetchTextTask extends AsyncTask<Void, Void,String> {
        protected String doInBackground(Void... voids){
            String apikey = "b58e782d8c5a53386137f197053c672a";
            String city = "karachi";
            String text = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey+"&units=metric");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder buffer = new StringBuilder();
                if (inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0){
                    return null;
                }
                text = parseJsonText(buffer.toString());

            }
            catch (IOException | JSONException e){
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader!= null){
                    try{
                        reader.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            return text;
        }

        @Override
        protected void onPostExecute(String temp){
            if (temptext != null){
                temptext.setText(temp);
            }
            else{
                temptext.setText("Failed to fetch text");
            }
        }
    }

    private String parseJsonText(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        // Check if the API response contains the "main" object
        if (jsonObject.has("main")) {
            JSONObject mainObject = jsonObject.getJSONObject("main");

            // Check if the "temp" field is available
            if (mainObject.has("temp")) {
                double temperature = mainObject.getDouble("temp");

                // Format the temperature as a string and return
                return String.format("%.1f°C", temperature);
            }
        }

        // Return an error message if the expected data is not found
        return "Failed to parse weather data";
    }
}
