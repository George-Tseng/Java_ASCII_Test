package javaTest;

public class Get_Key {

    /**
     * 簡易加密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT;
     * MacAddress為M、字串長度為pM，，參數m為M * pM
     * 將輸入的字元轉換為K(ASCII)。
     * 運算：t + m + K，
     * 運算結果 / 128 -> 得到 商 與 餘數，
     * 每次輸入字元就轉換成 "商:餘數"，彼此以","隔開。
     *
     * @param inputTime 簡易加密過的時戳(String)
     * @param inputMacAddress  簡易加密過的網卡資訊(String)
     * @param inputKey 使用者輸入的訊息(String)
     *
     * @return 加密後的資訊(String)
     *
     * @see String#toCharArray()
     * @see Integer#parseInt(String)
     */
    protected static String getEncodeKey(String inputTime, String inputMacAddress, String inputKey){
        String result;

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
        for (int i = 0; i < keySpace.length; i++) {
            int paraInput = ASCII_Translator.getASCII(keySpace[i]);
            int paraT, paraM, paraTotal;

            if (keySpace.length <= paraTime) {
                paraT = Integer.parseInt(timeSpace[i + 1]);
            } else {
                paraT = Integer.parseInt(timeSpace[i % paraTime + 1]);
            }

            if (keySpace.length <= paraMacAddress) {
                paraM = Integer.parseInt(macAddressSpace[i + 1]);
            } else {
                paraM = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
            }

            paraTotal = paraTime * paraT + paraMacAddress * paraM + paraInput;
            paraResult = paraTotal / 128;
            paraRest = (byte) ((paraTotal) % 128);

            sb0.append(paraResult);
            sb0.append(":");
            sb0.append(paraRest);
            if (i < keySpace.length - 1) {
                sb0.append(",");
            }
        }
        result = sb0.toString();
        return result;
    }

    /**
     * getEncodeKey的解密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT
     * ;MacAddress為M、字串長度為pM，參數m為M * pM;
     * 先透過拆解","，還原出單組資料(商:餘數)。
     * 運算商 * 128 + 餘數 - t + m，
     * 結果即為原始輸入的K，最後再轉換回char
     *
     * @param inputTime 時戳(String)
     * @param inputMacAddress 網卡資訊(String)
     * @param inputText 先前加密後的資訊(String)
     *
     * @return 使用者輸入的原始訊息(String)
     *
     * @see String#split(String)
     * @see Integer#parseInt(String)
     */
    protected static String getDecodeKey(String inputTime, String inputMacAddress, String inputText){
        String result;

        /*字串轉換*/
        String inputParaT = ASCII_Translator.getMessageASCII(inputTime);
        String inputParaM = ASCII_Translator.getMessageASCII(inputMacAddress);

        /*用split拆裝String*/
        String [] timeSpace = inputParaT.split(",");
        String [] macAddressSpace = inputParaM.split(",");
        String [] textSpace = inputText.split(",");

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*反運算產物*/
        int paraResult, paraRest;
        byte paraKey;

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
            paraRest = Integer.parseInt(keySpace[1]);

            paraKey = (byte)((paraResult * 128 + paraRest) - (paraTime * paraT + paraMacAddress * paraM));
            sb0.append(ASCII_Translator.getChar(paraKey));
        }
        result = sb0.toString();
        return result;
    }

    /**
     * 中等加密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT;
     * MacAddress為M、字串長度為pM，，參數m為M * pM
     * 將輸入的字元轉換為K(ASCII)。
     * 運算 (t+k)/m + (t-k)/m + t/(m+k) + t/(m-k) + t/mk + tk/m，
     * 得出 商 跟 餘數，產生"商:餘數轉換成16進位"，每項一樣彼此以","隔開
     *
     * @param inputTime 簡易加密過的時戳(String)
     * @param inputMacAddress 簡易加密過的網卡資訊(String)
     * @param inputKey 使用者輸入的訊息(String)
     *
     * @return 中等加密後的資訊(String)
     *
     * @see String#toCharArray()
     * @see Integer#parseInt(String)
     * @see Math#pow(double, double)
     * @see Long#toHexString(long)
     */
    protected static String getEncodeKeyMid(String inputTime, String inputMacAddress, String inputKey){
        String result;

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
        result = sb0.toString();
        return result;
    }

    /**
     * getEncodeKeyMid的解密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT;
     * MacAddress為M、字串長度為pM，，參數m為M * pM
     * 先拆解","，再還原出單組資料(商:餘數)。
     * 然後用迴圈帶入的方式求出K，再轉回char
     *
     * @param inputTime 時戳(String)
     * @param inputMacAddress 網卡資訊(String)
     * @param inputText 先前中等加密後的資訊(String)
     *
     * @return 使用者輸入的原始訊息(String)
     *
     * @see String#split(String)
     * @see Integer#parseInt(String)
     * @see Long#parseLong(String)
     * @see Long#valueOf(String, int)
     * @see Math#pow(double, double)
     */
    protected static String getDecodeKeyMid(String inputTime, String inputMacAddress, String inputText){
        String result;

        /*字串轉換*/
        String inputParaT = ASCII_Translator.getMessageASCII(inputTime);
        String inputParaM = ASCII_Translator.getMessageASCII(inputMacAddress);

        /*用split拆裝String*/
        String [] timeSpace = inputParaT.split(",");
        String [] macAddressSpace = inputParaM.split(",");
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
        result = sb0.toString();
        return result;
    }

    /**
     * 較高強度的加密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT;
     * MacAddress為M、字串長度為pM，，參數m為M * pM
     * 將輸入的字元轉換為k(ASCII)。
     * 再對k進行以下處理：新K(k') = 小於等於k的最大質數 * 10 + k與該質數的差值，
     * 運算 (t+k')/m + (t-k')/m + t/(m+k') + t/(m-k') + t/mk' + tk'/m，
     * 得出 商 跟 餘數，產生"商:餘數先加上商後再轉換成16進位"，每項一樣彼此以","隔開
     *
     * @param inputTime 簡易加密過的時戳(String)
     * @param inputMacAddress 簡易加密過的網卡資訊(String)
     * @param inputKey 使用者輸入的訊息(String)
     *
     * @return 較高強度加密後的資訊(String)
     *
     * @see String#split(String)
     * @see Integer#parseInt(String)
     * @see Get_Prime_Number
     * @see Math#pow(double, double)
     * @see Long#toHexString(long)
     */
    protected static String getEncodeKeyLong(String inputTime, String inputMacAddress, String inputKey){
        String result;

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
            int paraInputTmp = ASCII_Translator.getASCII(keySpace[i]);
            int paraInputPrime = Get_Prime_Number.getMaxPrimeNumI(paraInputTmp);
            int paraInput = paraInputPrime * 10 + (paraInputTmp - paraInputPrime);

            int paraT, paraM;
            long paraTotalUp, paraTotalDown;

            if(keySpace.length <= paraTime){
                int paraTTmp = Integer.parseInt(timeSpace[i + 1]);
                int paraTPrime = Get_Prime_Number.getMaxPrimeNumI(paraTTmp);
                paraT = paraTPrime * 10 + (paraTTmp - paraTPrime);
            }
            else{
                int paraTTmp = Integer.parseInt(timeSpace[i % paraTime + 1]);
                int paraTPrime = Get_Prime_Number.getMaxPrimeNumI(paraTTmp);
                paraT = paraTPrime * 10 + (paraTTmp - paraTPrime);
            }

            if(keySpace.length <= paraMacAddress){
                int paraMTmp = Integer.parseInt(macAddressSpace[i + 1]);
                int paraMPrime = Get_Prime_Number.getMaxPrimeNumI(paraMTmp);
                paraM = paraMPrime * 10 + (paraMTmp - paraMPrime);
            }
            else{
                int paraMTmp = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
                int paraMPrime = Get_Prime_Number.getMaxPrimeNumI(paraMTmp);
                paraM = paraMPrime * 10 + (paraMTmp - paraMPrime);
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
        result = sb0.toString();
        return result;
    }

    /**
     * getEncodeKeyLong的解密流程：
     * 時戳為T、時戳字串長度為pT，參數t為T * pT;
     * MacAddress為M、字串長度為pM，，參數m為M * pM。
     * 先拆解","，再還原出單組資料(商:餘數)，
     * 對將以迴圈帶入的k進行以下處理：新K(k') = 小於等於k的最大質數 * 10 + k與該質數的差值，
     * 使用迴圈重複帶入k'，將符合條件的k轉換回char
     *
     * @param inputTime 時戳(String)
     * @param inputMacAddress 網卡資訊(String)
     * @param inputText 先前較高強度加密後的資訊(String)
     *
     * @return 使用者輸入的原始訊息(String)
     *
     * @see String#split(String)
     * @see Integer#parseInt(String)
     * @see Get_Prime_Number
     * @see Long#parseLong(String) 
     * @see Long#valueOf(String, int) 
     * @see Math#pow(double, double) 
     */
    protected static String getDecodeKeyLong(String inputTime, String inputMacAddress, String inputText){
        String result;

        /*字串轉換*/
        String inputParaT = ASCII_Translator.getMessageASCII(inputTime);
        String inputParaM = ASCII_Translator.getMessageASCII(inputMacAddress);

        /*用split拆裝String*/
        String [] timeSpace = inputParaT.split(",");
        String [] macAddressSpace = inputParaM.split(",");
        String [] textSpace = inputText.split(",");

        /*參數*/
        int paraTime = Integer.parseInt(timeSpace[0]);
        int paraMacAddress = Integer.parseInt(macAddressSpace[0]);

        /*反運算參數*/
        long paraResult, paraRest;

        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();

        /*利用迴圈執行轉換*/
        for(int i = 0; i < textSpace.length; i++){
            /*用split拆裝String*/
            String [] keySpace = textSpace[i].split(":");

            int paraT, paraM;
            long paraTotalUp, paraTotalDown;

            if (textSpace.length <= paraTime) {
                int paraTTmp = Integer.parseInt(timeSpace[i + 1]);
                int paraTPrime = Get_Prime_Number.getMaxPrimeNumI(paraTTmp);
                paraT = paraTPrime * 10 + (paraTTmp - paraTPrime);
            } else {
                int paraTTmp = Integer.parseInt(timeSpace[i % paraTime + 1]);
                int paraTPrime = Get_Prime_Number.getMaxPrimeNumI(paraTTmp);
                paraT = paraTPrime * 10 + (paraTTmp - paraTPrime);
            }

            if (textSpace.length <= paraMacAddress) {
                int paraMTmp = Integer.parseInt(macAddressSpace[i + 1]);
                int paraMPrime = Get_Prime_Number.getMaxPrimeNumI(paraMTmp);
                paraM = paraMPrime * 10 + (paraMTmp - paraMPrime);
            } else {
                int paraMTmp = Integer.parseInt(macAddressSpace[i % paraMacAddress + 1]);
                int paraMPrime = Get_Prime_Number.getMaxPrimeNumI(paraMTmp);
                paraM = paraMPrime * 10 + (paraMTmp - paraMPrime);
            }

            paraResult = Long.parseLong(keySpace[0]);
            paraRest = Long.valueOf(keySpace[1], 16) - paraResult;

            for(int j = 0; j < 128; j++){
                int jPrime = Get_Prime_Number.getMaxPrimeNumI(j);
                int jTmp = jPrime * 10 + (j - jPrime);
                long paraTmp = (long)(Math.pow((paraMacAddress * paraM), 2) - Math.pow(jTmp, 2));
                paraTotalUp = (long)((2 * (paraTime * paraT) * Math.pow((paraMacAddress * paraM), 2) * jTmp)
                        + (2 * jTmp + 1) * (paraTime * paraT) * paraTmp
                        + (Math.pow(jTmp, 2) * (paraTime * paraT) * paraTmp));
                paraTotalDown = paraMacAddress * paraM * jTmp * (paraTmp);

                if(paraTotalDown != 0){
                    if((paraResult == paraTotalUp / paraTotalDown) && (paraRest == paraTotalUp % paraTotalDown)){
                        sb0.append(ASCII_Translator.getChar(j));
                        break;
                    }
                }
            }
        }
        result = sb0.toString();
        return result;
    }
}
