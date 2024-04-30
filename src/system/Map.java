package system;

import components.Cistern;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;
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
            for (int j = 0; j < cells[i].length; j++) 
            {
                cells[i][j] = new Cell(i,j);
                cells[i][j].map = this;
                cells[i][j].isEmpty = true;
            }
        }
    }

    public void initializeMap(){
        Cistern newCistern1 = new Cistern();
        Cistern newCistern2 = new Cistern();
        Cistern newCistern3 = new Cistern();
        Spring newSpring1 = new Spring();
        Spring newSpring2 = new Spring();
        Pipe newPipe1 = new Pipe();
        Pipe newPipe2 = new Pipe();
        Pipe newPipe3 = new Pipe();
        Pipe newPipe4 = new Pipe();
        Pipe newPipe5 = new Pipe();
        Pipe newPipe6 = new Pipe();
        Pipe newPipe7 = new Pipe();
        Pump pump1 = new Pump();
        Pump pump2 = new Pump();
        cells[0][0].placeComponent(newCistern1);
        cells[4][4].placeComponent(newCistern2);
        cells[7][7].placeComponent(newCistern3);
        cells[0][7].placeComponent(newSpring1);
        cells[7][0].placeComponent(newSpring2);
        cells[0][1].placeComponent(newPipe1);
        cells[0][2].placeComponent(newPipe2);
        cells[0][3].placeComponent(newPipe3);
        cells[0][4].placeComponent(newPipe7);
        cells[3][2].placeComponent(newPipe4);
        cells[4][2].placeComponent(newPipe5);
        cells[4][3].placeComponent(newPipe6);
        cells[1][4].placeComponent(pump1);
        cells[2][2].placeComponent(pump2);
        pump1.setBroken(true);
        newPipe3.setBroken(true);

        /*
        |c|p|p|p| | | |s|
        | | | | |x| | | |
        | | |x| | | | | |
        | | |p| | | | | |
        | | |p|p|c| | | |
        | | | | | | | | |
        | | | | | | | | |
        |s| | | | | | |c|
         */
        try
        {
            newCistern1.addConnectedComponent(newPipe1, Direction.RIGHT);
            newPipe1.addConnectedComponent(newCistern1, Direction.LEFT);

            newPipe1.addConnectedComponent(newPipe2, Direction.RIGHT);
            newPipe2.addConnectedComponent(newPipe1, Direction.LEFT);

            newPipe2.addConnectedComponent(newPipe3, Direction.RIGHT);
            newPipe3.addConnectedComponent(newPipe2, Direction.LEFT);
            
            newPipe3.addConnectedComponent(newPipe7, Direction.RIGHT);
            newPipe7.addConnectedComponent(newPipe3, Direction.LEFT);

            newPipe7.addConnectedComponent(pump1, Direction.DOWN);
            pump1.addConnectedComponent(newPipe7, Direction.UP);

            pump2.addConnectedComponent(newPipe4, Direction.DOWN);
            newPipe4.addConnectedComponent(pump2, Direction.UP);

            newPipe4.addConnectedComponent(newPipe5, Direction.DOWN);
            newPipe5.addConnectedComponent(newPipe4, Direction.UP);

            newPipe5.addConnectedComponent(newPipe6, Direction.RIGHT);
            newPipe6.addConnectedComponent(newPipe5, Direction.LEFT);

            newPipe6.addConnectedComponent(pump2, Direction.RIGHT);
            pump2.addConnectedComponent(newPipe6, Direction.LEFT);
        }
        catch (Exception ex) {}
        
        
        cisterns.add(newCistern1);
        cisterns.add(newCistern2);
        cisterns.add(newCistern3);

        springs.add(newSpring1);
        springs.add(newSpring2);
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

    public void draw(){
        printMap();
        outputMap();
    }

    private void printMap() {
        System.out.println("c - cistern; p - pipe; x - pump; s - spring, b - broken pipe, y - broken pump, * - player");
    
        for (int i = 0; i < columns; i++) {
            System.out.print("_");
        }
        System.out.println();
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isEmpty) {
                    System.out.print("| ");
                }
                else if (cells[i][j].isPlayerOn()) {

                    System.out.print("|*");
                } else if (cells[i][j].getComponent() instanceof Cistern) {
                    System.out.print("|c");
                } else if (cells[i][j].getComponent() instanceof Pipe) {
                    if(((Pipe)cells[i][j].getComponent()).isBroken()){
                        System.out.print("|b");
                    } else {
                    System.out.print("|p");
                    }
                } else if (cells[i][j].getComponent() instanceof Pump) {
                    if(((Pump)cells[i][j].getComponent()).isBroken()){
                    System.out.print("|y");
                    } else {
                        System.out.print("|x");
                    }
                } else if (cells[i][j].getComponent() instanceof Spring) {
                    System.out.print("|s");
                }
            }
            System.out.println("|");
        }
    }

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
                        myWriter.append("* ");
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

    public List<Cistern> getCisterns() 
    {
        return cisterns;
    }

    public List<Spring> getSprings()
    {
        return springs;
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
