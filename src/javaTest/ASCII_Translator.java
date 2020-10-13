package javaTest;

public class ASCII_Translator {

    /**
     *
     * @param inputChar 輸入字元(char)
     * @return ASCII(int)
     */
    protected static int getASCII(char inputChar){
        return inputChar;
    }

    /**
     *
     * @param inputByte 輸入ASCII(int)
     * @return 字元(char)
     */
    public static char getChar(int inputByte){
        return (char)inputByte;
    }

    /**
     *
     * @param inputParas 輸入資訊(String)
     * @return  轉換成ASCII後的資訊(String)
     */
    protected static String getMessageASCII(String inputParas){
        /*用toCharArray方法把匯入的String拆成Char陣列*/
        char [] parasSpace = inputParas.toCharArray();
        /*根據Char陣列的大小生成對應用來裝ASCII的陣列*/
        int [] parasEncode = new int[parasSpace.length];
        /*生成StringBuilder物件*/
        StringBuilder sb0 = new StringBuilder();
        /*先存入整個String的長度在最前頭*/
        sb0.append(parasEncode.length);

        /*用迴圈進行逐個字元的轉換*/
        for(int i = 0; i < parasEncode.length; i++) {
            /*用逗號隔開每一項*/
            sb0.append(",");
            /*使用getASCII()方法轉換*/
            parasEncode[i] = getASCII(parasSpace[i]);
            /*填入StringBuilder*/
            sb0.append(parasEncode[i]);
        }
        /*用toString()方法讓StringBuilder轉回String*/
        return sb0.toString();
    }

    /**
     *
     * @param inputParas 輸入資訊(String)
     * @return 轉換回字元的資訊(String)
     */
    protected static String getMessageChar(String inputParas){
        /*用split拆裝String*/
        String [] parasSpace = inputParas.split(",");
        /*先解開陣列的第一項，作為實際字串的長度*/
        char [] parasDecode = new char[Integer.parseInt(parasSpace[0])];
        /*用迴圈進行逐個字元的轉換*/
        for(int i = 0; i < parasDecode.length; i++){
            /*使用getChar()方法轉換*/
            parasDecode[i] = getChar(Byte.parseByte(parasSpace[i+1]));
        }
        /*new String(Char Array) = String*/
        return new String(parasDecode);
    }
}
