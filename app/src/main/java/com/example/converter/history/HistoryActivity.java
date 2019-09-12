package com.example.converter.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.converter.R;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {

    private String historyString = " ";
    private static TextView historyView;
    public static Stack<String> history = new Stack<>();
    BuildHistoryStringTask buildHistoryStringTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyView = findViewById(R.id.historyText);
        Stack<String> text = (Stack<String>) history.clone();

        buildHistoryStringTask = new BuildHistoryStringTask();
        buildHistoryStringTask.execute(text);
        try {
            historyString = buildHistoryStringTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        historyView.setText(historyString);
    }
}
