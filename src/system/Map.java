package system;

import components.Cistern;
import components.Pipe;
import components.Pump;
import components.Spring;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import player.MovablePlayer;

public class Map {
    private int rows = 8; // temporary number
    private int columns = 8; // temporary number
    /**
     * This encapsulated attribute represents the matrix of Cell objects that collectively constitute the game map, facilitating comprehensive spatial data management. Initialized by the constructor with the required components in dependence with the chosen map.
     */
    private Cell[][] cells = new Cell[rows][columns];
    private List<Cistern> cisterns = new ArrayList<Cistern>();
    private List<Spring> springs = new ArrayList<Spring>();
    private int numberOfCisterns = 0; // must be a multiple of 4
    private int numberOfSprings = numberOfCisterns / 4;

    public List<MovablePlayer> players = new ArrayList<MovablePlayer>();


    String size;

    public Map(int sizeN, int sizeM){
        // rows = sizeN;
        // columns = sizeM;

        for (int i = 0; i < cells.length; i++)
        {
            for (int j = 0; j < cells[i].length; j++) 
            {
                cells[i][j] = new Cell();
                cells[i][j].map = this;
            }
        }
    }

    public void initializeMap(){

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (j == rows-1){
                    Cistern newCistern = new Cistern();
                    cells[i][j].placeComponent(newCistern);
                    cisterns.add(newCistern);
                }
                else if (i == 2 && j == 2 || i == 5 && j == 6){
                    Spring newSpring = new Spring();
                    cells[i][j].placeComponent(newSpring);
                    springs.add(newSpring);
                }
                else if (i == 0 || j == columns -1){
                    if (j != 0){
                        Pipe newPipe = new Pipe();
                        cells[i][j].placeComponent(newPipe);
                    } else if (i != rows -1) {
                        Pipe newPipe = new Pipe();
                        cells[i][j].placeComponent(newPipe);
                    }

                }
            }
        }
    }

    public Cell getUpwardCell(Cell currentCell){
        int row = -1;
        int col = -1;

        // Find the coordinates of the given cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == currentCell) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }

        // Check if the cell has an upward neighbor
        if (row > 0) {
            return cells[row - 1][col];
        } else {
            // Return null if no upward neighbor or cell not found
            return null;
        }
    }
    public Cell getDownwardCell(Cell currentCell){
        int row = -1;
        int col = -1;

        // Find the coordinates of the given cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == currentCell) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }

        // Check if the cell has a downward neighbor
        if (row < cells.length - 1) {
            return cells[row + 1][col];
        } else {
            // Return null if no downward neighbor or cell not found
            return null;
        }
    }
    
    public Cell getLeftwardCell(Cell currentCell) {
        int row = -1;
        int col = -1;

        // Find the coordinates of the given cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == currentCell) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }

        // Check if the cell has a left neighbor
        if (row != -1 && col > 0) {
            return cells[row][col - 1];
        } else {
            // Return null if no left neighbor or cell not found
            return null;
        }
    }

    public Cell getRightwardCell(Cell currentCell){
        int row = -1;
        int col = -1;

        // Find the coordinates of the given cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == currentCell) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) {
                break;
            }
        }

        // Check if the cell has a right neighbor
        if (row != -1 && col < cells[row].length - 1) {
            return cells[row][col + 1];
        } else {
            // Return null if no right neighbor or cell not found
            return null;
        }
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
    public List<Cell> getNeighbouringCells(Cell cell){
        List<Cell> returnList = new ArrayList<>();
        returnList.add(getDownwardCell(cell));
        returnList.add(getUpwardCell(cell));
        returnList.add(getLeftwardCell(cell));
        returnList.add(getRightwardCell(cell));
        return returnList;
    }
    public void updateWaterFlow(){
        List<Cell> queue = findSprings();
        List<Cell> visitedCells = new ArrayList<>();

        while(!queue.isEmpty()){
            List<Cell> neighbouringCells = getNeighbouringCells(queue.get(0));
            visitedCells.add(queue.get(0));
            for (int i = 0; i < neighbouringCells.size(); i++) {
                if (neighbouringCells.get(i).getComponent() instanceof Pipe){
                    queue.add(neighbouringCells.get(i));
                    visitedCells.add(neighbouringCells.get(i));
                    Pipe pipe = (Pipe) cells[neighbouringCells.get(i).row][neighbouringCells.get(i).column].getComponent();
                    pipe.setWaterFlowing(true);
                } else if (neighbouringCells.get(i).getComponent() instanceof Pump) {
                    queue.add(neighbouringCells.get(i));
                    visitedCells.add(neighbouringCells.get(i));
                }
            }
            queue.remove(0);
        }
    }
//    public void checkForFreeEnds(){
//
//    }
    public String getSize() {
        return size;
    }

    public void draw(){
        printMap();
        outputMap();
    }

    private void printMap(){
        System.out.println("c - cistern; p - pipe; x - pump; s - spring");

        for (int i = 0; i < columns; i++) {
            System.out.println("_");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isEmpty){
                    System.out.println("| ");
                } else if (cells[i][j].getComponent() instanceof Cistern) {
                    System.out.println("|c");
                }else if (cells[i][j].getComponent() instanceof Pipe) {
                    System.out.println("|p");
                }else if (cells[i][j].getComponent() instanceof Pump) {
                    System.out.println("|x");
                }else if (cells[i][j].getComponent() instanceof Spring) {
                    System.out.println("|s");
                }
            }
        }

    }

    private void outputMap(){
        try { // creating file
            File myObj = new File("output.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write("c - cistern; p - pipe; x - pump; s - spring");
            for (int i = 0; i < columns; i++) {
                System.out.println("_");
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (j != columns-1){
                        if (cells[i][j].isEmpty){
                            System.out.print("| ");
                        } else if (cells[i][j].getComponent() instanceof Cistern) {
                            System.out.print("|c");
                        }else if (cells[i][j].getComponent() instanceof Pipe) {
                            System.out.print("|p");
                        }else if (cells[i][j].getComponent() instanceof Pump) {
                            System.out.print("|x");
                        }else if (cells[i][j].getComponent() instanceof Spring) {
                            System.out.print("|s");
                        }
                    }else {
                        if (cells[i][j].isEmpty){
                            System.out.println("| ");
                        } else if (cells[i][j].getComponent() instanceof Cistern) {
                            System.out.println("|c");
                        }else if (cells[i][j].getComponent() instanceof Pipe) {
                            System.out.println("|p");
                        }else if (cells[i][j].getComponent() instanceof Pump) {
                            System.out.println("|x");
                        }else if (cells[i][j].getComponent() instanceof Spring) {
                            System.out.println("|s");
                        }
                    }

                }
            }


            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private List<Cell> findSprings() {
        List<Cell> springs = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].getComponent() instanceof Spring) {
                    springs.add(cells[i][j]);
                }
            }
        }
        return springs;
    }

    public Cell getCells(int row, int col)
    {
        return cells[row][col];
    }
}
