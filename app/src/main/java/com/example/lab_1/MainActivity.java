package com.example.lab_1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextNumberDecimal);
        textView = findViewById(R.id.textView2);
    }

    public void onClickButton(View view) {
        String input = editText.getText().toString();

        if (input.isEmpty()) {
            Toast.makeText(this, "Введіть число!", Toast.LENGTH_SHORT).show();
            return;
        }

        int number = Integer.parseInt(input);

        if (number < 50) {
            textView.setText("Більше");
        } else if (number > 50) {
            textView.setText("Менше");
        } else {
            textView.setText("Відгадано!");
        }
    }
}
