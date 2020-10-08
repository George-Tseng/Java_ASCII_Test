package javaTest;

import java.text.ParseException;

public class Get_Key {

    protected static String getEncodeKey(String inputTime, String inputMacAddress, String inputKey) throws ParseException {
        /*計時用*/
        String timeStart = Get_Date.getDateNow();
        String timeEnd;

        /*用split拆裝String*/
        String [] timeSpace = inputTime.split(",");
        String [] macAddressSpace = inputMacAddress.split(",");

        /*用toCharArray方法把匯入的String拆成Char陣列*/
        char [] keySpace = inputKey.toCharArray();

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*運算產物*/
        int paraResult;
        byte paraRest;

        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();

        /*利用迴圈執行轉換*/
        for(int i = 0; i < keySpace.length; i++){
            int paraInput = ASCII_Translator.getASCII(keySpace[i]);
            int paraT, paraM, paraTotal;

            if(keySpace.length <= paraTime){
                paraT = Integer.parseInt(timeSpace[i + 1]);
            }
            else{
                paraT = Integer.parseInt(timeSpace[i % paraTime + 1]);
            }

            if(keySpace.length <= paraMacAddress){
                paraM = Integer.parseInt(macAddressSpace[i + 1]);
            }
            else{
                paraM = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
            }

            paraTotal = paraTime * paraT + paraMacAddress * paraM + paraInput;
            paraResult = paraTotal / 128;
            paraRest = (byte)((paraTotal) % 128);

            sb0.append(paraResult);
            sb0.append(":");
            //sb0.append(ASCII_Translator.getChar(paraRest));
            sb0.append(paraRest);
            if(i < keySpace.length - 1){
                sb0.append(",");
            }
        }
        timeEnd = Get_Date.getDateNow();
        System.out.println(Get_Date.getTimeDifference(timeStart, timeEnd));

        return sb0.toString();
    }
    protected static String getDecodeKey(String inputTime, String inputMacAddress, String inputText) throws ParseException {
        /*計時用*/
        String timeStart = Get_Date.getDateNow();
        String timeEnd;

        /*用split拆裝String*/
        String [] timeSpace = inputTime.split(",");
        String [] macAddressSpace = inputMacAddress.split(",");
        String [] textSpace = inputText.split(",");

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*反運算產物*/
        int paraResult, paraRest;
        byte paraKey;
        //char paraText;

        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();

        /*利用迴圈執行轉換*/
        for(int i = 0; i < textSpace.length; i++){
            /*用split拆裝String*/
            String [] keySpace = textSpace[i].split(":");

            int paraT, paraM;

            if(textSpace.length <= paraTime){
                paraT = Integer.parseInt(timeSpace[i + 1]);
            }
            else{
                paraT = Integer.parseInt(timeSpace[i % paraTime + 1]);
            }

            if(textSpace.length <= paraMacAddress){
                paraM = Integer.parseInt(macAddressSpace[i + 1]);
            }
            else{
                paraM = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
            }

            paraResult = Integer.parseInt(keySpace[0]);
            //paraText = keySpace[1].charAt(0);
            //paraRest = ASCII_Translator.getASCII(paraText);
            paraRest = Integer.parseInt(keySpace[1]);

            paraKey = (byte)((paraResult * 128 + paraRest) - (paraTime * paraT + paraMacAddress * paraM));
            sb0.append(ASCII_Translator.getChar(paraKey));
        }
        timeEnd = Get_Date.getDateNow();
        System.out.println(Get_Date.getTimeDifference(timeStart, timeEnd));

        return sb0.toString();
    }

    protected static String getEncodeKeyMid(String inputTime, String inputMacAddress, String inputKey) throws ParseException {
        /*計時用*/
        String timeStart = Get_Date.getDateNow();
        String timeEnd;

        /*用split拆裝String*/
        String [] timeSpace = inputTime.split(",");
        String [] macAddressSpace = inputMacAddress.split(",");

        /*用toCharArray方法把匯入的String拆成Char陣列*/
        char [] keySpace = inputKey.toCharArray();

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*運算產物*/
        long paraResult, paraRest;

        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();

        /*利用迴圈執行轉換*/
        for(int i = 0; i < keySpace.length; i++){
            int paraInput = ASCII_Translator.getASCII(keySpace[i]);
            int paraT, paraM;
            long paraTotalUp, paraTotalDown;

            if(keySpace.length <= paraTime){
                paraT = Integer.parseInt(timeSpace[i + 1]);
            }
            else{
                paraT = Integer.parseInt(timeSpace[i % paraTime + 1]);
            }

            if(keySpace.length <= paraMacAddress){
                paraM = Integer.parseInt(macAddressSpace[i + 1]);
            }
            else{
                paraM = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
            }

            long paraTmp = (long)(Math.pow((paraMacAddress * paraM), 2) - Math.pow(paraInput, 2));
            paraTotalUp = (long)((2 * (paraTime * paraT) * Math.pow((paraMacAddress * paraM), 2) * paraInput)
                    + (2 * paraInput + 1) * (paraTime * paraT) * paraTmp
                    + (Math.pow(paraInput, 2) * (paraTime * paraT) * paraTmp));
            paraTotalDown = paraMacAddress * paraM * paraInput * (paraTmp);

            paraResult = paraTotalUp / paraTotalDown;
            paraRest = paraTotalUp % paraTotalDown + paraResult;

            sb0.append(paraResult);
            sb0.append(":");
            sb0.append(Long.toHexString(paraRest));
            if(i < keySpace.length - 1){
                sb0.append(",");
            }
        }
        timeEnd = Get_Date.getDateNow();
        System.out.println(Get_Date.getTimeDifference(timeStart, timeEnd));

        return sb0.toString();
    }

    protected static String getDecodeKeyMid(String inputTime, String inputMacAddress, String inputText) throws ParseException {
        /*計時用*/
        String timeStart = Get_Date.getDateNow();
        String timeEnd;

        /*用split拆裝String*/
        String [] timeSpace = inputTime.split(",");
        String [] macAddressSpace = inputMacAddress.split(",");
        String [] textSpace = inputText.split(",");

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*反運算參數*/
        long paraResult, paraRest;

        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();

        /*利用迴圈執行轉換*/
        for(int i = 0; i < textSpace.length; i++) {
            /*用split拆裝String*/
            String [] keySpace = textSpace[i].split(":");

            int paraT, paraM;
            long paraTotalUp, paraTotalDown;

            if (textSpace.length <= paraTime) {
                paraT = Integer.parseInt(timeSpace[i + 1]);
            } else {
                paraT = Integer.parseInt(timeSpace[i % paraTime + 1]);
            }

            if (textSpace.length <= paraMacAddress) {
                paraM = Integer.parseInt(macAddressSpace[i + 1]);
            } else {
                paraM = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
            }

            paraResult = Long.parseLong(keySpace[0]);
            paraRest = Long.valueOf(keySpace[1], 16) - paraResult;

            for(int j = 0; j < 128; j++){
                long paraTmp = (long)(Math.pow((paraMacAddress * paraM), 2) - Math.pow(j, 2));
                paraTotalUp = (long)((2 * (paraTime * paraT) * Math.pow((paraMacAddress * paraM), 2) * j)
                        + (2 * j + 1) * (paraTime * paraT) * paraTmp
                        + (Math.pow(j, 2) * (paraTime * paraT) * paraTmp));
                paraTotalDown = paraMacAddress * paraM * j * (paraTmp);

                if(paraTotalDown != 0){
                    if((paraResult == paraTotalUp / paraTotalDown) && (paraRest == paraTotalUp % paraTotalDown)){
                        sb0.append(ASCII_Translator.getChar(j));
                        break;
                    }
                }
            }
        }
        timeEnd = Get_Date.getDateNow();
        System.out.println(Get_Date.getTimeDifference(timeStart, timeEnd));

        return sb0.toString();
    }
}
