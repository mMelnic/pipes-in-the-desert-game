package system;

import components.Cistern;
import components.Component;
import components.Pipe;
import enumerations.Direction;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import jdk.jshell.spi.ExecutionControl;
import player.MovablePlayer;
import player.Plumber;
import player.PlumberScorer;
import player.Saboteur;
import player.SaboteurScorer;
import player.Team;


public class GameManager 
{
    private Map map;
    private Timer timer;
    private int plumbersScore;
    private int saboteursScore;
    private List<Team> teams;
    private boolean isTimeUp = false;
    private Plumber activePlumber = null;
    private Saboteur activeSaboteur = null;
    private MovablePlayer activePlayer;
    private SaboteurScorer saboteurScorer = new SaboteurScorer();
    private PlumberScorer plumberScorer = new PlumberScorer();


    Scanner scanner = new Scanner(System.in);

    public void showKeyBindings()
    {
        String message = "                === KEY BINDINGS ===               \n\n\n"
                       + "In the prototype version, \"the key bindings\" section contains available input commands in the game for testing purposes:\n"
                       + "Enter anything to return back to the main menu.\n";

        System.out.print(message);
        writeToOutputTxt(message);
        scanner.nextLine();
        System.out.println();
    }

    public GameManager() 
    {
        cleanOutputTxt();
        isTimeUp = false;
        plumbersScore = 0;
        saboteursScore = 0;

        teams = new ArrayList<Team>();

        teams.add(new Team(new PlumberScorer()));
        teams.add(new Team(new SaboteurScorer()));
        
        Plumber plumber1 = new Plumber(teams.get(0));
        teams.get(0).assignPlayer(plumber1);

        Plumber plumber2 = new Plumber(teams.get(0));
        teams.get(0).assignPlayer(plumber2);

        Saboteur saboteur1 = new Saboteur(teams.get(1));
        teams.get(1).assignPlayer(saboteur1);

        Saboteur saboteur2 = new Saboteur(teams.get(1));
        teams.get(1).assignPlayer(saboteur2);
    }

    public void startGame() 
    {
        
        map.players.add(teams.get(0).getPlayers().get(0));
        map.players.add(teams.get(0).getPlayers().get(1));
        map.players.add(teams.get(1).getPlayers().get(0));
        map.players.add(teams.get(1).getPlayers().get(1));
        map.players.get(0).setCurrentCell(map.getCells(0, 7));
        map.getCells(0, 7).setPlayerOn(true);
        map.players.get(2).setCurrentCell(map.getCells(0, 3));
        map.getCells(0, 3).setPlayerOn(true);
        map.initializeMap();

        startTimer();

        String inputText;
        GAME_LOOP: while (!isTimeUp && !checkIfAllCisternsAreFull())
        {
            map.draw();
            inputText = receiveInput();

            switch (inputText)
            {
                case "U" -> {
                    activePlayer.move(Direction.UP);
                }
                case "R" -> {
                    activePlayer.move(Direction.RIGHT);
                }
                case "D" -> {
                    activePlayer.move(Direction.DOWN);
                }
                case "L" -> {
                    activePlayer.move(Direction.LEFT);
                }
                case "RepairPipe" -> {
                    if (activePlumber != null)
                    {
                        activePlumber.repairPipe();
                    }
                    else
                    {
                        String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "RepairPump" -> {
                    if (activePlumber != null)
                    {
                        activePlumber.repairPump();
                    }
                    else
                    {
                        String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "PuncturePipe" -> {
                    if (activeSaboteur != null)
                    {
                        activeSaboteur.puncturePipe();
                    }
                    else
                    {
                        String message = "\nYou are not a saboteur: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "InstallComponent" -> {
                    if (activePlumber != null)
                    {
                        activePlumber.installComponent(activePlayer.getFacingDirection());
                    }
                    else
                    {
                        String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "PickComponent" -> {
                    if (activePlumber != null)
                    {
                        activePlumber.pickComponent();
                    }
                    else
                    {
                        String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "ChangePumpDirection" -> {
                    String message = "\nIncoming-outgoing pipes directions (e.g. UL, U (up) - for incoming, L (left) - for outgoing):\n\n";
                    System.out.print(message);
                    writeToOutputTxt(message);
                    inputText = receiveInput();

                    if (inputText.length() != 2) 
                    {
                        String errorMessage = "\nIncorrect input\n\n\n";
                        System.out.print(errorMessage);
                        writeToOutputTxt(errorMessage);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                        continue;
                    }

                    Pipe newIncomingPipe = null;
                    Pipe newOutgoingPipe = null;
                    boolean[] checkPumpOrPipe;

                    switch (inputText.charAt(0))
                    {
                        case 'U' -> {
                            if (map.getUpwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newIncomingPipe = (Pipe)map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'R' -> {
                            if (map.getRightwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newIncomingPipe = (Pipe)map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'D' -> {
                            if (map.getDownwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newIncomingPipe = (Pipe)map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'L' -> {
                            if (map.getLeftwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newIncomingPipe = (Pipe)map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        default -> {
                            if (newIncomingPipe == null)
                            {
                                String errorMessage = "\nIncorrect input, or there is no pipe in the chosen direction for the incoming pipe.\n\n\n";
                                System.out.print(errorMessage);
                                writeToOutputTxt(errorMessage);
                                try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                                continue;
                            }
                        }
                    }
                    switch (inputText.charAt(1))
                    {
                        case 'U' -> {
                            if (map.getUpwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newOutgoingPipe = (Pipe)map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'R' -> {
                            if (map.getRightwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newOutgoingPipe = (Pipe)map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'D' -> {
                            if (map.getDownwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newOutgoingPipe = (Pipe)map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        case 'L' -> {
                            if (map.getLeftwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
                            {
                                newOutgoingPipe = (Pipe)map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
                            }
                        }
                        default -> {
                            if (newOutgoingPipe == null)
                            {
                                String errorMessage = "\nIncorrect input, or there is no pipe in the chosen direction for the outgoing pipe.\n\n\n";
                                System.out.print(errorMessage);
                                writeToOutputTxt(errorMessage);
                                try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                                continue;
                            }
                        }
                    }
                    
                    activePlayer.redirectWaterFlow(newIncomingPipe, newOutgoingPipe);
                }
                case "DetachPipeFromActiveComponent" -> {
                    if (activePlumber != null)
                    {
                        String message = "\nOld component - new component direction pair (e.g. UL, where 'U' (up) is for the old component's direction, 'L' (left) - for the new one's):\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        inputText = receiveInput();

                        if (inputText.length() != 2) 
                        {
                            String errorMessage = "\nIncorrect input\n\n\n";
                            System.out.print(errorMessage);
                            writeToOutputTxt(errorMessage);
                            continue;
                        }
    
                        Component oldComponent = null;
                        Component newComponent = null;
    
                        switch (inputText.charAt(0))
                        {
                            case 'U' -> oldComponent = map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'R' -> oldComponent = map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'D' -> oldComponent = map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'L' -> oldComponent = map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
                            default -> {
                                if (oldComponent == null)
                                {
                                    String errorMessage = "\nIncorrect input.\n\n\n";
                                    System.out.print(errorMessage);
                                    writeToOutputTxt(errorMessage);
                                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                                    continue;
                                }
                            }
                        }
                        switch (inputText.charAt(1))
                        {
                            case 'U' -> newComponent = map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'R' -> newComponent = map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'D' -> newComponent = map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
                            case 'L' -> newComponent = map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
                            default -> {
                                if (newComponent == null)
                                {
                                    String errorMessage = "\nIncorrect input.\n\n\n";
                                    System.out.print(errorMessage);
                                    writeToOutputTxt(errorMessage);
                                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                                    continue;
                                }
                            }
                        }

                        activePlumber.detachPipe(newComponent, oldComponent);
                    }
                    else
                    {
                        String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                }
                case "Exit" -> {
                    break GAME_LOOP;
                }
            }
        }
        String message = "\n\n\nThe game finished. Calculation of points is in progress...\n";
        System.out.print(message);
        writeToOutputTxt(message);
        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}

        saboteursScore = saboteurScorer.getScore();
        plumbersScore = plumberScorer.getScore();

        String winMessage;
        if (plumbersScore >= saboteursScore)
        {
            winMessage = "Plumbers won with a lead of " + (plumbersScore - saboteursScore) + " points!\n\n\n";
        }
        else
        {
            winMessage = "Saboteurs won with a lead of " + (saboteursScore - plumbersScore) + " points!\n\n\n";
        }

        System.out.print(winMessage);
        writeToOutputTxt(message);

    }

    public static void printMainMenuContents()
    {
        String message = "                === PIPES IN THE DESERT ===               \n\n\n"
                       + "1. Start\n"
                       + "2. Key bindings\n"
                       + "3. Exit\n\n";

        System.out.print(message);
        writeToOutputTxt(message);
    }

    public void openMenu()
    {
        String inputText = "";
        int input = 0;

        MENU_LOOP: do
        {
            try
            {
                printMainMenuContents();
                inputText = receiveInput();
                input = Integer.parseInt(inputText);
            }
            catch(Exception exception)
            {
                String message = "\nPlease enter 1, 2, or 3.\n\n\n\n\n";
                System.out.print(message);
                writeToOutputTxt(message);
                try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                input = 0;
                continue;
            }

            
            switch (input)
            {
                case 1 -> showMaps();
                case 2 -> showKeyBindings();
                case 3 -> {
                    break MENU_LOOP;
                }
                default -> {
                    String message = "\nPlease enter 1, 2, or 3.\n\n\n\n\n";
                    System.out.print(message);
                    writeToOutputTxt(message);
                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                }
            }
        }
        while (true);
    }

    public void startSandstorm() 
    {

    }

    public void startTimer() 
    {
        timer = new Timer("GameTimer");
        timer.schedule(new TimerTask() {
            public void run()
            {
                isTimeUp = true;
            }
        }, 3 * 60 * 1000);
    }

    public int compareScore() 
    {
        return 0;
    }

    public boolean checkIfAllCisternsAreFull() 
    {
        for (Cistern cistern : map.getCisterns()) {
            if (!cistern.getIsCisternFull()) {return false;}
        }
        return true;
    }

    public void showMaps() 
    {
        String message = "                === MAPS ===               \n\n\n"
                       + "Choose the map size:\n"
                       + "1. Small\n"
                       + "2. Medium\n"
                       + "3. Large\n"
                       + "4. Return to the main menu.\n\n";

        System.out.print(message);
        writeToOutputTxt(message);

        String inputText = "";
        int input = 0;

        MAPS_MENU_LOOP: do
        {
            try
            {
                inputText = receiveInput();
                input = Integer.parseInt(inputText);
            }
            catch (Exception exception)
            {
                String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
                System.out.print(message);
                writeToOutputTxt(message);
                try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                continue;
            }

            switch (input)
            {
                case 1 -> {
                    map = new Map(5, 5);
                }
                case 2 -> {
                    map = new Map(10, 10);
                }
                case 3 -> {
                    map = new Map(15, 15);
                }
                case 4 -> {
                    break MAPS_MENU_LOOP;
                }
                default -> {
                    String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
                    System.out.print(message);
                    writeToOutputTxt(message);
                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    continue;
                }
            }
            showTeams();
        }
        while (input != 1 && input != 2 && input != 3);
        
    }

    public void showTeams()
    {
        String message = "                === TEAMS ===               \n\n\n"
                       + "1. Plumbers\n"
                       + "2. Saboteurs\n"
                       + "3. Return to the main menu.\n\n";

        System.out.print(message);
        writeToOutputTxt(message);

        String inputText = "";
        int input = 0;

        TEAMS_MENU_LOOP: do
        {
            try
            {
                inputText = receiveInput();
                input = Integer.parseInt(inputText);
            }
            catch (Exception exception)
            {
                String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
                System.out.print(message);
                writeToOutputTxt(message);
                try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                continue;
            }

            switch (input)
            {
                case 1 -> {
                    activePlayer = teams.get(0).getPlayers().get(0);
                    activePlumber = (Plumber)teams.get(0).getPlayers().get(0);
                    activeSaboteur = null;
                }
                case 2 -> {
                    activePlayer = teams.get(1).getPlayers().get(0);
                    activeSaboteur = (Saboteur)teams.get(1).getPlayers().get(0);
                    activePlumber = null;
                }
                case 3 -> {
                    break TEAMS_MENU_LOOP;
                }
                default -> {
                    String errorMessage = "\nPlease enter 1, 2, or 3.\n\n\n\n\n";
                    System.out.print(message);
                    writeToOutputTxt(message);
                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    continue;
                }
            }
            startGame();
        }
        while (input != 1 && input != 2);
        
    }

    public String receiveInput()
    {
        String inputText = "";
        int input = 0;
        do
        {
            input = 0;
            while (input != 1 && input != 2)
            {
                try
                {
                    System.out.println("Would you like to provide input via the command line or an input file?");
                    System.out.println("1 - Command line.");
                    System.out.println("2 - File.");
                    System.out.print("Your choice: ");
                    inputText = scanner.nextLine();
                    input = Integer.parseInt(inputText);
                }
                catch (Exception exception)
                {
                    System.out.println("Incorrect input. Please enter either 1 or 2.");
                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    System.out.println("\n\n");
                }
            }
            
    
            switch (input)
            {
                case 1 -> {
                    System.out.print("Enter your input: ");
                    inputText = scanner.nextLine();
                }
                case 2 -> {
                    System.out.print("Enter file path: ");
                    inputText = scanner.nextLine();

                    Scanner fileScanner = null;


                    try
                    {
                        fileScanner = new Scanner(new File(inputText));
                        inputText  = fileScanner.nextLine();
                    }
                    catch(Exception exception)
                    {
                        System.out.println("File not found or it is empty.");
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                        System.out.println("\n\n");
                        continue;
                    }
                    finally
                    {
                        if (fileScanner != null) {fileScanner.close();}
                    }
                    cleanOutputTxt();
                }
                default -> {
                    if (input != 1 && input != 2)
                    {
                        System.out.println("Incorrect input. Please enter either 1 or 2.");
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                        System.out.println("\n\n");
                    }
                }
            }
            System.out.println();
            return inputText;
        }
        while (true);
    }

    public static void writeToOutputTxt(String message)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter("output.txt", true);
            fileWriter.append(message);
        }
        catch(Exception exception)
        {
            System.out.println("ERROR!");
            exception.printStackTrace();
            System.out.println();
        }
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (Exception ex) {}
            }
        }
    }

    public static void cleanOutputTxt()
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter("output.txt", false);
            fileWriter.write("");
        }
        catch(Exception exception)
        {exception.printStackTrace();}
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch(Exception ex) {}
            }
        }
    }
}
