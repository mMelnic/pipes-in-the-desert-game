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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import player.Plumber;
import player.PlumberScorer;
import player.Saboteur;
import player.SaboteurScorer;
import player.Team;
import javax.swing.*;

/**
 * The GameManager class manages the game's flow and logic.
 */
public class GameManager implements ICisternListener {
    private Map map;
    private List<Team> teams;
    private Plumber activePlumber = null;
    private Saboteur activeSaboteur = null;
    private Saboteur activeSaboteur2;
    private Plumber activePlumber2;
    private SaboteurScorer saboteurScorer = new SaboteurScorer();
    private PlumberScorer plumberScorer = new PlumberScorer();
    private MapWindow mapWindow;
    private ScheduledExecutorService sandstormScheduler = Executors.newScheduledThreadPool(1);
    private Random random = new Random();

    Scanner scanner = new Scanner(System.in);

    /**
     * Displays the key bindings for the game.
     */
    public void showKeyBindings() {
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
    public GameManager() {
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
        }
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        map.setPlumberScorer(plumberScorer);
        map.setSaboteurScorer(saboteurScorer);
        map.players.add(teams.get(0).getPlayers().get(0));
        map.players.add(teams.get(0).getPlayers().get(1));
        map.players.add(teams.get(1).getPlayers().get(0));
        map.players.add(teams.get(1).getPlayers().get(1));
        activeSaboteur.setCurrentCell(map.getCells(0, 0));
        map.getCells(0, 0).setPlayerOn(true);
        activeSaboteur2.setCurrentCell(map.getCells(7, 1));
        map.getCells(7, 1).setPlayerOn(true);

        // Put the plumber on row 1, column 3
        activePlumber.setCurrentCell(map.getCells(0, 3));
        map.getCells(0, 3).setPlayerOn(true);
        activePlumber2.setCurrentCell(map.getCells(7, 3));
        map.getCells(7, 3).setPlayerOn(true);

        map.setGameManager(this);

        map.initializeMap();

        for (Cistern cistern : map.getCisterns()) {
            cistern.addCisternFullListener(this);
        }

        manufactureComponents();
        startSandstorms();
    }


  
    public void openMenu() {
        MainWindow mainWindow = new MainWindow(this);
        mainWindow.show();
    }

    public void startSandstorms() {
        List<Long> sandstormTimes = new ArrayList<>();
        long previousTime = 0;
        for (int i = 0; i < 3; i++) {
            long randomTime = previousTime + random.nextInt(301) + 30;
            sandstormTimes.add(randomTime);
            previousTime = randomTime;
        }

        for (long delay : sandstormTimes) {
            sandstormScheduler.schedule(this::startSandstorm, delay, TimeUnit.SECONDS);
        }
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
                    if (!pump.isFilling() || !pump.isReservoirFull()) {
                        pump.fillReservoir();
                    }
                }
            }
        } else {
            Random random = new Random();
            Pump randomPump = pumps.get(random.nextInt(pumps.size()));
            if (!randomPump.isBroken()) {
                randomPump.setBroken(true);
                if (isPumpConnectedToWaterFlowingPipe(randomPump)) {
                    if (!randomPump.isFilling() || !randomPump.isReservoirFull()) {
                        randomPump.fillReservoir();
                    }
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
                2000);
    }

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

     public int getPlumberScore(){
        return plumberScorer.getScore();
    }

    public int getSaboteurScore() {
        return saboteurScorer.getScore();
    }

    /**
     * Checks if all cisterns are full.
     * 
     * @return true if all cisterns are full, false otherwise
     */
    public boolean checkIfAllCisternsAreFull() {
        for (Cistern cistern : map.getCisterns()) {
            if (!cistern.getIsCisternFull()) {
                return false;
            }
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

  
    public void setActiveTeam(int teamIndex) {
        // activePlayer = teams.get(0).getPlayers().get(0);
        activePlumber = (Plumber) teams.get(0).getPlayers().get(0);
        activePlumber2 = (Plumber) teams.get(0).getPlayers().get(1);
        activeSaboteur = (Saboteur) teams.get(1).getPlayers().get(0);
        activeSaboteur2 = (Saboteur)teams.get(1).getPlayers().get(1);
    }

    /**
     * Writes a message to the output.txt file.
     * 
     * @param message the message to write
     */
    public static void writeToOutputTxt(String message) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("output.txt", true);
            fileWriter.append(message);
        } catch (Exception exception) {
            System.out.println("ERROR!");
            exception.printStackTrace();
            System.out.println();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Cleans the output.txt file.
     */
    public static void cleanOutputTxt() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("output.txt", false);
            fileWriter.write("");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Manufactures components periodically.
     */
    public void manufactureComponents() {
        Timer manufactureTimer = new Timer("ManufactureTimer");
        manufactureTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println();
                for (Cistern cistern : map.getCisterns()) {
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
