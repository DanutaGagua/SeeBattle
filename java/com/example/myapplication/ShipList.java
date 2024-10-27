package danuta.gagua.seabattlegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ShipList implements Serializable {
    private ArrayList<Ship> shipList;
    private int maxLength, size;

//    Logger log = Logger.getLogger(AllShips.class.getName());

    ShipList(int maxLength) {
        this.maxLength = maxLength;
        shipList = new ArrayList<>();
    }

    ShipList(int maxLength, int size) {
        this.maxLength = maxLength;
        this.size = size;
        shipList = new ArrayList<>();
    }

    boolean addShip(Ship ship) {
        if (shipList.contains(ship) || shipList.size() == maxLength) {
//            log.info("Ship maxlength");
            return false;
        } else {
//            log.info("Ship added");
            shipList.add(ship);

            return true;
        }
    }

    public void deleteShip(Ship ship) {
        shipList.remove(ship);
    }

    int getShipListNumber() {
        return shipList.size();
    }

    public int getSize() {
        return size;
    }

    void clearShipList() {
        shipList.clear();
    }

    boolean checkShipsEnvironment(Field field){
        for (Ship ship: shipList){
            if (!ship.checkEnviroment(field)){
                return false;
            }
        }
        return true;
    }

    boolean checkCellIsNotInShipList(Cell cell) {
        for (Ship ship: shipList) {
            if (ship.getCells().contains(cell)) {
                return false;
            }
        }
        return true;
    }

    String returnStateFromCell(Cell cell){
        Logger log = Logger.getLogger(AllShips.class.getName());

        for (Ship ship: shipList) {
            if (ship.getCells().contains(cell)) {
                int length = 0;
                for (Cell currentCell: ship.getCells()){
                    if (currentCell.equals(cell)){
                        currentCell.setState("fire");
                    }
                    if (currentCell.getState().equals("fire")){
                        length += 1;
                    }
                }

                if (length == ship.getLength()){
                    for (Cell currentCell: ship.getCells()){
                        if (currentCell.equals(cell)){
                            currentCell.setState("done");
                        }
                    }

                    return "done";
                } else {
                    return "fire";
                }
            }
        }

        return "fire";
    }
}
