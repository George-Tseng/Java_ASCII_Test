package javaTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Get_Token {

    private static final String CreateTokenKey = "aAaAaAaAaAaAaAaAaAaAaAa";
    private static final File resultF = new File("appResult.txt");
    private static final int paraTime = 23;

    /**
     * 將參數寫入輸出檔的最前端
     *
     * @param encodeTime 加密後的原始時戳(String)
     * @param encodeMacAddress 加密後的原始網卡資訊(String)
     *
     * @return 正常運行時會傳回空字串(String)
     *
     * @see FileOutputStream
     * @see OutputStreamWriter
     * @see StandardCharsets
     * @see IOException
     */
    /*將驗證用的資訊寫入輸出檔*/
    protected static String writeToken(String encodeTime, String encodeMacAddress){
        FileOutputStream fos0;
        OutputStreamWriter osw0;
        String result = "";
        String tokenKey = encodeTime + "\n"+ encodeMacAddress + "\n" + Get_Key.getEncodeKey(encodeTime, encodeMacAddress, CreateTokenKey);
        try{
            fos0 = new FileOutputStream(resultF);
            osw0 = new OutputStreamWriter(fos0, StandardCharsets.UTF_8);

            osw0.write(tokenKey);
            osw0.flush();
            fos0.close();
            osw0.close();
        } catch(IOException IOE){
            result = IOE.getMessage();
        }
        return result;
    }
}
