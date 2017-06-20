package cn.keepfight.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * QS 管理软件相关工具
 * Created by tom on 2017/6/20.
 */
public class QSUtil {

    /**
     * 订单号格式
     */
    private static SimpleDateFormat orderSerialFormatter = new SimpleDateFormat("yyMMdd");

    public static String orderSerial(Long stamp, Long ct){
        return orderSerialFormatter.format(new Date(stamp))+String.format("%03d", ct);
    }
}
