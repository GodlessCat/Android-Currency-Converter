package com.example.converter.result;

import android.os.AsyncTask;
import org.json.JSONObject;
import static com.example.converter.MainActivity.currentCourse;
import static com.example.converter.MainActivity.input;
import static com.example.converter.MainActivity.output;

class getCoefficientTask extends AsyncTask<String, Void, Double> {
    @Override
    protected Double doInBackground(String... params) {
        String res = params[0];
        try {
            double secondCoefficient = 1.0;
            double firstCoefficient = 1.0;
            JSONObject valuteValue;
            JSONObject apiValue = new JSONObject(res);
            JSONObject valuteName = new JSONObject(apiValue.getString("Valute"));
            if (input != "RUB") {
                valuteValue = new JSONObject(valuteName.getString(input));
                firstCoefficient = valuteValue.getDouble("Value") / valuteValue.getDouble("Nominal");
            }
            if (output != "RUB") {
                valuteValue = new JSONObject(valuteName.getString(output));
                secondCoefficient = valuteValue.getDouble("Value") / valuteValue.getDouble("Nominal");
            }
            currentCourse = firstCoefficient / secondCoefficient;
        }
        catch (Exception ex){
        }
        return currentCourse;
    }
}
