package javaTest;

public class Get_Key {

    public static String getEncodeKey(String inputTime, String inputMacAddress, String inputKey){
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
            int paraT, paraM;

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

            paraResult = (paraTime * paraT + paraMacAddress * paraM + paraInput) / 128;
            paraRest = (byte)((paraTime * paraT + paraMacAddress * paraM + paraInput) % 128);

            sb0.append(paraResult);
            sb0.append(":");
            //sb0.append(ASCII_Translator.getChar(paraRest));
            sb0.append(paraRest);
            if(i < keySpace.length - 1){
                sb0.append(",");
            }
        }

        return sb0.toString();
    }
    public static String getDecodeKey(String inputTime, String inputMacAddress, String inputText){
        /*用split拆裝String*/
        String [] timeSpace = inputTime.split(",");
        String [] macAddressSpace = inputMacAddress.split(",");
        String [] textSpace = inputText.split(",");

        /*產生暫存的陣列*/

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

        return sb0.toString();
    }
}
