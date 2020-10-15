package javaTest;

import java.util.TimerTask;

public class File_Timer extends TimerTask {
    public void run(){
        String timeNow = Get_Date.getDateNow();
        String tmpEncodeTime = ASCII_Translator.getMessageASCII(timeNow);
        String macAddressNow = Get_Mac_Address.getMacAddress();
        String tmpEncodeMacAddress = ASCII_Translator.getMessageASCII(macAddressNow);
        String timeNowEncode = Get_Key.getEncodeKeyLong(tmpEncodeTime, tmpEncodeMacAddress, timeNow);
        String tmpKey = tmpEncodeTime + System.lineSeparator() + tmpEncodeMacAddress + System.lineSeparator() + timeNowEncode;
        if(!File_Conf.writeTmpFile(tmpKey).equals("")){
            System.out.println(File_Conf.writeTmpFile(tmpKey));
        }
    }
}
