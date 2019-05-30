package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PrepareToGameActivity extends AppCompatActivity implements OnTouchListener {

    private int[][] positions = new int[10][10] ;

    float x;
    float y;

    int number_1_palub_ships = 0;
    int number_2_palub_ships = 0;
    int number_3_palub_ships = 0;
    int number_4_palub_ships = 0;
    int number_ships = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_to_game);

        draw_field();
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            x = event.getX();
            y = event.getY();

            if (x < 80 || x > 680 || y < 80 || y > 680)
            {
                return true;
            }

            int j = getIndex(x);
            int i = getIndex(y);

            if (positions[i][j] == 0)
            {
                positions[i][j] = 6;
            }
            else
                {
                positions[i][j] = 0;
            }

            draw_field();
        }

        return true;
    }

    private int getIndex(float x)
    {
        int i = (int) x;

        i = (i - 80) / 60;

        return i;
    }

    public void start_game(View view)
    {
        if (!check_ships()){

            Toast.makeText(this, "Неправильно расставлены корабли.", Toast.LENGTH_SHORT).show();

            clear_data();
        }
        else
        {
            if (number_1_palub_ships == 4 && number_2_palub_ships == 3 && number_3_palub_ships == 2 && number_4_palub_ships == 1)
            {
                Field field = new Field(positions);

                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(Field.class.getSimpleName(), field);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Неправильно расставлены корабли.", Toast.LENGTH_SHORT).show();

                clear_data();
            }

        }
    }

    private boolean check_ships()
    {
        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                if (positions[i][j] == 6)
                {
                    int flag4 = check_4_ship(i, j);

                    if (flag4 == 2)
                    {
                        return false;
                    }
                    else
                    {
                        if (flag4 == 1) continue;
                    }

                    int flag3 = check_3_ship(i, j);

                    if (flag3 == 2)
                    {
                        return false;
                    }
                    else
                    {
                        if (flag3 == 1) continue;
                    }

                    int flag2 = check_2_ship(i, j);

                    if (flag2 == 2)
                    {
                        return false;
                    }
                    else
                    {
                        if (flag2 == 1) continue;
                    }

                    if (check_enviroment(i, j))
                    {
                        positions[i][j] = 1;

                        number_1_palub_ships++;
                        number_ships++;

                    } else return false;
                }
            }
        }

        return true;
    }

    private int check_4_ship (int i, int j)
    {
        boolean flag = false;
        int  i2 = 0, i3 = 0, i4 = 0,  j2 = 0, j3 = 0, j4 = 0;

        if (i <= 6)
         {
            if (positions[i+1][j] == 6)
            {
                i2 = i+1;
                j2 = j;

                if (positions[i+2][j] == 6)
                {
                    i3 = i+2;
                    j3 = j;

                    if (positions[i+3][j] == 6)
                    {
                        i4 = i+3;
                        j4 = j;

                        flag = true;
                    }
                }
            }
         }

         if (!flag)
        {
            if (j <= 6)
            {
                if (positions[i][j+1] == 6)
                {
                    i2 = i;
                    j2 = j+1;

                    if (positions[i][j+2] == 6)
                    {
                        i3 = i;
                        j3 = j+2;

                        if (positions[i][j+3] == 6)
                        {
                            i4 = i;
                            j4 = j+3;

                            flag =  true;
                        }
                    }
                }
            }
        }

        if (flag)
        {
            positions[i][j] = 4;
            positions[i2][j2] = 4;
            positions[i3][j3] = 4;
            positions[i4][j4] = 4;

            if (check_enviroment(i, j) && check_enviroment(i2, j2) && check_enviroment(i3, j3) && check_enviroment(i4, j4))
            {
                number_4_palub_ships++;
                number_ships++;

                return 1;
            }
            else return 2;
        }
        else return 0;
    }

    private int check_3_ship (int i, int j)
    {
        boolean flag = false;
        int  i2 = 0, i3 = 0, j2 = 0, j3 =  0;

        if (i <= 7)
        {
            if (positions[i+1][j] == 6)
            {
                i2 = i+1;
                j2 = j;

                if (positions[i+2][j] == 6)
                {
                    i3 = i+2;
                    j3 = j;

                    flag = true;
                }
            }
        }

        if (!flag)
        {
            if (j <= 7)
            {
                if (positions[i][j+1] == 6)
                {
                    i2 = i;
                    j2 = j+1;

                    if (positions[i][j+2] == 6)
                    {
                        i3 = i;
                        j3 = j+2;

                        flag =  true;
                    }
                }
            }
            else flag =  false;
        }

        if (flag)
        {
            positions[i][j] = 3;
            positions[i2][j2] = 3;
            positions[i3][j3] = 3;

            if (check_enviroment(i, j) && check_enviroment(i2, j2) && check_enviroment(i3, j3))
            {
                number_3_palub_ships++;
                number_ships++;

                return 1;
            }
            else return 2;
        }
        else return 0;
    }

    private int check_2_ship (int i, int j)
    {
        boolean flag = false;
        int  i2 = 0,  j2 = 0 ;

        if (i <= 8)
        {
            if (positions[i+1][j] == 6)
            {
                i2 = i+1;
                j2 = j;

                flag = true;
            }
        }

        if (!flag)
        {
            if (j <= 8)
            {
                if (positions[i][j+1] == 6)
                {
                    i2 = i;
                    j2 = j+1;

                    flag = true;
                }
            }
            else flag =  false;
        }

        if (flag)
        {
            positions[i][j] = 2;
            positions[i2][j2] = 2;

            if (check_enviroment(i, j) && check_enviroment(i2, j2))
            {
                number_2_palub_ships++;
                number_ships++;

                return 1;
            }
            else return 2;
        }
        else return 0;
    }

    private boolean check_enviroment(int i, int j)
    {
        if (i-1 > -1 && i-1 < 10 && j-1 > -1 && j-1 < 10 )
        {
            if (positions[i-1][j-1] == 0 || positions[i-1][j-1] != 6) {
                positions[i - 1][j - 1] = 0;
            }
            else return false;
        }

        if (i-1 > -1 && i-1 < 10 ) {
            if (positions[i-1][j] == 0 || positions[i-1][j] != 6) positions[i - 1][j] = 0;
            else   return false;
        }

        if (j-1 > -1 && j-1 < 10 )
        {
            if (positions[i][j-1] == 0 || positions[i][j-1] != 6) positions[i][j-1] = 0;
            else   return false;
        }

        if (i-1 > -1 && i-1 < 10 && j+1 > -1 && j+1 < 10 )
        {
            if ( positions[i-1][j+1] == 0 || positions[i-1][j+1] != 6) positions[i-1][j+1] = 0;
            else  return false;
        }

        if (i+1 > -1 && i+1 < 10 && j-1 > -1 && j-1 < 10 )
        {
            if (positions[i+1][j-1] == 0 || positions[i+1][j-1] != 6) positions[i+1][j-1] = 0;
            else   return false;
        }

        if (j+1 > -1 && j+1 < 10 )
        {
            if (positions[i][j+1] == 0 || positions[i][j+1] != 6) positions[i][j+1] = 0;
            else return false;
        }

        if (i+1 > -1 && i+1 < 10 )
        {
            if (positions[i+1][j] == 0 || positions[i+1][j] != 6) positions[i+1][j] = 0;
            else return false;
        }

        if (i+1 > -1 && i+1 < 10 && j+1 > -1 && j+1 < 10 )
        {
            if (positions[i+1][j+1] == 0 || positions[i+1][j+1] != 6) positions[i+1][j+1] = 0;
            else return false;
        }

        return true;
    }

    private void clear_data()
    {
        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                positions[i][j] = 0;
            }
        }

        number_ships = 0;
        number_1_palub_ships = 0;
        number_2_palub_ships = 0;
        number_3_palub_ships = 0;
        number_4_palub_ships  = 0;

         FieldView field = new FieldView(this, positions);
        field.setOnTouchListener(this);

        LinearLayout layout = (LinearLayout)findViewById(R.id.prepare_view) ;
        layout.removeAllViews();

        layout.addView(field);
    }

    private void draw_field()
    {
        FieldView field = new FieldView(this, this.positions);
        field.setOnTouchListener(this);

        LinearLayout layout = (LinearLayout)findViewById(R.id.prepare_view) ;

        layout.removeAllViews();

        layout.addView(field);
    }
}


