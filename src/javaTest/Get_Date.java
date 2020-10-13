package javaTest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Get_Date {
    /*時間格式*/
    private final static SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");

    /**
     * 以格式化的字串型式傳回某時間點的時間
     *
     * @return 格式化後的時間(String)
     *
     * @see Date
     * @see SimpleDateFormat
     */
    protected static String getDateNow(){
        /*取得目前時間*/
        Date timeCurrent = new Date();
        /*格式化時間*/
        return sdf0.format(timeCurrent);
    }
}
