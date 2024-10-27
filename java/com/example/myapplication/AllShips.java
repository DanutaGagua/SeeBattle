package danuta.gagua.seabattlegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AllShips implements Serializable {
    private ShipList ships4;
    private ShipList ships3;
    private ShipList ships2;
    private ShipList ships1;

    private ArrayList<ShipList> allShips;

    private transient Logger log;

    AllShips(){
//        ships1 = new ShipList(4);
//        ships2 = new ShipList(3);
//        ships3 = new ShipList(2);
//        ships4 = new ShipList(1);

        allShips = new ArrayList<>();

        for (int i = 1; i < 5; i++){
            allShips.add(new ShipList(i, 5-i));
        }

        log = Logger.getLogger(AllShips.class.getName());
    }

    void clearShips(){
//        ships4.clearShipList();
//        ships3.clearShipList();
//        ships2.clearShipList();
//        ships1.clearShipList();

        for (ShipList ships: allShips){
            ships.clearShipList();
        }
    }

    boolean addShip(ArrayList<Cell> ship){
//        switch (ship.size()){
//            case 4: log.info("Ship 4"); return ships4.addShip(new Ship(ship));
//            case 3: log.info("Ship 3"); return ships3.addShip(new Ship(ship));
//            case 2: log.info("Ship 2"); return ships2.addShip(new Ship(ship));
//            default: log.info("Ship 1"); return ships1.addShip(new Ship(ship));
//        }

        log = Logger.getLogger(AllShips.class.getName());

        for (ShipList ships: allShips){
            if (ships.getSize() == ship.size()){
                log.info("Ship " + Integer.toString(ships.getSize()));
                return ships.addShip(new Ship(ship));
            }
        }

        return false;
    }

    boolean isNotInShips(Cell cell){
        log = Logger.getLogger(AllShips.class.getName());

//        if (!ships4.checkCellIsNotInShipList(cell)){
//            log.info("Ship in 4");
//            return false;
//        }
//        if (!ships3.checkCellIsNotInShipList(cell)){
//            log.info("Ship in 3");
//            return false;
//        }
//        if (!ships2.checkCellIsNotInShipList(cell)){
//            log.info("Ship in 2");
//            return false;
//        }
//        if (!ships1.checkCellIsNotInShipList(cell)){
//            log.info("Ship in 1");
//            return false;
//        }

        for (ShipList ships: allShips){
            if (!ships.checkCellIsNotInShipList(cell)){
                log.info("Ship in " + Integer.toString(ships.getSize()));
                return false;
            }
        }

        log.info("Ship not in");
        return true;
    }

    int getShipsCount(){
        log = Logger.getLogger(AllShips.class.getName());

        int count = 0;

        for (ShipList ships: allShips){
            count += ships.getShipListNumber();
            log.info(Integer.toString(ships.getShipListNumber()));
        }

        return count;

//        return ships1.getShipListNumber() + ships2.getShipListNumber() +
//                ships3.getShipListNumber() + ships4.getShipListNumber();
    }

    boolean checkShipsEnvironment(Field field){
        log = Logger.getLogger(AllShips.class.getName());
//        if (!ships1.checkShipsEnvironment(field)){
//            return false;
//        }
//        if (!ships2.checkShipsEnvironment(field)){
//            return false;
//        }
//        if (!ships3.checkShipsEnvironment(field)){
//            return false;
//        }
//        return ships4.checkShipsEnvironment(field);

        for (ShipList ships: allShips){
            if (!ships.checkShipsEnvironment(field)){
                log.info("Ship not watered in " + Integer.toString(ships.getSize()));
                return false;
            }
        }

        return true;
    }

    String getStateFromShips(Cell cell){
        log = Logger.getLogger(AllShips.class.getName());
//        if (ships4.checkCellIsNotInShipList(cell)){
//            if (ships3.checkCellIsNotInShipList(cell)){
//                if (ships2.checkCellIsNotInShipList(cell)){
//                    log.info("Cell in ship 1");
//                    return ships1.returnStateFromCell(cell);
//                } else {
//                    log.info("Cell in ship 2");
//                    return ships2.returnStateFromCell(cell);
//                }
//            } else {
//                log.info("Cell in ship 3");
//                return ships3.returnStateFromCell(cell);
//            }
//        } else {
//            log.info("Cell in ship 4");
//            return ships4.returnStateFromCell(cell);
//        }

        for (ShipList ships: allShips){
            if (!ships.checkCellIsNotInShipList(cell)){
                log.info("Ship " + Integer.toString(ships.getSize()) + " " + ships.returnStateFromCell(cell));
                return ships.returnStateFromCell(cell);
            }
        }

        return "none";
    }
}
