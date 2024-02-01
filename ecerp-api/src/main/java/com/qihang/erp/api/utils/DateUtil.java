package com.qihang.erp.api.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 时间工具类
 * pbd add 2018/12/21 10:54
 */
public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateFormat_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前日期的间隔日期(负数为之前，正数为之后)
     *
     * @return
     */
    public static Date getIntervalDate(Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num);//得到前3个月
        return calendar.getTime();
    }
    public static String timeStampToDate(Long timestamp) {
        if(StringUtils.isEmpty(timestamp)) return null;
        Long times = timestamp * 1000;
        String date = dateFormat.format(new Date(times));
        return date;
    }
    /**
     * 获取当前月份
     *
     * @return
     */
    public static String getDateMonth() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentDate() {
        try {
            return dateFormat.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }
    public static String getCurrentDateTime() {
        try {
            return dateFormat_.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前日期的前几天时间
     * @param dayCount
     * @return
     */
    public static String customizeDate(Integer dayCount){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -dayCount); //得到前一天
        Date date = calendar.getTime();
        return dateFormat_.format(date);
    }

    public static String customizeDate_(Integer dayCount){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -dayCount); //得到前一天
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }


    /**
     * 获取指定日期的后1天
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(specifiedDay));
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
            return dateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 时间转换为时间戳
     *
     * @param s
     * @return
     */
    public static Integer strToStamp(String s) {
        Integer res = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt1 = simpleDateFormat.parse(s);
            res = (int) (dt1.getTime() / 1000);
        } catch (Exception e) {
            System.out.println("时间转换时间戳异常信息：" + e);
        }
        return res;
    }

    /**
     * 时间戳转换成时间
     *
     * @param s
     * @return
     */
    public static String stampToDateTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt =Long.parseLong(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 时间戳转换成时间
     *
     * @param stamp 时间戳
     * @return
     */
    public static String stampToDateTime(Long stamp) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(stamp * 1000);
            res = simpleDateFormat.format(date);
            return res;
        }catch (Exception e){
            return "";
        }
    }

    /**
     * 时间戳转换成时间
     *
     * @param s
     * @return
     */
    public static String stampToDateTime2(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = Long.parseLong(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Integer dateToStamp(String dateStr) {
        Integer res = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(dateStr);
            res = (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Integer dateTimeToStamp(String time) {
        Integer res = 0;
        if(StringUtils.isEmpty(time) || time.equals("0"))return res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            res = (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public static Date beforeDayDate(Integer day){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -day);
        //String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        return now.getTime();
    }


    public static Long aliDateToLong(String time) {
        Long res = 0L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = simpleDateFormat.parse(time);
            res = (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Long dateToStamp(Date time) {
        if (StringUtils.isEmpty(time)) return 0l;
        Long res = 0l;
        res = (time.getTime() / 1000);
        return res;
    }

    /**
     * DATE 转 Long yyyy-MM-dd
     *
     * @param s
     * @return
     */
    public static Long strToLong(String s) {
        if(StringUtils.isEmpty(s))return 0L;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（毫秒）
        return date.getTime() / 1000;
    }

    /**
     * String 转 Long yyyy-MM-dd HH:mm:ss
     *
     * @param s
     * @return
     */
    public static Long strToLongGo(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //日期转时间戳（毫秒）
        return date.getTime() / 1000;
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    public static Date stringtoDate(String dateStr) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /*
     * 将unix时间戳转换成时间格式字符串
     * @param timestampString 秒（除了1000的结果）
     * @return
     */
    public static String unixTimeStampToDate(long timestampString) {
        if (StringUtils.isEmpty(timestampString)) return "";
        Long timestamp = timestampString * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
        return date;
    }

    /*
     * 将unix时间戳转换成时间格式字符串
     * @param timestampString 秒（除了1000的结果）
     * @return
     */
    public static String unixTimeStampToDate2(long timestampString,String Format) {
        if (timestampString == 0) return null;
        Long timestamp = timestampString * 1000;
        String date = new SimpleDateFormat(Format).format(new Date(timestamp));
        return date;
    }

    public static String unixTimeStampToDate(long timestampString, String format) {
        if (timestampString == 0) return null;
        Long timestamp = timestampString * 1000;
        String date = new SimpleDateFormat(format).format(new Date(timestamp));
        return date;
    }


    /**
     * 把日期转换为字符串
     */
    public static String dateToString(Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    public static Date unixTimeToDate(Long time) {
        try {
            //String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
            return new Date(time*1000);   //对应的就是时间戳对应的Date
        } catch (Exception e) {
            return null;
        }
    }




    public static void main(String[] args) {
        // System.out.println(dateTimeToStamp("2021-06-08 00:41:23"));

        Long endTime=System.currentTimeMillis() / 1000;
        System.out.println(endTime-60 * 60 *24*7);
        System.out.println(getSpecifiedDayAfter("2021-08-23"));
     }

    /**
     * 时间转时间戳
     **/
    public static Long dataToString(String payTime) {
        if (payTime == null) {
            return 0L;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMddHHmmssSSSZ").parse(payTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }


}
