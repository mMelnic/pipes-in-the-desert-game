package player;

import components.Pipe;

import java.util.Date;

public class Saboteur extends MovablePlayer {

    /**
     * Allows the saboteur to puncture a pipe, causing water leakage and hindering the progress of the plumber team.
     * @return boolean
     */
    public Saboteur(Team team){
        super(team);
    }

    public boolean puncturePipe(){
        if (!currentCell.isEmpty){
            if (currentCell.getComponent() instanceof Pipe){
                Pipe pipe = (Pipe) currentCell.getComponent();
                if (!pipe.isBroken()){
                    pipe.startLeaking();
                    pipe.setBroken(true);
                    pipe.setWaterFlowing(true);
                    return true;
                }else {return false;}
            }else {return false;}
        }else {return false;}
    }
}
