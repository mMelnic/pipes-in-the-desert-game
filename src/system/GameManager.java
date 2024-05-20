package system;

import GUI.MainWindow;
import GUI.MapWindow;
import components.Cistern;
import components.Component;
import components.Pipe;
import components.Pump;
import interfaces.ICisternListener;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import player.MovablePlayer;
import player.Plumber;
import player.PlumberScorer;
import player.Saboteur;
import player.SaboteurScorer;
import player.Team;
import javax.swing.*;

/**
 * The GameManager class manages the game's flow and logic.
 */
public class GameManager implements ICisternListener
{
    private Map map;
    private List<Team> teams;
    private Plumber activePlumber = null;
    private Saboteur activeSaboteur = null;
    private Saboteur activeSaboteur2;
    private Plumber activePlumber2;
    private SaboteurScorer saboteurScorer = new SaboteurScorer();
    private PlumberScorer plumberScorer = new PlumberScorer();
    private MapWindow mapWindow;

    Scanner scanner = new Scanner(System.in);
    /**
     * Displays the key bindings for the game.
     */
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

    /**
     * Constructs a GameManager instance.
     */
    public GameManager() 
    {
        cleanOutputTxt();

        teams = new ArrayList<Team>();

        teams.add(new Team(plumberScorer));
        teams.add(new Team(saboteurScorer));
        
        Plumber plumber1 = new Plumber(teams.get(0));
        teams.get(0).assignPlayer(plumber1);

        Plumber plumber2 = new Plumber(teams.get(0));
        teams.get(0).assignPlayer(plumber2);

        Saboteur saboteur1 = new Saboteur(teams.get(1));
        teams.get(1).assignPlayer(saboteur1);

        Saboteur saboteur2 = new Saboteur(teams.get(1));
        teams.get(1).assignPlayer(saboteur2);
    }

    public void setMapWindow(MapWindow mapWindow) {
        this.mapWindow = mapWindow;
    }

    @Override
    public void onCisternFullCheck() {
        if (checkIfAllCisternsAreFull()) {
            map.stopLeakingAndFreeEnds();
            mapWindow.DisplayCisternFullWindow();
            // Handle the event when all cisterns are full
            System.out.println("All cisterns are full!");
            // TODO compare score
            
        }
    }

    /**
     * Starts the game.
     */
    public void startGame() 
    {
        map.setPlumberScorer(plumberScorer);
        map.setSaboteurScorer(saboteurScorer);
        map.players.add(teams.get(0).getPlayers().get(0));
        map.players.add(teams.get(0).getPlayers().get(1));
        map.players.add(teams.get(1).getPlayers().get(0));
        map.players.add(teams.get(1).getPlayers().get(1));
        activeSaboteur.setCurrentCell(map.getCells(0, 0));
        map.getCells(0, 0).setPlayerOn(true);
        activeSaboteur2.setCurrentCell(map.getCells(0, 1));
        map.getCells(0, 1).setPlayerOn(true);
    
        // Put the plumber on row 1, column 3
        activePlumber.setCurrentCell(map.getCells(0, 3));
        map.getCells(0, 3).setPlayerOn(true);
        activePlumber2.setCurrentCell(map.getCells(0, 5));
        map.getCells(0, 5).setPlayerOn(true);

        map.setGameManager(this);
    
        map.initializeMap();
       

        for (Cistern cistern : map.getCisterns()) {
             cistern.addCisternFullListener(this);
        }

        manufactureComponents();
        startSandstorms();
        // String inputText;

        
       
        // GAME_LOOP: while (!isTimeUp && !checkIfAllCisternsAreFull())
        // {
        //     map.draw();
        //     inputText = receiveInput();
        //     System.out.println("Current player: " + (activePlayer instanceof Plumber ? "Plumber" : "Saboteur"));

        //     switch (inputText)
        //     {
        //         case "SwitchPlayer" -> {
        //             switchActivePlayer();
        //             System.out.println("Switched to " + (activePlayer instanceof Plumber ? "Plumber" : "Saboteur"));
        //         }
        //         case "U" -> {
        //             activePlayer.move(Direction.UP);
        //         }
        //         case "R" -> {
        //             activePlayer.move(Direction.RIGHT);
        //         }
        //         case "D" -> {
        //             activePlayer.move(Direction.DOWN);
        //         }
        //         case "L" -> {
        //             activePlayer.move(Direction.LEFT);
        //         }
        //         case "RepairPipe" -> {
        //             if (activePlumber != null)
        //             {
        //                 activePlumber.repairPipe();
        //             }
        //             else
        //             {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //             }
        //         }
        //         case "RepairPump" -> {
        //             if (activePlumber != null)
        //             {
        //                 activePlumber.repairPump();
        //             }
        //             else
        //             {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //             }
        //         }
        //         case "PuncturePipe" -> {
        //             if (activeSaboteur != null)
        //             {
        //                 activeSaboteur.puncturePipe();
        //             }
        //             else
        //             {
        //                 String message = "\nYou are not a saboteur: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //             }
        //         }
        //         case "InstallComponent" -> {
        //             if (activePlumber != null) {
        //                 activePlumber.installComponent(activePlayer.getFacingDirection());
        //             } else {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {
        //                     Thread.sleep(1500);
        //                 } catch (InterruptedException interruptedException) {
        //                 }
        //             }
        //         }
        //         case "PickComponent" -> {
        //             if (activePlumber != null)
        //             {
        //                 activePlumber.pickComponent();
        //             }
        //             else
        //             {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //             }
        //         }case "DropComponent" -> {
        //             if (activePlumber != null) {
        //                 activePlumber.dropComponent();
        //             } else {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {
        //                     Thread.sleep(1500);
        //                 } catch (InterruptedException interruptedException) {
        //                 }
        //             }
        //         }
        //         case "ChangePumpDirection" -> {
        //             String message = "\nIncoming-outgoing pipes directions (e.g. UL, U (up) - for incoming, L (left) - for outgoing):\n\n";
        //             System.out.print(message);
        //             writeToOutputTxt(message);
        //             inputText = receiveInput();

        //             if (inputText.length() != 2) 
        //             {
        //                 String errorMessage = "\nIncorrect input\n\n\n";
        //                 System.out.print(errorMessage);
        //                 writeToOutputTxt(errorMessage);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //                 continue;
        //             }

        //             Pipe newIncomingPipe = null;
        //             Pipe newOutgoingPipe = null;
        //             boolean[] checkPumpOrPipe;

        //             switch (inputText.charAt(0))
        //             {
        //                 case 'U' -> {
        //                     if (map.getUpwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newIncomingPipe = (Pipe)map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'R' -> {
        //                     if (map.getRightwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newIncomingPipe = (Pipe)map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'D' -> {
        //                     if (map.getDownwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newIncomingPipe = (Pipe)map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'L' -> {
        //                     if (map.getLeftwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newIncomingPipe = (Pipe)map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 default -> {
        //                     if (newIncomingPipe == null)
        //                     {
        //                         String errorMessage = "\nIncorrect input, or there is no pipe in the chosen direction for the incoming pipe.\n\n\n";
        //                         System.out.print(errorMessage);
        //                         writeToOutputTxt(errorMessage);
        //                         try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //                         continue;
        //                     }
        //                 }
        //             }
        //             switch (inputText.charAt(1))
        //             {
        //                 case 'U' -> {
        //                     if (map.getUpwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newOutgoingPipe = (Pipe)map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'R' -> {
        //                     if (map.getRightwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newOutgoingPipe = (Pipe)map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'D' -> {
        //                     if (map.getDownwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newOutgoingPipe = (Pipe)map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 case 'L' -> {
        //                     if (map.getLeftwardCell(activePlayer.getCurrentCell()).checkPumpOrPipe()[0])
        //                     {
        //                         newOutgoingPipe = (Pipe)map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     }
        //                 }
        //                 default -> {
        //                     if (newOutgoingPipe == null)
        //                     {
        //                         String errorMessage = "\nIncorrect input, or there is no pipe in the chosen direction for the outgoing pipe.\n\n\n";
        //                         System.out.print(errorMessage);
        //                         writeToOutputTxt(errorMessage);
        //                         try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //                         continue;
        //                     }
        //                 }
        //             }
                    
        //             activePlayer.redirectWaterFlow(newIncomingPipe, newOutgoingPipe);
        //         }
        //         case "DetachPipe" -> {
        //             if (activePlumber != null)
        //             {
        //                 String message = "\nOld component - new component direction pair (e.g. UL, where 'U' (up) is for the old component's direction, 'L' (left) - for the new one's):\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 inputText = receiveInput();

        //                 if (inputText.length() != 2) 
        //                 {
        //                     String errorMessage = "\nIncorrect input\n\n\n";
        //                     System.out.print(errorMessage);
        //                     writeToOutputTxt(errorMessage);
        //                     continue;
        //                 }
    
        //                 Component oldComponent = null;
        //                 Component newComponent = null;
    
        //                 switch (inputText.charAt(0))
        //                 {
        //                     case 'U' -> oldComponent = map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'R' -> oldComponent = map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'D' -> oldComponent = map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'L' -> oldComponent = map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     default -> {
        //                         if (oldComponent == null)
        //                         {
        //                             String errorMessage = "\nIncorrect input.\n\n\n";
        //                             System.out.print(errorMessage);
        //                             writeToOutputTxt(errorMessage);
        //                             try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //                             continue;
        //                         }
        //                     }
        //                 }
        //                 switch (inputText.charAt(1))
        //                 {
        //                     case 'U' -> newComponent = map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'R' -> newComponent = map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'D' -> newComponent = map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'L' -> newComponent = map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     default -> {
        //                         if (newComponent == null)
        //                         {
        //                             String errorMessage = "\nIncorrect input.\n\n\n";
        //                             System.out.print(errorMessage);
        //                             writeToOutputTxt(errorMessage);
        //                             try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //                             continue;
        //                         }
        //                     }
        //                 }

        //                 activePlumber.detachPipe(newComponent, oldComponent);
        //             }
        //             else
        //             {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
        //             }
        //         }
        //         case "StartSandstorm" -> {
        //             startSandstorm();
        //         }
        //         case "ConnectPSC" -> {
        //             if (activePlumber != null) {
        //                 String message = "\n Cistern/Spring direction  (e.g. L, 'L' (left) - for the spring/cistern's):\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 inputText = receiveInput();

        //                 if (inputText.length() != 1) {
        //                     String errorMessage = "\nIncorrect input\n\n\n";
        //                     System.out.print(errorMessage);
        //                     writeToOutputTxt(errorMessage);
        //                     continue;
        //                 }

        //                 Component oldComponent = null;

        //                 switch (inputText.charAt(0)) {
        //                     case 'U' -> oldComponent = map.getUpwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'R' ->
        //                         oldComponent = map.getRightwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'D' ->
        //                         oldComponent = map.getDownwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     case 'L' ->
        //                         oldComponent = map.getLeftwardCell(activePlayer.getCurrentCell()).getComponent();
        //                     default -> {
        //                         if (oldComponent == null) {
        //                             String errorMessage = "\nIncorrect input.\n\n\n";
        //                             System.out.print(errorMessage);
        //                             writeToOutputTxt(errorMessage);
        //                             try {
        //                                 Thread.sleep(1500);
        //                             } catch (InterruptedException interruptedException) {
        //                             }
        //                             continue;
        //                         }
        //                     }
        //                 }

        //                 activePlumber.connectPipeWithComponent((Pipe)activePlumber.getCurrentCell().getComponent(), oldComponent);
        //             } else {
        //                 String message = "\nYou are not a plumber: the action is not possible!\n\n\n";
        //                 System.out.print(message);
        //                 writeToOutputTxt(message);
        //                 try {
        //                     Thread.sleep(1500);
        //                 } catch (InterruptedException interruptedException) {
        //                 }
        //             }
        //         }
        //         case "Exit" -> {
        //             break GAME_LOOP;
        //         }
        //     }
        // }
        // String message = "\n\n\nThe game finished. Calculation of points is in progress...\n";
        // System.out.print(message);
        // writeToOutputTxt(message);
        // try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}

        // saboteursScore = saboteurScorer.getScore();
        // plumbersScore = plumberScorer.getScore();

        // String winMessage;
        // if (plumbersScore >= saboteursScore)
        // {
        //     winMessage = "Plumbers won with a lead of " + (plumbersScore - saboteursScore) + " points!\n\n\n";
        // }
        // else
        // {
        //     winMessage = "Saboteurs won with a lead of " + (saboteursScore - plumbersScore) + " points!\n\n\n";
        // }

        // System.out.print(winMessage);
        // writeToOutputTxt(message);

    }

    /**
     * Displays the main menu contents.
     */
    public static void printMainMenuContents()
    {
        String message = "                === PIPES IN THE DESERT ===               \n\n\n"
                       + "1. Start\n"
                       + "2. Key bindings\n"
                       + "3. Exit\n\n";

        System.out.print(message);
        writeToOutputTxt(message);
    }
    // public void switchActivePlayer() 
    // {
    //     if (activePlayer instanceof Plumber) {
    //         activePlayer = teams.get(1).getPlayers().get(0);
    //         activeSaboteur = (Saboteur) activePlayer;
    //         activePlumber = null;
    //     } else if (activePlayer instanceof Saboteur) {
    //         activePlayer = teams.get(0).getPlayers().get(0);
    //         activePlumber = (Plumber) activePlayer;
    //         activeSaboteur = null;
    //     }
    // }

  
    public void openMenu() {
        MainWindow mainWindow = new MainWindow(this);
        mainWindow.show();
    }

    public void startSandstorms() {
        Timer sandstormTimer = new Timer("SandstormTimer");
        sandstormTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                startSandstorm();
            }
        }, 1000 * 15, 1000 * 50); 
    }

    /**
     * Initiates a sandstorm event.
     */
    public void startSandstorm() {
        System.out.println("SANDSTORM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        showSandstormWindow();
        List<Pump> pumps = map.getPumps();

        if (pumps.isEmpty()) {
            System.out.println("No pumps available.");
            return;
        }

        if (pumps.size() == 1) {
            Pump pump = pumps.get(0);
            if (!pump.isBroken()) {
                pump.setBroken(true);
                if (isPumpConnectedToWaterFlowingPipe(pump)) {
                    pump.fillReservoir();
                }
            }
        } else {
            Random random = new Random();
            Pump randomPump = pumps.get(random.nextInt(pumps.size()));
            if (!randomPump.isBroken()) {
                randomPump.setBroken(true);
                if (isPumpConnectedToWaterFlowingPipe(randomPump)) {
                    randomPump.fillReservoir();
                }
            }
        }

        
        
    }

    private void showSandstormWindow() {
        JFrame frame = new JFrame("Sandstorm Alert");
        JLabel label = new JLabel("SANDSTORM!", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 27));
        frame.add(label);
        frame.setSize(300, 100);
        frame.setUndecorated(true); // Remove window decorations
    
        frame.setLocationRelativeTo(null);
    
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                frame.dispose();
            }
        }, 
        3000); }

    private boolean isPumpConnectedToWaterFlowingPipe(Pump pump) {
        Set<Component> visited = new HashSet<>();
        return checkPumpConnectedToWaterFlowingPipe(pump, visited);
    }

    private boolean checkPumpConnectedToWaterFlowingPipe(Pump pump, Set<Component> visited) {
        if (pump == null || visited.contains(pump)) {
            return false;
        }

        visited.add(pump);

        Pipe incomingPipe = pump.getIncomingPipe();
        Pipe outgoingPipe = pump.getOutgoingPipe();

        if ((incomingPipe != null && incomingPipe.isWaterFlowing()) ||
            (outgoingPipe != null && outgoingPipe.isWaterFlowing())) {
            return true;
        }

        for (Component connectedComponent : pump.getConnectedComponents().values()) {
            if (connectedComponent instanceof Pump) {
                checkPumpConnectedToWaterFlowingPipe((Pump) connectedComponent, visited);
            }
        }

        return false;
    }
    
//     /**
//      * Starts sandstorm timers.
//      */
//     public void startSandstormTimers() 
// {
//     Timer timer = new Timer("SandstormTimer");

//     // Schedule the sandstorm to start after 15 seconds
//     timer.schedule(new TimerTask() {
//         public void run()
//         {
//             startSandstorm();
//         }
//     }, 15 * 1000);

//     // Cancel the timer after the sandstorm starts
//     timer.schedule(new TimerTask() {
//         public void run()
//         {
//             timer.cancel();
//         }
//     }, 16 * 1000); // Adjust the time accordingly

// }
    /**
     * Starts the game timer.
     */
     public int getPlumberScore(){
        return plumberScorer.getScore();
    }
    public int getSaboteurScore(){
        return saboteurScorer.getScore();
    }
    /**
     * Checks if all cisterns are full.
     * 
     * @return true if all cisterns are full, false otherwise
     */
    public boolean checkIfAllCisternsAreFull() 
    {
        for (Cistern cistern : map.getCisterns()) {
            if (!cistern.getIsCisternFull()) {return false;}
        }
        return true;
    }
    public void selectMapSize(int size) {
        switch (size) {
            case 8:
                map = new Map(8, 8);
                break;
            case 9:
                map = new Map(9, 9);
                break;
            case 10:
                map = new Map(10, 10);
                break;
            default:
                System.out.println("Invalid map size.");
                break;
        }
    }

     /**
     * Shows available maps.
     */
    // public void showMaps() 
    // {
    //     String message = "                === MAPS ===               \n\n\n"
    //                    + "Choose the map size:\n"
    //                    + "1. Small\n"
    //                    + "2. Medium\n"
    //                    + "3. Large\n"
    //                    + "4. Return to the main menu.\n\n";

    //     System.out.print(message);
    //     writeToOutputTxt(message);

    //     String inputText = "";
    //     int input = 0;

    //     MAPS_MENU_LOOP: do
    //     {
    //         try
    //         {
    //             inputText = receiveInput();
    //             input = Integer.parseInt(inputText);
    //         }
    //         catch (Exception exception)
    //         {
    //             String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
    //             System.out.print(message);
    //             writeToOutputTxt(message);
    //             try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
    //             continue;
    //         }

    //         switch (input)
    //         {
    //             case 1 -> {
    //                 map = new Map(5, 5);
    //             }
    //             case 2 -> {
    //                 map = new Map(10, 10);
    //             }
    //             case 3 -> {
    //                 map = new Map(15, 15);
    //             }
    //             case 4 -> {
    //                 break MAPS_MENU_LOOP;
    //             }
    //             default -> {
    //                 String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
    //                 System.out.print(message);
    //                 writeToOutputTxt(message);
    //                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
    //                 continue;
    //             }
    //         }
    //         showTeams();
    //     }
    //     while (input != 1 && input != 2 && input != 3);
        
    // }
    public void setActiveTeam(int teamIndex) {
        // activePlayer = teams.get(0).getPlayers().get(0);
        activePlumber = (Plumber) teams.get(0).getPlayers().get(0);
        activePlumber2 = (Plumber) teams.get(0).getPlayers().get(1);
        activeSaboteur = (Saboteur) teams.get(1).getPlayers().get(0);
        activeSaboteur2 = (Saboteur)teams.get(1).getPlayers().get(1);
    }

    /**
     * Shows available teams.
     */
//     public void showTeams()
// {
//     String message = "                === TEAMS ===               \n\n\n"
//                    + "1. Plumbers\n"
//                    + "2. Saboteurs\n"
//                    + "3. Return to the main menu.\n\n";

//     System.out.print(message);
//     writeToOutputTxt(message);

//     String inputText = "";
//     int input = 0;

//     TEAMS_MENU_LOOP: do
//     {
//         try
//         {
//             inputText = receiveInput();
//             input = Integer.parseInt(inputText);
//         }
//         catch (Exception exception)
//         {
//             String errorMessage = "\nPlease enter 1, 2, or 3.\n\n\n\n\n";
//             System.out.print(message);
//             writeToOutputTxt(message);
//             try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
//             continue;
//         }

//         switch (input)
//         {
//             case 1 -> {
//                 activePlayer = teams.get(0).getPlayers().get(0);
//                 activePlumber = (Plumber)teams.get(0).getPlayers().get(0);
//                 activeSaboteur = null;
//             }
//             case 2 -> {
//                 activePlayer = teams.get(1).getPlayers().get(0);
//                 activeSaboteur = (Saboteur)teams.get(1).getPlayers().get(0);
//                 activePlumber = null;
//             }
//             case 3 -> {
//                 break TEAMS_MENU_LOOP;
//             }
//             default -> {
//                 String errorMessage = "\nPlease enter 1, 2, or 3.\n\n\n\n\n";
//                 System.out.print(message);
//                 writeToOutputTxt(message);
//                 try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
//                 continue;
//             }
//         }
        
    //     // Start sandstorm timers after 15 seconds
    //     Timer timer = new Timer("SandstormTimer");
    //     timer.schedule(new TimerTask() {
    //         public void run()
    //         {
    //             startSandstorm();
    //         }
    //     }, 15 * 1000);

    //     startGame(); // Start the game after selecting a team
    // }
    // while (input != 1 && input != 2);


    /**
     * Receives input from the user.
     * 
     * @return the user's input
     */
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

    /**
     * Writes a message to the output.txt file.
     * 
     * @param message the message to write
     */
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

    /**
     * Cleans the output.txt file.
     */
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

    /**
     * Manufactures components periodically.
     */
    public void manufactureComponents()
    {
        Timer manufactureTimer = new Timer("ManufactureTimer");
        manufactureTimer.scheduleAtFixedRate(new TimerTask() {
            public void run()
            {
                System.out.println();
                for (Cistern cistern : map.getCisterns())
                {
                    cistern.manufactureComponent();
                }
            }
        }, 1000 * 10, 1000 * 10);
    }

    public Map getMap() {
        return map;
    }

    public Plumber getActivePlumber() {
        return activePlumber;
    }

    public Saboteur getActiveSaboteur() {
        return activeSaboteur;
    }

    public Plumber getActivePlumber2() {
        return activePlumber2;
    }

    public Saboteur getActiveSaboteur2() {
        return activeSaboteur2;
    }
}
