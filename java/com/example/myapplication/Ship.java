package danuta.gagua.seabattlegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Ship implements Serializable {
    private int length;
    private ArrayList<Cell> cells;
//    Logger log = Logger.getLogger(AllShips.class.getName());

    Ship (ArrayList<Cell> cells){
        this.length = cells.size();
        this.cells = cells;
    }

    ArrayList<Cell> getCells() {
        return cells;
    }

    boolean checkEnvironment(Field field){
        for (Cell cell: cells){
            int x = cell.getX();
            int y = cell.getY();

            for (int i = -1; i < 2; i++){
                for (int j = -1; j < 2; j++){
                    if (!checkCell(x+j, y+i, field.getCells())){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkCell(int x, int y, Cell[][] cells){
        if (0 <= x && x < 10 && 0 <= y && y < 10){
            if (this.cells.contains(cells[y][x])){
                return true;
            } else {
                return cells[y][x].getState() != "ship";
            }
        } else {
            return true;
        }
    }

    int getLength() {
        return length;
    }

    public void cleanWater(Cell[][] cells){
        for (Cell cell: this.cells){
            int x = cell.getX();
            int y = cell.getY();

            for (int i = -1; i < 2; i++){
                for (int j = -1; j < 2; j++) {
                    if (0 <= x + j && x + j < 10 && 0 <= y + i && y + i < 10){
                        if (cells[y + i][x + j].getState().equals("water")) {
                            cells[y + i][x + j].setState("miss");
                        }
                    }
                }
            }
        }
    }
}
