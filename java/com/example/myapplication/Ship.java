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

    boolean checkEnviroment(Field field){
        for (Cell cell: cells){
            int x = cell.getX();
            int y = cell.getY();
//            log.info("Ship " + Integer.toString(x) + " " + Integer.toString(y));
            if (!checkCell(x-1, y, field.getCells())){
                return false;
            }
            if (!checkCell(x+1, y, field.getCells())){
                return false;
            }
            if (!checkCell(x, y-1, field.getCells())){
                return false;
            }
            if (!checkCell(x, y+1, field.getCells())){
                return false;
            }
            if (!checkCell(x-1, y-1, field.getCells())){
                return false;
            }
            if (!checkCell(x-1, y+1, field.getCells())){
                return false;
            }
            if (!checkCell(x+1, y-1, field.getCells())){
                return false;
            }
            if (!checkCell(x+1, y+1, field.getCells())){
                return false;
            }
        }

        return true;
    }

    boolean checkCell(int x, int y, Cell[][] cells){
        if (0 <= x && x < 10 && 0 <= y && y < 10){
//            log.info("Ship " + Integer.toString(x) + " " + Integer.toString(y));
            if (this.cells.contains(cells[y][x])){
//                log.info("Ship contains cell");
                return true;
            } else {
//                log.info("Ship contains " + Boolean.toString(cells[y][x].getState() != "ship"));
                return cells[y][x].getState() != "ship";
            }
        } else {
            return true;
        }
    }

    int getLength() {
        return length;
    }
}
