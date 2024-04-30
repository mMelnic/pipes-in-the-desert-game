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

import enumerations.Direction;
import exceptions.CisternMultipleComponentsConnectedException;
import exceptions.PumpConnectablePipeNumberExceedException;
import exceptions.SpringMultipleComponensConnectedException;
import player.MovablePlayer;
import player.Plumber;
import player.Saboteur;

/**
 * The Map class represents the game map consisting of cells, cisterns, pipes, pumps, and springs.
 * It facilitates various map-related operations such as initialization, updating water flow, and providing neighboring cells.
 */
public class Map {
    private int rows = 8; // temporary number
    private int columns = 8; // temporary number
    private Cell[][] cells = new Cell[rows][columns];
    private List<Cistern> cisterns = new ArrayList<Cistern>();
    private List<Spring> springs = new ArrayList<Spring>();
    private List<Pump> pumps = new ArrayList<Pump>();
    private int numberOfCisterns = 0; // must be a multiple of 4
    private int numberOfSprings = numberOfCisterns / 4;

    public List<MovablePlayer> players = new ArrayList<MovablePlayer>();


    String size;

    public Map(int sizeN, int sizeM){
        // rows = sizeN;
        // columns = sizeM;

        for (int i = 0; i < cells.length; i++)
        {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].map = this;
                cells[i][j].isEmpty = true;
                /* 
                try {
                    cells[i][j].getComponent().addConnectedComponent(cells[i][j].getComponent(), Direction.LEFT);
                    cells[i][j].getComponent().addConnectedComponent(cells[i][j].getComponent(), Direction.RIGHT);
                    cells[i][j].getComponent().addConnectedComponent(cells[i][j].getComponent(), Direction.DOWN);
                    cells[i][j].getComponent().addConnectedComponent(cells[i][j].getComponent(), Direction.UP);
                } catch (PumpConnectablePipeNumberExceedException e) {
                    e.printStackTrace();
                } catch (CisternMultipleComponentsConnectedException e) {
                    e.printStackTrace();
                } catch (SpringMultipleComponensConnectedException e) {
                    e.printStackTrace();
                }
                */
            }


        }
    }

     /**
     * Initializes the map by placing cisterns, springs, pipes, and pumps on the map grid.
     */
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
                
                if (cells[i][j].getComponent() != null) {
                    // Check if left cell exists and has a component
                    if (j > 0 && cells[i][j - 1].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i][j - 1].getComponent(),
                                    Direction.LEFT);
                        } catch (PumpConnectablePipeNumberExceedException | CisternMultipleComponentsConnectedException
                                | SpringMultipleComponensConnectedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if right cell exists and has a component
                    if (j < cells[i].length - 1 && cells[i][j + 1].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i][j + 1].getComponent(),
                                    Direction.RIGHT);
                        } catch (PumpConnectablePipeNumberExceedException | CisternMultipleComponentsConnectedException
                                | SpringMultipleComponensConnectedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if lower cell exists and has a component
                    if (i < cells.length - 1 && cells[i + 1][j].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i + 1][j].getComponent(),
                                    Direction.DOWN);
                        } catch (PumpConnectablePipeNumberExceedException | CisternMultipleComponentsConnectedException
                                | SpringMultipleComponensConnectedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if upper cell exists and has a component
                    if (i > 0 && cells[i - 1][j].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i - 1][j].getComponent(),
                                    Direction.UP);
                        } catch (PumpConnectablePipeNumberExceedException | CisternMultipleComponentsConnectedException
                                | SpringMultipleComponensConnectedException e) {
                            e.printStackTrace();
                        }
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
    /**
     * Retrieves the cell located below the specified cell on the map grid.
     * @param currentCell The cell for which to find the downward neighbor.
     * @return The cell located below the specified cell, or null if no downward neighbor exists.
     */
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
    /**
     * Updates the water flow on the map by simulating the flow from springs through pipes.
     */
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

     /**
     * Retrieves the list of pumps present on the map.
     * @return The list of pumps.
     */
    public List<Pump> getPumps()
    {
        for (int i = 0; i < 8; i++)
        {
            for (Cell cell : cells[i])
            {
                if (cell.getComponent() instanceof Pump)
                {
                    pumps.add((Pump)cell.getComponent());
                }
            }
        }
        return pumps;
    }
/**
     * Draws the map by printing its contents to the console and writing them to a file.
     */
    public void draw(){
        printMap();
        outputMap();
    }
 /**
     * Prints the map to the console.
     * '|' represents cell boundaries, 'c' represents cistern, 'p' represents pipe, 'x' represents pump, 's' represents spring,
     * '*' represents plumber, and '+' represents saboteur.
     */
    private void printMap() {
        System.out.println("c - cistern; p - pipe; x - pump; s - spring; * - plumber; + - saboteur");
    
        for (int i = 0; i < columns; i++) {
            System.out.print("_");
        }
        System.out.println();
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isEmpty) {
                    System.out.print("| ");
                } else if (cells[i][j].isPlayerOn()) {
                    if (cells[i][j].getPlayerOn() instanceof Plumber) {
                        System.out.print("|*");
                    } else if (cells[i][j].getPlayerOn() instanceof Saboteur) {
                        System.out.print("|+");
                    }
                } else if (cells[i][j].getComponent() instanceof Cistern) {
                    System.out.print("|c");
                } else if (cells[i][j].getComponent() instanceof Pipe) {
                    System.out.print("|p");
                } else if (cells[i][j].getComponent() instanceof Pump) {
                    System.out.print("|x");
                } else if (cells[i][j].getComponent() instanceof Spring) {
                    System.out.print("|s");
                }
            }
            System.out.println("|");
        }
    }
     /**
     * Outputs the map to a text file named "output.txt".
     * '|' represents cell boundaries, 'c' represents cistern, 'p' represents pipe, 'x' represents pump, 's' represents spring,
     * '*' represents plumber, and '+' represents saboteur.
     */
    private void outputMap() {
        try {
            FileWriter myWriter = new FileWriter("output.txt", true);
            myWriter.append("c - cistern; p - pipe; x - pump; s - spring\n");
    
            for (int i = 0; i < columns; i++) {
                myWriter.append("__");
            }
            myWriter.append("\n");
    
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    myWriter.append("|");
                    if (cells[i][j].isEmpty) {
                        myWriter.append("  ");
                    } else if (cells[i][j].isPlayerOn()) {
                        if (cells[i][j].getPlayerOn() instanceof Plumber) {
                            myWriter.append("* ");
                        } else if (cells[i][j].getPlayerOn() instanceof Saboteur) {
                            myWriter.append("+ ");
                        }
                    } else if (cells[i][j].getComponent() instanceof Cistern) {
                        myWriter.append("c ");
                    } else if (cells[i][j].getComponent() instanceof Pipe) {
                        myWriter.append("p ");
                    } else if (cells[i][j].getComponent() instanceof Pump) {
                        myWriter.append("x ");
                    } else if (cells[i][j].getComponent() instanceof Spring) {
                        myWriter.append("s ");
                    }
                }
                myWriter.append("|\n");
            }
    
            for (int i = 0; i < columns; i++) {
                myWriter.append("__");
            }
            myWriter.append("\n");
    
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
 /**
     * Retrieves the list of cisterns present on the map.
     * @return The list of cisterns.
     */
    public List<Cistern> getCisterns() 
    {
        return cisterns;
    }

    /**
     * Retrieves the list of springs present on the map.
     * @return The list of springs.
     */
    public List<Spring> getSprings()
    {
        return springs;
    }
    
 /**
     * Finds and returns a list of cells containing springs.
     * @return A list of cells containing springs.
     */
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
