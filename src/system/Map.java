package system;

import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import components.Spring;
import enumerations.Direction;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import player.MovablePlayer;
import player.PlumberScorer;
import player.SaboteurScorer;

/**
 * The Map class represents the game map consisting of cells, cisterns, pipes,
 * pumps, and springs.
 * It facilitates various map-related operations such as initialization,
 * updating water flow, and providing neighboring cells.
 */
public class Map {
    public int rows = 8; // temporary number
    public int columns = 8; // temporary number
    private Cell[][] cells = new Cell[rows][columns];
    private List<Cistern> cisterns = new ArrayList<>();
    private List<Spring> springs = new ArrayList<>();
    private List<Pump> pumps = new ArrayList<>();
    public List<MovablePlayer> players = new ArrayList<>();
    List<Pump> pumpsTraversed = new ArrayList<>(); // Local list to store traversed pumps
    private PlumberScorer plumberScorer;
    private SaboteurScorer saboteurScorer;
    private JPanel mapPanel;
    private GameManager gameManager;

    public Map(int sizeN, int sizeM) {
        // rows = sizeN;
        // columns = sizeM;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].map = this;
                cells[i][j].isEmpty = true;
            }
        }
    }

    /**
     * Initializes the map by placing cisterns, springs, pipes, and pumps on the map
     * grid.
     */
    public void initializeMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (j == rows - 1 && (i % 2) == 0) {
                    Cistern newCistern = new Cistern(cells[i][j]);
                    newCistern.addScorer(plumberScorer);
                    newCistern.addCisternFullListener(gameManager);
                    cells[i][j].placeComponent(newCistern);
                    cisterns.add(newCistern);
                } else if (i == 2 && j == 2 || i == 5 && j == 6) {
                    Spring newSpring = new Spring(cells[i][j]);
                    cells[i][j].placeComponent(newSpring);
                    springs.add(newSpring);
                } else if (i == 0 && (j != columns - 1)) {
                    if (j == 5) {
                        Pump newPump = new Pump(3, cells[i][j]);
                        cells[i][j].placeComponent(newPump);
                        newPump.addScorer(saboteurScorer);
                    } else {
                        Pipe newPipe = new Pipe(cells[i][j]);
                        newPipe.addScorer(saboteurScorer);
                        cells[i][j].placeComponent(newPipe);
                    }
                } else if (i == 7 && (j != 0)) {
                    if (j == 4) {
                        Pump newPump = new Pump(3, cells[i][j]);
                        cells[i][j].placeComponent(newPump);
                        newPump.addScorer(saboteurScorer);
                    } else {
                        Pipe newPipe = new Pipe(cells[i][j]);
                        newPipe.addScorer(saboteurScorer);
                        cells[i][j].placeComponent(newPipe);
                    }
                }

            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].getComponent() != null) {
                    // Check if left cell exists and has a component
                    if (j > 0 && cells[i][j - 1].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i][j - 1].getComponent(),
                                    Direction.LEFT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if right cell exists and has a component
                    if (j < cells[i].length - 1 && cells[i][j + 1].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i][j + 1].getComponent(),
                                    Direction.RIGHT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if lower cell exists and has a component
                    if (i < cells.length - 1 && cells[i + 1][j].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i + 1][j].getComponent(),
                                    Direction.DOWN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // Check if upper cell exists and has a component
                    if (i > 0 && cells[i - 1][j].getComponent() != null) {
                        try {
                            cells[i][j].getComponent().addConnectedComponent(cells[i - 1][j].getComponent(),
                                    Direction.UP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                ((Pipe)cells[7][7].getComponent()).changeShape();
            }
        }

    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Updates the water flow on the map by simulating the flow from springs through
     * pipes.
     */
    public synchronized void updateWaterFlow() {
        // Initialize a set to keep track of visited components
        Set<Component> visited = new HashSet<>();

        // Step 1: Start water supply from springs that are already supplying water
        boolean anySpringFlowing = false;
        for (Spring spring : springs) {
            if (spring.isWaterFlowing()) {
                spring.startWaterSupplyDFS(spring, visited);
                anySpringFlowing = true;
            }
        }
        // Return if no spring has water flowing
        if (!anySpringFlowing) {
            return;
        }

        // Step 2: Set unvisited components to not have water flow and not leaking
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                Cell cell = cells[row][col];
                if (cell != null) {
                    Component component = cell.getComponent();
                    if (component != null && !visited.contains(component)) {
                        resetComponentState(component);
                    }
                }
            }
        }
        checkForFreeEnds();
        // SwingUtilities.invokeLater(() -> mapPanel.repaint());
    }

    private void resetComponentState(Component component) {
        if (component instanceof Pipe) {
            Pipe pipe = (Pipe) component;
            pipe.setWaterFlowing(false);
            pipe.setFull(false);
            pipe.stopLeaking();
        } else if (component instanceof Pump) {
            Pump pump = (Pump) component;
            if (pump.isReservoirFull()) {
                pump.setReservoirFull(false);
                pump.stopFillingTask();
            }
            pump.stopLeaking();
        }
    }

    public synchronized void checkForFreeEnds() {
        List<Pipe> pipesWithFreeEndLeaking = findPipesWithFreeEndLeaking();
        updatePipesNotInList(pipesWithFreeEndLeaking);
        resetPumps();
    }

    private List<Pipe> findPipesWithFreeEndLeaking() {
        List<Pipe> pipesWithFreeEndLeaking = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells[i][j];
                if (cell != null) {
                    Component component = cell.getComponent();
                    if (component instanceof Pipe) {
                        Pipe pipe = (Pipe) component;
                        if (pipe.getConnectedComponents().size() == 1 && pipe.isWaterFlowing()) {
                            pipe.setFreeEndLeaking(true);
                            pipesWithFreeEndLeaking.add(pipe);
                        }
                    } else if (component instanceof Pump) {
                        Pump pump = (Pump) component;
                        checkPumpConnectedPipes(pump, pipesWithFreeEndLeaking);
                    }
                }
            }
        }
        return pipesWithFreeEndLeaking;
    }

    private List<Pump> checkPumpConnectedPipes(Pump pump, List<Pipe> pipesWithFreeEndLeaking) {
        for (Component connectedComponent : pump.getConnectedComponents().values()) {
            if (connectedComponent instanceof Pipe
                    && ((Pipe) connectedComponent).isWaterFlowing()) {
                Pipe pipe = (Pipe) connectedComponent;
                if (pipe != pump.getIncomingPipe() && pipe != pump.getOutgoingPipe()) {
                    pipe.setFreeEndLeaking(true);
                    pipesWithFreeEndLeaking.add(pipe);
                    pumpsTraversed.add(pump);
                }
            }
        }
        return pumpsTraversed;
    }

    private void updatePipesNotInList(List<Pipe> pipesWithFreeEndLeaking) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells[i][j];
                if (cell != null) {
                    Component component = cell.getComponent();
                    if (component instanceof Pipe) {
                        Pipe pipe = (Pipe) component;
                        if (!pipesWithFreeEndLeaking.contains(pipe)) {
                            pipe.setFreeEndLeaking(false);
                        }
                    }
                }
            }
        }
    }

    public void resetPumps() {
        for (Pump pump : pumpsTraversed) {
            if (pump.isReservoirFull()) {
                pump.setReservoirFull(false);
                pump.stopFillingTask();
                if (pump.getOutgoingPipe().isFull() || pump.getIncomingPipe().isFull()) {
                    pump.undoFullPipes();
                }
            } else if (pump.isFilling()) {
                pump.stopFillingTask();
            }
            if (pump.isLeaking()) {
                pump.stopLeaking();
            }
        }
        pumpsTraversed.clear(); // Reset the list of traversed pumps
    }

    public void stopLeakingAndFreeEnds() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells[i][j];
                Component component = cell.getComponent();
                if (component instanceof Pipe) {
                    Pipe pipe = (Pipe) component;
                    if (pipe.isLeaking()) {
                        pipe.stopLeaking();
                    }
                    if (pipe.isFreeEndLeaking()) {
                        pipe.setFreeEndLeaking(false);
                    }
                } else if (component instanceof Pump) {
                    Pump pump = (Pump) component;
                    if (pump.isLeaking()) {
                        pump.stopLeaking();
                    }
                }
            }
        }
    }

    public Cell getUpwardCell(Cell currentCell) {
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
     * 
     * @param currentCell The cell for which to find the downward neighbor.
     * @return The cell located below the specified cell, or null if no downward
     *         neighbor exists.
     */
    public Cell getDownwardCell(Cell currentCell) {
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

    public Cell getRightwardCell(Cell currentCell) {
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

    public boolean isNeighbouringCell(Cell cell1, Cell cell2) {
        Cell upCell = getUpwardCell(cell1);
        Cell downCell = getDownwardCell(cell1);
        Cell leftCell = getLeftwardCell(cell1);
        Cell rightCell = getRightwardCell(cell1);
        if (cell2.equals(upCell) || cell2.equals(downCell) || cell2.equals(leftCell) || cell2.equals(rightCell)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Cell> getNeighbouringCells(Cell cell) {
        List<Cell> returnList = new ArrayList<>();
        returnList.add(getDownwardCell(cell));
        returnList.add(getUpwardCell(cell));
        returnList.add(getLeftwardCell(cell));
        returnList.add(getRightwardCell(cell));
        return returnList;
    }

    /**
     * Retrieves the list of pumps present on the map.
     * 
     * @return The list of pumps.
     */
    public List<Pump> getPumps() {
        for (int i = 0; i < 8; i++) {
            for (Cell cell : cells[i]) {
                if (cell.getComponent() instanceof Pump) {
                    pumps.add((Pump) cell.getComponent());
                }
            }
        }
        return pumps;
    }

    /**
     * Draws the map by printing its contents to the console and writing them to a
     * file.
     */
    public void draw() {
        printMap();
        outputMap();
    }

    /**
     * Prints the map to the console.
     * '|' represents cell boundaries, 'c' represents cistern, 'p' represents pipe,
     * 'x' represents pump, 's' represents spring,
     * '*' represents plumber, and '+' represents saboteur.
     */
    public void printMap() {
        System.out.println("c - cistern; p - pipe; x - pump; s - spring");
        for (int i = 0; i < columns; i++) {
            System.out.print("_");
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isEmpty) {
                    System.out.print("| ");
                }  else {
                    if (cells[i][j].isPlayerOn()) {
                        System.out.print("|*"); // Print player symbol
                    } else if (cells[i][j].getComponent() instanceof Cistern) {
                        System.out.print("|c");
                    } else if (cells[i][j].getComponent() instanceof Spring) {
                        System.out.print("|s");
                    } else if (cells[i][j].getComponent() instanceof Pipe) {
                        Pipe pipe = (Pipe) cells[i][j].getComponent();
                        if (pipe.isFreeEndLeaking()) {
                            System.out.print("|e");
                        } else if (pipe.isLeaking()) {
                            System.out.print("|l");
                        } else if (pipe.isBroken()) {
                            System.out.print("|b");
                        } else if (pipe.isFull()) {
                            System.out.print("|f");
                        } else if (pipe.isWaterFlowing()) {
                            System.out.print("|w");
                        } else {
                            System.out.print("|p");
                        }
                    } else if (cells[i][j].getComponent() instanceof Pump) {
                        Pump pump = (Pump) cells[i][j].getComponent();
                        if (pump.isReservoirFull()) {
                            System.out.print("|F");
                        } else if (pump.isLeaking()) {
                            System.out.print("|L");
                        } else if (pump.isBroken()) {
                            System.out.print("|X");
                        } else {
                            System.out.print("|x");
                        }
                    }
                }
            }
            System.out.println("|");
        }
    }

    /**
     * Outputs the map to a text file named "output.txt".
     * '|' represents cell boundaries, 'c' represents cistern, 'p' represents pipe,
     * 'x' represents pump, 's' represents spring,
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

    /**
     * Retrieves the list of cisterns present on the map.
     * 
     * @return The list of cisterns.
     */
    public List<Cistern> getCisterns() {
        return cisterns;
    }

    /**
     * Retrieves the list of springs present on the map.
     * 
     * @return The list of springs.
     */
    public List<Spring> getSprings() {
        return springs;
    }
   
    public Cell getCells(int row, int col) {
        return cells[row][col];
    }

    public void setPlumberScorer(PlumberScorer plumberScorer) {
        this.plumberScorer = plumberScorer;
    }
    public int getRows(){
        return rows;
    }
    public int getColumns(){
        return columns;
    }

    public JPanel getMapPanel() {
        return mapPanel;
    }
    
    public SaboteurScorer getSaboteurScorer() {
        return saboteurScorer;
    }

    public void setSaboteurScorer(SaboteurScorer saboteurScorer) {
        this.saboteurScorer = saboteurScorer;
    }
    
    public void setMapPanel(JPanel mapPanel) {
        this.mapPanel = mapPanel;
    }
}