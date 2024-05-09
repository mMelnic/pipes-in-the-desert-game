package player;

import components.Pipe;
import java.io.FileWriter;
import java.io.IOException;

public class Saboteur extends MovablePlayer {
    /**
     * Allows the saboteur to puncture a pipe, causing water leakage and hindering the progress of the plumber team.
     *
     * @return boolean
     */
    public Saboteur(Team team) {
        super(team);
    }

    public boolean puncturePipe() {
        try {
            FileWriter myWriter = new FileWriter("output.txt", true);
            myWriter.append("c - cistern; p - pipe; x - pump; s - spring\n");

            if (!currentCell.isEmpty) {
                if (currentCell.getComponent() instanceof Pipe) {
                    Pipe pipe = (Pipe) currentCell.getComponent();
                    if (!pipe.isBroken()) {
                        pipe.startLeaking();
                        pipe.setBroken(true);
                        pipe.setWaterFlowing(true);
                        pipe.stopFlow();
                        return true;
                    } else {
                        System.out.print("couldn't puncture, because pipe is already broken");
                        return false;
                    }
                } else {
                    System.out.print("couldn't puncture, because cell is not a pipe");
                    return false;
                }
            } else {
                System.out.print("couldn't puncture, because cell is empty");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.print("couldn't write to file, so function was aborted");
        return false;
    }
}
