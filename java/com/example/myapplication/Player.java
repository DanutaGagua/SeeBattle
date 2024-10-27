package danuta.gagua.seabattlegame;

import android.content.Context;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private Field field;
    private String IP;

    Player(Context context, String name)
    {
        this.name = name;
        field = new Field(context);
    }

    Player(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getIP() {
        return IP;
    }

    void setIP(String IP) {
        this.IP = IP;
    }

    Field getField() {
        return field;
    }

    void setField(Context context) {
        this.field = new Field(context);
    }
}
