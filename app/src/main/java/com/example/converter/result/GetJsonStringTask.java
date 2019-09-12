package com.example.converter.result;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import static com.example.converter.MainActivity.calendar;
import static com.example.converter.MainActivity.dayFormat;
import static com.example.converter.MainActivity.monthFormat;
import static com.example.converter.MainActivity.convertResult;
import static com.example.converter.MainActivity.yearFormat;

public class GetJsonStringTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        String content = " ";
        String year = yearFormat.format(calendar.getTime());
        String month = monthFormat.format(calendar.getTime());
        String day = dayFormat.format(calendar.getTime());
        JSONObject apiValue;
        try{
            content = getContent("https://www.cbr-xml-daily.ru/archive/" + year + "/" + month + "/" + day + "/daily_json.js");
            System.out.println(year + " " + month + " " + day);
            apiValue = new JSONObject(content);
            while (apiValue.getInt("code") == 404){
                calendar.add(calendar.DAY_OF_MONTH, -1);
                year = yearFormat.format(calendar.getTime());
                month = monthFormat.format(calendar.getTime());
                day = dayFormat.format(calendar.getTime());
                content = getContent("https://www.cbr-xml-daily.ru/archive/" + year + "/" + month + "/" + day + "/daily_json.js");
                apiValue = new JSONObject(content);
            }
        }
        catch (IOException ex){
            convertResult.setText("Нет соединения");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return content;
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            if (connection.getResponseCode() == 404){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}