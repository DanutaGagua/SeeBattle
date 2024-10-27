package danuta.gagua.seabattlegame;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

class Cell implements Serializable {
    private transient Button button;
    private String state = "water";
    private boolean isChangedFlag = true;
    private int x, y;

    Cell(Context context, int x, int y){
        button = new Button(context);
        button.setBackgroundResource(R.drawable.water);
        button.setOnClickListener(new Listener(x, y));

        this.x = x;
        this.y = y;
    }

    private void changeCellBackground(){
        switch (state){
            case "water": button.setBackgroundResource(R.drawable.water); break;
            case "fire": button.setBackgroundResource(R.drawable.fire); break;
            case "miss": button.setBackgroundResource(R.drawable.miss); break;
            case "done": button.setBackgroundResource(R.drawable.well); break;
            case "ship": button.setBackgroundResource(R.drawable.ship); break;
        }
    }

    void setChangedFlag(boolean changedFlag) {
        isChangedFlag = changedFlag;
    }

    void setButton(Context context) {
        button = new Button(context);
        changeCellBackground();
        button.setOnClickListener(new Listener(x, y));
    }

    Button getButton() {
        return button;
    }

    String getState() {
        return state;
    }

    void setState (String state) {
        this.state = state;
        changeCellBackground();
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    private class Listener implements View.OnClickListener {
        private int x = 0;
        private int y = 0;

        Listener(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public void onClick(View view)
        {
            if (isChangedFlag){
                switch (state){
                    case "water": setState("ship"); break;
                    case "ship": setState("water"); break;
                }
            }
        }
    }
}
