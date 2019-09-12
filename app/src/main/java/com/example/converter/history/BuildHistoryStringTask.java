package com.example.converter.history;

import android.os.AsyncTask;
import java.util.Stack;

public class BuildHistoryStringTask extends AsyncTask<Stack<String>, Void, String> {
    private String historyString = " ";
    @Override
    protected String doInBackground(Stack<String>... stacks) {
        Stack<String> text = (Stack<String>) stacks[0].clone();
        if (text.empty()){ historyString = "Операции отсутствуют"; }
        while (!text.empty()){ historyString += text.pop() + "\n"; }
        return historyString;
    }
}
