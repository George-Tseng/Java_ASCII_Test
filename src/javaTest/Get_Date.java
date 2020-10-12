package javaTest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Get_Date {
    /*時間格式*/
    private final static SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");

    protected static String getDateNow(){
        /*取得目前時間*/
        Date timeCurrent = new Date();
        /*格式化時間*/
        return sdf0.format(timeCurrent);
    }
}
