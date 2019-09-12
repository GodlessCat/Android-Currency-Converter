package com.example.converter.result;

import com.example.converter.MainActivity;
import java.util.concurrent.ExecutionException;
import static com.example.converter.history.HistoryActivity.history;
import static com.example.converter.MainActivity.calendar;
import static com.example.converter.MainActivity.currentCourse;
import static com.example.converter.MainActivity.dayFormat;
import static com.example.converter.MainActivity.formatter;
import static com.example.converter.MainActivity.input;
import static com.example.converter.MainActivity.monthFormat;
import static com.example.converter.MainActivity.output;
import static com.example.converter.MainActivity.urlContent;
import static com.example.converter.MainActivity.yearFormat;

public class GetResultString {
    private static String year;
    private static String month;
    private static String day;
    static getCoefficientTask getCoefficientTask;


    static public String getConvertResult() throws ExecutionException, InterruptedException {
        setTime();
        getCoefficientTask = new getCoefficientTask();
        getCoefficientTask.execute(urlContent);
        currentCourse = getCoefficientTask.get();
        String txt = "" + formatter.format(Double.valueOf(MainActivity.enterAmount.getText().toString())) + " " +  input + " = "
                + formatter.format(Double.valueOf(MainActivity.enterAmount.getText().toString()) * currentCourse) + " " + output;
        history.push(txt + "\n(" + day + "/" + month + "/" + year + ")\n");
        return txt;
    }

    static public String getCourse() throws ExecutionException, InterruptedException {
        setTime();
        getCoefficientTask getCoefficientTask = new getCoefficientTask();
        getCoefficientTask.execute(urlContent);
        currentCourse = getCoefficientTask.get();
        String crs = "Курс на " + day + "/" + month + "/" + year + " -> " + formatter.format(currentCourse);
        return crs;
    }

    private static void setTime(){
        year = yearFormat.format(calendar.getTime());
        month = monthFormat.format(calendar.getTime());
        day = dayFormat.format(calendar.getTime());
    }
}
