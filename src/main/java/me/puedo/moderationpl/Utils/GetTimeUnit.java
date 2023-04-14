package me.puedo.moderationpl.Utils;

import java.util.List;

public class GetTimeUnit {

    public long getTimeUnitFromShortcut(String s, Long duration){
        List<String> units = BanUnit.getUnitsAsString();
        if(units.contains(s.replaceAll("\\d", ""))) {
            BanUnit unit = BanUnit.getUnit(s.replaceAll("\\d", ""));
            long seconds = duration * unit.getToSecond();
            return seconds;
        }
        return 0;
    }
}
