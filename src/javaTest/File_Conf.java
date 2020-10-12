package javaTest;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class File_Conf {
    /*固定設定檔、暫存檔等的路徑*/
    private final static File confF = new File("appConf.csv");
    private final static File tmpF = new File("appTmp.csv");
    private final static File resultF = new File("appResult.txt");

    /*建立設定檔*/
    protected static String createConfFile(){
        String status = "";
        try{
            if(!confF.createNewFile()){
                status = "建立失敗";
            }
        } catch(IOException IOE){
            status = IOE.getMessage();
        }
        return status;
    }

    /*寫入設定檔*/
    protected static String writeConfFile(String timeNow){
        FileOutputStream fos0;
        OutputStreamWriter osw0;
        String result = "";

        try{
            fos0 = new FileOutputStream(confF);
            osw0 = new OutputStreamWriter(fos0, StandardCharsets.UTF_8);

            osw0.write(timeNow);
            osw0.flush();
            fos0.close();
            osw0.close();
        } catch(IOException IOE){
            result = IOE.getMessage();
        }
        return result;
    }

    /*讀取設定檔*/
    protected static String readConfFile(){
        FileInputStream fis0;
        InputStreamReader isr0;
        int count;
        String result = "";

        try{
            fis0 = new FileInputStream(confF);
            isr0 = new InputStreamReader(fis0, StandardCharsets.UTF_8);
            StringBuilder sb0 = new StringBuilder();

            while((count = isr0.read()) != -1){
                char inputChar = (char)count;
                sb0.append(inputChar);
            }

            result = sb0.toString();
            fis0.close();
            isr0.close();
        } catch(IOException IOE){
            System.out.println(IOE.getMessage());
        }
        return result;
    }

    /*建立暫存檔*/
    protected static String createTmpFile(){
        String status = "";
        try{
            if(!tmpF.createNewFile()){
                status = "建立失敗";
            }
        } catch(IOException IOE){
            status = IOE.getMessage();
        }
        return status;
    }

    /*寫入暫存檔*/
    protected static String writeTmpFile(String timeNow){
        FileOutputStream fos0;
        OutputStreamWriter osw0;
        String result = "";

        try{
            fos0 = new FileOutputStream(tmpF);
            osw0 = new OutputStreamWriter(fos0, StandardCharsets.UTF_8);

            osw0.write(timeNow);
            osw0.flush();
            fos0.close();
            osw0.close();
        } catch(IOException IOE){
            result = IOE.getMessage();
        }
        return result;
    }

    /*讀取暫存檔*/
    protected static String readTmpFile(){
        FileInputStream fis0;
        InputStreamReader isr0;
        int count;
        String result = "";

        try{
            fis0 = new FileInputStream(tmpF);
            isr0 = new InputStreamReader(fis0, StandardCharsets.UTF_8);
            StringBuilder sb0 = new StringBuilder();

            while((count = isr0.read()) != -1){
                char inputChar = (char)count;
                sb0.append(inputChar);
            }

            result = sb0.toString();
            fis0.close();
            isr0.close();
        } catch(IOException IOE){
            System.out.println(IOE.getMessage());
        }
        return result;
    }

    /*程式結束時刪除暫存檔*/
    protected static String deleteTmpFile(){
        String status = "";
        try{
            tmpF.deleteOnExit();
        } catch(SecurityException SE){
            status = SE.getMessage();
        }
        return status;
    }

    /*立刻刪除暫存檔*/
    protected static String deleteTmpFileNow(){
        String status = "";
        try {
            if(!tmpF.delete()){
                status = "清除失敗";
            }
        } catch(SecurityException SE){
            status = SE.getMessage();
        }
        return status;
    }

    /*建立結果檔*/
    protected static String createResultFile(){
        String status = "";
        try{
            if(!resultF.createNewFile()){
                status = "建立失敗";
            }
        } catch(IOException IOE){
            status = IOE.getMessage();
        }
        return status;
    }

    /*寫入結果檔*/
    protected static String writeResultFile(String timeNow){
        FileOutputStream fos0;
        OutputStreamWriter osw0;
        String result = "";

        try{
            fos0 = new FileOutputStream(resultF);
            osw0 = new OutputStreamWriter(fos0, StandardCharsets.UTF_8);

            osw0.write(timeNow);
            osw0.flush();
            fos0.close();
            osw0.close();
        } catch(IOException IOE){
            result = IOE.getMessage();
        }
        return result;
    }

    /*讀取結果檔*/
    protected static String readResultFile(){
        FileInputStream fis0;
        InputStreamReader isr0;
        int count;
        String result = "";

        try{
            fis0 = new FileInputStream(resultF);
            isr0 = new InputStreamReader(fis0, StandardCharsets.UTF_8);
            StringBuilder sb0 = new StringBuilder();

            while((count = isr0.read()) != -1){
                char inputChar = (char)count;
                sb0.append(inputChar);
            }

            result = sb0.toString();
            fis0.close();
            isr0.close();
        } catch(IOException IOE){
            System.out.println(IOE.getMessage());
        }
        return result;
    }

    /*確認設定檔是否存在*/
    protected static boolean checkConfFile(){
        return confF.exists();
    }

    /*確認暫存檔是否存在*/
    protected static boolean checkTmpFile(){
        return tmpF.exists();
    }

    /*確認結果檔是否存在*/
    protected static boolean CheckResultFile() {return resultF.exists();}
}
