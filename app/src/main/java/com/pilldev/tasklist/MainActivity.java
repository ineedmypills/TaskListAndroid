package com.pilldev.tasklist;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    LinearLayout list;
    TextInputEditText input;
    View button;

    View delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list = findViewById(R.id.list);
        input = findViewById(R.id.input);
        button = findViewById(R.id.button);
        button.setOnClickListener(addListener);
        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(deleteListener);
    }

    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = Objects.requireNonNull(input.getText()).toString();
            if (!text.isEmpty()) {
                input.setText("");

                TextView textEl = new TextView(MainActivity.this);
                textEl.setText(text);

                list.addView(textEl);
            }
        }
    };

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                list.removeView(v);
        }
    };
}