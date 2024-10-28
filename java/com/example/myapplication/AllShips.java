package danuta.gagua.seabattlegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AllShips implements Serializable {
    private ArrayList<ShipList> allShips;

    private transient Logger log;

    AllShips(){
        allShips = new ArrayList<>();

        for (int i = 1; i < 5; i++){
            allShips.add(new ShipList(i, 5-i));
        }
    }

    void clearShips(){
        for (ShipList ships: allShips){
            ships.clearShipList();
        }
    }

    boolean addShip(ArrayList<Cell> ship){
        log = Logger.getLogger(AllShips.class.getName());
        for (ShipList ships: allShips){
            if (ships.getSize() == ship.size()){
                log.info("Add ship " + Integer.toString(ships.getSize()));
                return ships.addShip(new Ship(ship));
            }
        }

        return false;
    }

    boolean isNotInShips(Cell cell){
        for (ShipList ships: allShips){
            if (!ships.checkCellIsNotInShipList(cell)){
                return false;
            }
        }

        return true;
    }

    int getShipsCount(){
        int count = 0;

        for (ShipList ships: allShips){
            count += ships.getShipListNumber();
        }

        return count;
    }

    boolean checkShipsEnvironment(Field field){
        for (ShipList ships: allShips){
            if (!ships.checkShipsEnvironment(field)){
                return false;
            }
        }

        return true;
    }

    String getStateFromShips(Cell cell){
        for (ShipList ships: allShips){
            if (!ships.checkCellIsNotInShipList(cell)){
                return ships.returnStateFromCell(cell);
            }
        }

        return "none";
    }

    public Ship getKilledShip(){
        for(ShipList ships: allShips){
            if (ships.getKilledShip() != null){
                Ship ship = ships.getKilledShip();

                ships.deleteShip(ship);

                return ship;
            }
        }

        return null;
    }
}
