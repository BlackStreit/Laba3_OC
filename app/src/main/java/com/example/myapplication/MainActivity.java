package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private EditText inputUtf8;
    private Button convToUtf16;
    private EditText resConvUtf816;
    private EditText inputUtf16;
    private Button convToUtf8;
    private EditText resConvtUtf168;

    private Button chooseFile;
    private Button coding;
    private Spinner spinner1;
    private Spinner spinner2;

    private String fileCoding = "";
    private String fileEncoding = "";

    private static final int FILE_SELECT_CODE = 0;
    private static String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        init();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // Развертка с выбором кодировки файла
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.coding);
                fileCoding = choose[selectedItemPosition];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // Развертка с выбором кодировки файла
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.coding);
                fileEncoding = choose[selectedItemPosition];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        chooseFile.setOnClickListener(v -> showFileChooser());

        coding.setOnClickListener(v -> { //Забираем файл по полученному пути и конвертируем его
            File sdPath = Environment.getExternalStorageDirectory();
            try (final FileInputStream fileInputStream    = new FileInputStream(sdPath + "/" +
                    filePath.split(":")[1]);
                 final InputStreamReader inputStreamReader  = new InputStreamReader(fileInputStream, fileCoding);
                 final FileOutputStream fileOutputStream   = new FileOutputStream(sdPath + "/" +
                         filePath.split(":")[1] + fileEncoding + ".txt");
                 final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, fileEncoding))
            {
                final int BUFFER_SIZE = 2048;
                final char[] buffer = new char[BUFFER_SIZE];
                int readed;
                while ((readed = inputStreamReader.read(buffer, 0, BUFFER_SIZE)) > 0)
                    outputStreamWriter.write(buffer, 0, readed);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        convToUtf16.setOnClickListener(v -> { // Конвертация из UTF-8 в UTF-16
            if(inputUtf8.getText().toString().length() == 0){ //Если строка ввода пустая
                Toast.makeText(MainActivity.this, "Текст для конвертации не найден!", Toast.LENGTH_SHORT).show(); // Выводим Тост, если текст не введен
                return;
            }

            String textInUtf16 = inputUtf8.getText().toString(); //Считываем текст в переменную
            try {
                byte[] utf8 = textInUtf16.getBytes("UTF-8"); //Записываем в байтовый массив с кодировкой utf-8
                String textInUtf8 = new String(utf8, "UTF-8"); //записываем в строку с кодировкой utf-8
                byte[] utf16 = textInUtf8.getBytes("UTF-16"); //записываем в байтовый массив с кодировкой utf-16

                resConvUtf816.setText(new String(utf16, "UTF-16"));//выводим массив
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        convToUtf8.setOnClickListener(v -> { // Конвертация из UTF-16 в UTF-8
            if(inputUtf16.getText().toString().length() == 0){ //Если строка ввода пустая
                Toast.makeText(MainActivity.this, "Текст для конвертации не найден!", Toast.LENGTH_SHORT).show(); // Выводим Тост, если текст не введен
                return;
            }

            String textInUtf16 = inputUtf16.getText().toString(); //Считываем текст в переменную
            try{
                byte[] utf8 = textInUtf16.getBytes("UTF-8");//записываем в строку с кодировкой utf-16
                String textInUtf8 = new String(utf8, "UTF-8"); //записываем в байтовый массив с кодировкой utf-8
                resConvtUtf168.setText(textInUtf8); //выводим массив
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    private void showFileChooser() { //Открытие файлового менеджера
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Выбор файла"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Требуется установка файлового менеджера!",
                    Toast.LENGTH_SHORT).show();
            filePath = "";
        }
    }

    private void init(){
        inputUtf8 = (EditText) findViewById(R.id.inputUtf8);
        convToUtf16 = (Button) findViewById(R.id.convertToUtf16);
        resConvUtf816 = (EditText) findViewById(R.id.resultConvertUtf816);
        inputUtf16 = (EditText) findViewById(R.id.inputUtf16);
        convToUtf8 = (Button) findViewById(R.id.convertToUtf8);
        resConvtUtf168 = (EditText) findViewById(R.id.resultConvertUtf168);

        chooseFile = (Button) findViewById(R.id.chooseFile);
        coding = (Button) findViewById(R.id.coding);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
    }
}