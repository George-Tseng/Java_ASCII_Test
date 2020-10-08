package javaTest;

import java.text.ParseException;
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

    protected static String getTimeDifference(String timeStart, String timeEnd) throws ParseException {
        Date start = sdf0.parse(timeStart);
        Date end = sdf0.parse(timeEnd);

        long l =  end.getTime() - start.getTime();
        long day =  l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = ((l / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = l - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;

        StringBuilder sb0 = new StringBuilder();
        sb0.append("共花費： ");

        if(day != 0){
            sb0.append(day).append(" 天 ");
        }
        if(hour != 0){
            sb0.append(hour).append(" 小時 ");
        }
        if(min != 0){
            sb0.append(min).append(" 分 ");
        }
        if(s != 0){
            sb0.append(s).append(" 秒 ");
        }
        sb0.append(ms).append(" 毫秒");

        return sb0.toString();
    }
}
