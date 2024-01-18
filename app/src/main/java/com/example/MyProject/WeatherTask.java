package com.example.MyProject;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherTask extends AsyncTask<String, Void, String> {

    private final MainActivity1 mainActivity;

    public WeatherTask(MainActivity1 mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        String apiUrl = params[0];
        String result = "";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            result = stringBuilder.toString();

            reader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONObject mainObject = jsonObject.getJSONObject("main");
            double temperature = mainObject.getDouble("temp");

            JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0);
            String description = weatherObject.getString("description");

            mainActivity.onWeatherDataFetched(temperature, description);

        } catch (JSONException e) {
            e.printStackTrace();
            mainActivity.onWeathernoFetched("Wrong City");
        }
    }
}