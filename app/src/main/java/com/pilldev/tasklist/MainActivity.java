package com.pilldev.tasklist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    SQLiteDatabase db;
    LinearLayout list;
    TextInputEditText input;
    View button;

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

        db = openOrCreateDatabase("TaskListDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)");

        list = findViewById(R.id.list);
        input = findViewById(R.id.input);
        button = findViewById(R.id.button);
        button.setOnClickListener(addListener);

        loadTasks();
    }

    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = Objects.requireNonNull(input.getText()).toString();
            if (!text.isEmpty()) {
                input.setText("");

                ContentValues values = new ContentValues();
                values.put("text", text);
                long taskId = db.insert("tasks", null, values);

                addTaskToUI(taskId, text);
            }
        }
    };

    private void addTaskToUI(long taskId, String text) {
        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textEl = new TextView(this);
        textEl.setText(text);
        textEl.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.delete_ic);
        deleteButton.setBackgroundColor(Color.TRANSPARENT);
        deleteButton.setOnClickListener(v -> {
            db.delete("tasks", "id = ?", new String[]{String.valueOf(taskId)});
            list.removeView(taskLayout);
        });

        taskLayout.addView(textEl);
        taskLayout.addView(deleteButton);

        list.addView(taskLayout);
    }

    private void loadTasks() {
        Cursor cursor = db.rawQuery("SELECT id, text FROM tasks", null);
        while (cursor.moveToNext()) {
            long taskId = cursor.getLong(0);
            String text = cursor.getString(1);
            addTaskToUI(taskId, text);
        }
        cursor.close();
    }
}