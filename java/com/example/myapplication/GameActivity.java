package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GameActivity extends AppCompatActivity
{
    private int[][] positions = new int[10][10];

    Field field = new Field(positions);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            field = (Field) arguments.getSerializable(Field.class.getSimpleName());
        }
    }

    public void connect(View view)
    {
        EditText editText = (EditText)findViewById(R.id.ip);

        field.IP = editText.getText().toString();

         Intent intent = new Intent(this, GameService.class);
         intent.putExtra(Field.class.getSimpleName(), field);
         startActivity(intent);
    }
}
