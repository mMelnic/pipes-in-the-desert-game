package system;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import player.Team;


public class GameManager 
{
    private List<Map> maps;
    private Map map;
    private Timer timer;
    private int plumbersScore;
    private int saboteursScore;
    private List<Team> teams;
    private boolean isTimeUp;


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
    }

    public void startGame() 
    {
        timer = new Timer("GameTimer");
        timer.schedule(new TimerTask() {
            public void run()
            {
                isTimeUp = true;
            }
        }, 3 * 60 * 1000);
        teams.add(new Team())
        while (!isTimeUp)
        {
            
        }
    }

    public void endGame() 
    {

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

    }

    public int compareScore() 
    {
        return 0;
    }

    public boolean checkIfAllCisternsAreFull() 
    {
        return false;
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
                    map = maps.get(0);
                }
                case 2 -> {
                    map = maps.get(1);
                }
                case 3 -> {
                    map = maps.get(2);
                }
                case 4 -> {
                    break MAPS_MENU_LOOP;
                }
                default -> {
                    if (input != 1 && input != 2 && input != 3 && input != 4)
                    {
                        String errorMessage = "\nPlease enter 1, 2, 3, or 4.\n\n\n\n\n";
                        System.out.print(message);
                        writeToOutputTxt(message);
                        try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    }
                    else
                    {
                        startGame();
                    }
                }
            }
        }
        while (input != 1 && input != 2 && input != 3);
        
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
                    System.out.println("Incorrect input. Please enter either 1 or 2.");
                    try {Thread.sleep(1500);} catch (InterruptedException interruptedException) {}
                    System.out.println("\n\n");
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
