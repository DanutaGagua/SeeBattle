package danuta.gagua.seabattlegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_FILE = "Gamer_account";
    private static final String PREF_NAME = "Name";

    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    EditText nameBox;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBox = findViewById(R.id.user_name);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        String name = settings.getString(PREF_NAME,"");
        nameBox.setText(name);
    }

    @Override
    protected void onPause(){
        super.onPause();

        // сохраняем в настройках
        prefEditor = settings.edit();
        prefEditor.putString(PREF_NAME, nameBox.getText().toString());
        prefEditor.apply();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // сохраняем в настройках
        prefEditor = settings.edit();
        prefEditor.putString(PREF_NAME, nameBox.getText().toString());
        prefEditor.apply();
    }

    public void open_play_activity(View view)
    {
        Intent intent = new Intent(this, PrepareToGameActivity.class);
        startActivity(intent);
    }

    public void open_rules_activity(View view)
    {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
}
