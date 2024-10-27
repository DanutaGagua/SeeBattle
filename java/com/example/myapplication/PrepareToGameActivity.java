package danuta.gagua.seabattlegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class PrepareToGameActivity extends AppCompatActivity{
    Player player;

    private static final String PREFS_FILE = "Gamer_account";
    private static final String PREF_NAME = "Name";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_to_game);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        player = new Player(getApplicationContext(), settings.getString(PREF_NAME,""));

        buildGameField();
    }

    public void buildGameField(){
        TableLayout tableLayout = findViewById(R.id.field_view);

        for (int i = 0; i < 10; i++ ) {
            TableRow row = new TableRow(this); // создание строки таблицы
            for (int j = 0; j < 10; j++)
            {
                row.addView(player.getField().getCells()[i][j].getButton(),
                        new TableRow.LayoutParams(100,100));// добавление кнопки в строку таблицы
            }
            tableLayout.addView(row,
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT)); // добавление строки в таблицу
        }
    }

    public void start_game(View view)
    {
        if (!player.getField().checkShips(player)){
            Toast.makeText(this, "Неправильно расставлены корабли.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, GameActivity.class);
//            intent.putExtra(Field.class.getSimpleName(), player.getField());
            intent.putExtra(Player.class.getSimpleName(), player);
            startActivity(intent);
        }
    }
}


