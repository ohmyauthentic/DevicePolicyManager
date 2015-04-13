package com.candy.mergepart1;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by candy on 2015/4/13.
 */
public class Tool {
    private final static Format dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static NumberFormat numberFormat = new DecimalFormat("0000");
    private static int seq = 0;
    private static final int MAX = 9999;
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
    public static synchronized String generateSequenceNo() {
        Calendar rightNow = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
        numberFormat.format(seq, sb, HELPER_POSITION);
        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
