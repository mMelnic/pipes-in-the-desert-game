package system;

import components.Cistern;
import components.Spring;

import java.util.List;

public class Map {
    int rows = 10;//temporary number
    int columns; // temporary number
    Cell[][] cells = new Cell[rows][columns];
    List<Cistern> cisterns;
    List<Spring> springs;
    String size;

    public void initializeMap(){

    }

    public Cell getUpwardCell(Cell currentCell){

    }
    public Cell getDownwardCell(Cell currentCell){

    }
    public Cell getLeftwardCell(Cell currentCell){

    }
    public Cell getRightwardCell(Cell currentCell){

    }

    public String getSize() {
        return size;
    }
}
