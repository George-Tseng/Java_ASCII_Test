package javaTest;

import java.util.TimerTask;

public class File_Timer extends TimerTask {
    public void run(){
        String timeNow = Get_Date.getDateNow();
        if(!File_Conf.writeTmpFile(timeNow).equals("")){
            System.out.println(File_Conf.writeTmpFile(timeNow));
        }
    }
}
