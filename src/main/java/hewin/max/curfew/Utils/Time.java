package hewin.max.curfew.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

    public Integer GetTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Integer time = Integer.valueOf(format.format(date).replace(":", ""));
        return time;
    }

}
