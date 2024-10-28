package danuta.gagua.seabattlegame;

import android.content.Context;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

class Field implements Serializable{
    private final int LENGTH = 10;
    private Cell[][] cells = new Cell[LENGTH][LENGTH];
    private transient Logger log;
    private AllShips ships;

    Field(Context context) {
        for (int i = 0; i < LENGTH; i += 1)
        {
            for (int j = 0; j < LENGTH; j += 1)
            {
                this.cells[i][j] = new Cell(context, j, i);
            }
        }

        ships = new AllShips();
    }

    Cell[][] getCells() {
        return cells;
    }

    AllShips getShips() {
        return ships;
    }

    void setFieldButtons(Context context){
        for (int i = 0; i < LENGTH; i += 1)
        {
            for (int j = 0; j < LENGTH; j += 1)
            {
                this.cells[i][j].setButton(context);
            }
        }
    }

    void setChangedFlag(Boolean flag){
        for (int i = 0; i < LENGTH; i += 1)
        {
            for (int j = 0; j < LENGTH; j += 1)
            {
                this.cells[i][j].setChangedFlag(flag);
            }
        }
    }

    boolean checkShips(Player player){
        ships.clearShips();

        for (int i = 0; i < LENGTH; i += 1)        {
            for (int j = 0; j < LENGTH; j += 1)            {
                if (cells[i][j].getState().equals("ship")){
                    if (ships.isNotInShips(cells[i][j])){
                        ArrayList<Cell> temp = new ArrayList<>();

                        temp.add(cells[i][j]);

                        int i1 = i + 1;

                        while (i1 < LENGTH && cells[i1][j].getState().equals("ship")){
                            temp.add(cells[i1][j]);
                            i1 += 1;
                        }

                        if (temp.size() == 1){
                            int j1 = j + 1;

                            while (j1 < LENGTH && cells[i][j1].getState().equals("ship")){
                                temp.add(cells[i][j1]);
                                j1 += 1;
                            }
                        }

                        if (!ships.addShip(temp)){
                            return false;
                        }
                    }

                }
            }
        }

        return ships.getShipsCount() == 10 && ships.checkShipsEnvironment(player.getField());

//        if (ships.getShipsCount() != 10){
//            return false;
//        }
//
//        if (!ships.checkShipsEnvironment(player.getField())){
//            return false;
//        }
//
//        return true;
    }

    Cell getShipCell(){
        for (int i = 0; i < LENGTH; i += 1)        {
            for (int j = 0; j < LENGTH; j += 1)            {
                if (cells[i][j].getState().equals("ship")){
                    return cells[i][j];
                }
            }
        }

        return null;
    }

    String checkSelectedCell(int x, int y) {
        log = Logger.getLogger(Field.class.getName());
        String state = cells[y][x].getState();
        log.info("Cell " + cells[y][x].getState());

        if (state.equals("water")){
            this.cells[y][x].setState("miss");
        } else {
            state = ships.getStateFromShips(cells[y][x]);

            cells[y][x].setState(state);
        }

        log.info("Cell " + cells[y][x].getState());

        return cells[y][x].getState();
    }

    void setEnvironmentOfKilledShip(){
        Ship ship = ships.getKilledShip();

        ship.cleanWater(cells);
    }

    void setEnvironmentOfKilledShip(Ship ship){
        ship.cleanWater(cells);
    }

    public Ship addKilledShip(){
        log = Logger.getLogger(Field.class.getName());

        for (int i = 0; i < LENGTH; i += 1)        {
            for (int j = 0; j < LENGTH; j += 1)            {
                if (cells[i][j].getState().equals("done")){
                    if (ships.isNotInShips(cells[i][j])){
                        log.info("Is not in ship");

                        ArrayList<Cell> temp = new ArrayList<>();

                        temp = checkCellBehind(j, i, temp);

                        if (ships.addShip(temp)){
                            return new Ship(temp);
                        }
                    }

                }
            }
        }

        return null;
    }

    private ArrayList<Cell> checkCellBehind(int x, int y, ArrayList<Cell> temp){
        if (0 <= x && x < 10 && 0 <= y && y < 10){
            if (temp.contains(cells[y][x])){
                return temp;
            }
            if (cells[y][x].getState().equals("fire") ||
                    cells[y][x].getState().equals("done")){
                cells[y][x].setState("done");
                temp.add(cells[y][x]);

                temp = checkCellBehind(x-1, y, temp);
                temp = checkCellBehind(x+1, y, temp);
                temp = checkCellBehind(x, y-1, temp);
                temp = checkCellBehind(x, y+1, temp);
            } else {
                return temp;
            }
        }

        return temp;
    }
}
