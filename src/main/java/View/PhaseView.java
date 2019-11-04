package View;

import Model.GamePlay;

public class PhaseView implements IObserver{
    String header=          "______________________________________"  + "\n" +
                            "_______________PHASE VIEW_____________";
    String footer=          "______________________________________";


    String data;
    @Override
    public void update(GamePlay gamePlay) {
        data= "Current State is :  "+gamePlay.getCurrentState().toString();
        try {
            data += "\nCurrent Player is: " + gamePlay.getCurrentPlayerName();
        }catch (Exception e){
            //System.out.println("Skip above statement as player isn't initilized yet");
        }
        try {
            data += "\nOperation: " + gamePlay.getCurrentOperation();
        }catch (Exception e){
            //System.out.println("Skip above statement as player isn't initilized yet");
        }

        System.out.println(header + "\n" + data+"\n"+footer);
    }

    public void printPhaseView(){
        System.out.println(header + "\n" + data+"\n"+footer);

    }
}

