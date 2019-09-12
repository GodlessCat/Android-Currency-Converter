package com.example.converter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.converter.history.HistoryActivity;
import com.example.converter.result.GetJsonStringTask;
import com.example.converter.result.GetResultString;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static String urlContent = " ";
    public static TextView convertResult;
    private InputMethodManager imm;
    static TextView course;
    private Button dateBtn;
    public static EditText enterAmount;
    public static String input;
    public static String output;
    GetJsonStringTask getJsonStringTask;
    public static Double currentCourse = 1.0;
    private ArrayList currencies = new ArrayList();
    public static Calendar calendar = Calendar.getInstance();
    private Calendar minDate = Calendar.getInstance();
    private Calendar maxDate = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    public static SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    public static NumberFormat formatter = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button convertBtn = findViewById(R.id.Button);
        Button swapBtn = findViewById(R.id.swap);
        dateBtn = findViewById(R.id.DATE);
        Button historyBtn = findViewById(R.id.History);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        minDate.set(1999, 0, 1);
        convertResult = findViewById(R.id.RESULT);
        course = findViewById(R.id.Course);
        course.setText("Курс на " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR) + " -> " + formatter.format(currentCourse));
        enterAmount = findViewById(R.id.INPUT);

        setInitialDate();
        setCurrencies();

        enterAmount.requestFocus();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getJsonStringTask = new GetJsonStringTask();
        getJsonStringTask.execute();
        try {
            urlContent = getJsonStringTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (enterAmount.getText().toString().length() == 0){
                        convertResult.setText("Сумма не введена");
                    } else {
                        convertResult.setText(GetResultString.getConvertResult());
                        enterAmount.setText("");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        swapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buff = input;
                input = output;
                output = buff;
                changeSpiners(R.id.inputSpinner, R.id.outputSpinner);
                try {
                    course.setText(GetResultString.getCourse());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showKeyboard(enterAmount);
    }

    public void showKeyboard(View view) {
        imm.toggleSoftInput(0, 0);
    }

    private void setInitialDate() {
        dateBtn.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    private void setDate(View v) {
        datePickerDialog = new DatePickerDialog(MainActivity.this, dialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener dialog = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int yearC, int monthOfYearC, int dayOfMonthC) {
            calendar.set(Calendar.YEAR, yearC);
            calendar.set(Calendar.MONTH, monthOfYearC);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonthC);
            setInitialDate();
            GetJsonStringTask getJsonStringTask = new GetJsonStringTask();
            getJsonStringTask.execute();

            try {
                urlContent = getJsonStringTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                course.setText(GetResultString.getCourse());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            convertResult.setText("0");
        }
    };

    private void setSpinner(int id) {
        Spinner spinner = findViewById(id);

        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, currencies);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                if (parent.getId() == R.id.inputSpinner) {
                    input = item;
                }
                else {
                    output = item;
                }
                try {
                    course.setText(GetResultString.getCourse());
                    ((TextView) parent.getChildAt(0)).setTextColor(0xFFFFFFFF);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void changeSpiners(int id1, int id2){
        Spinner spinner1 = findViewById(id1);
        Spinner spinner2 = findViewById(id2);
        int positionSpinner1 = spinner1.getSelectedItemPosition() ;
        int positionSpinner2 = spinner2.getSelectedItemPosition() ;
        spinner1.setSelection(positionSpinner2);
        spinner2.setSelection(positionSpinner1);
    }

    private void setCurrencies(){
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("RUB");
        currencies.add("JPY");
        setSpinner(R.id.inputSpinner);
        setSpinner(R.id.outputSpinner);
    }
}