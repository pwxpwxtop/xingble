package pwx.util.format;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 时间转换
 * @author: pwx
 * @data: 2022/11/3 22:30
 * @version: 1.0
 */
public class Time {

    /**
     * 时间转字符串
     * @param date
     * @return  str
     */
    public static String toDatetime_Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 字符转datetime
     * @param datetime
     * @return date
     */
    public static Date toStr_Datetime(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间转字符串
     * @param date
     * @return  str
     */
    public static String toTime_Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    /**
     * 字符转time HH:mm:ss
     * @param datetime 11:34:22
     * @return date
     */
    public static Date toStr_Time(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            return format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间转字符串
     * @param date
     * @return  str
     */
    public static String toDate_Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 字符转time yyyy-MM-dd
     * @param datetime 11:34:22
     * @return date
     */
    public static Date toStr_Date(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



}
