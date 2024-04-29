package system;

import components.Cistern;
import components.Spring;
import java.util.List;
import java.util.Random;

public class Map {
    private int rows = 10; // temporary number
    private int columns = 10; // temporary number
    /**
     * This encapsulated attribute represents the matrix of Cell objects that collectively constitute the game map, facilitating comprehensive spatial data management. Initialized by the constructor with the required components in dependence with the chosen map.
     */
    private Cell[][] cells = new Cell[rows][columns];
    private List<Cistern> cisterns;
    private List<Spring> springs;
    private int percentThatSomethingWillSpawn = 0;
    String size;

    public Map(int sizeN, int sizeM){
        rows = sizeN;
        columns = sizeM;
    }

    public void initializeMap(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Random r = new Random();
                int randomNumber = r.nextInt(0,100);
                if (randomNumber < percentThatSomethingWillSpawn){
                    randomNumber = r.nextInt();
                }
            }
        }
    }

    public Cell getUpwardCell(Cell currentCell){
       if (currentCell.row != 0){
           return cells[currentCell.row-1][currentCell.column];
       }
       else return currentCell;
    }
    public Cell getDownwardCell(Cell currentCell){
        if (currentCell.row != rows-1){
            return cells[currentCell.row+1][currentCell.column];
        }
        else return currentCell;
    }
    public Cell getLeftwardCell(Cell currentCell){
        if (currentCell.column != 0){
            return cells[currentCell.row][currentCell.column-1];
        }
        else return currentCell;
    }
    public Cell getRightwardCell(Cell currentCell){
        if (currentCell.column != columns-1){
            return cells[currentCell.row][currentCell.column+1];
        }
        else return currentCell;
    }
    public boolean isNeighbouringCell(Cell cell1, Cell cell2){
        Cell upCell = getUpwardCell(cell1);
        Cell downCell = getDownwardCell(cell1);
        Cell leftCell = getLeftwardCell(cell1);
        Cell rightCell = getRightwardCell(cell1);

        if (cell2.equals(upCell) || cell2.equals(downCell) || cell2.equals(leftCell) || cell2.equals(rightCell)){
            return true;
        }
        else {return false;}
    }
    public void updateWaterFlow(){

    }
    public void checkForFreeEnds(){

    }
    public String getSize() {
        return size;
    }

    public Cell getCells(int row, int column){
        return cells[row][column];
    }
}
