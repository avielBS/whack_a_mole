package com.example.whackamole;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGameActivity();
            }
        });


    }

    private void openGameActivity() {
        EditText editText = findViewById(R.id.edit_text_name);
        String name = editText.getText().toString();
        Intent intent = new Intent(this,gameActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }

}
