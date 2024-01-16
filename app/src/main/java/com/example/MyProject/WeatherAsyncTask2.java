package com.example.MyProject;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAsyncTask2 extends AsyncTask<String, Void, String> {

    private WeatherCallback callback;

    public WeatherAsyncTask2(WeatherCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String apiUrl = params[0];
        String jsonResponse = "";

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

            jsonResponse = stringBuilder.toString();

            reader.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        if (jsonResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray listArray = jsonObject.getJSONArray("list");

                double[] tempValues = new double[3];
                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listItem = listArray.getJSONObject(i);
                    JSONObject mainObject = listItem.getJSONObject("main");
                    double temp = mainObject.getDouble("temp");
                    tempValues[i] = temp;
                }
                callback.onWeatherDataFetched2(tempValues[0], tempValues[1], tempValues[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface WeatherCallback {
        void onWeatherDataFetched2(double temp1, double temp2, double temp3);
    }
}
