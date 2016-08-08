package com.parcka.xtr100.app2_proyect2_diplomado.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XTR100 on 07/08/2016.
 */
public class ParseTime {

    public static Map getMinutesWithSeconds(int totalSeconds) {
        HashMap time = new HashMap();
        int numberTemp;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds - (3600 * hours)) / 60;
        int seconds = totalSeconds - ((hours * 3600) + (minutes * 60));

        time.put("hours",hours);
        time.put("minutes",minutes);
        time.put("seconds",seconds);

        return time;
    }


}
