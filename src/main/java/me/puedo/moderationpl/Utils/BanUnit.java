package me.puedo.moderationpl.Utils;

import java.util.ArrayList;
import java.util.List;

public enum BanUnit {

    SECOND("Second(s)", 1, "s"),
    MINUTE("Minute(s)", 60, "m"),
    HOUR("Hour(s)", 60*60, "h"),
    DAY("Day(s)", 24*60*60, "d"),
    WEEK("Week(s)", 7*24*60*60, "w");

    private String name;
    private int toSecond;
    private String shortcut;

    BanUnit(String name, int toSecond, String shortcut) {
        this.name = name;
        this.toSecond = toSecond;
        this.shortcut = shortcut;
    }

    public int getToSecond() {
        return toSecond;
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static List<String> getUnitsAsString() {
        List<String> units = new ArrayList<String>();
        for(BanUnit unit : BanUnit.values()) {
            units.add(unit.getShortcut().toLowerCase());
        }
        return units;
    }

    public static BanUnit getUnit(String unit) {
        for(BanUnit units : BanUnit.values()) {
            if(units.getShortcut().toLowerCase().equals(unit.toLowerCase())) {
                return units;
            }
        }
        return null;
    }


}
