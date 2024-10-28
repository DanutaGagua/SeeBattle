package danuta.gagua.seabattlegame;

import java.io.Serializable;
import java.util.ArrayList;

public class ShipList implements Serializable {
    private ArrayList<Ship> shipList;
    private int maxLength, size;

    ShipList(int maxLength, int size) {
        this.maxLength = maxLength;
        this.size = size;
        shipList = new ArrayList<>();
    }

    boolean addShip(Ship ship) {
        if (shipList.contains(ship) || shipList.size() == maxLength) {
            return false;
        } else {
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
            if (!ship.checkEnvironment(field)){
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
                        currentCell.setState("done");
                    }

                    return "done";
                } else {
                    return "fire";
                }
            }
        }

        return "fire";
    }

    public Ship getKilledShip(){
        for (Ship ship: shipList) {
            if (ship.getCells().get(0).getState().equals("done")) {
                return ship;
            }
        }

        return null;
    }
}