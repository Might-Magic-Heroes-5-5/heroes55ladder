package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LadderPriorities {
    public int id;
    public int userId;
    public String vetoedMaps;
    public boolean[] daysCanPlay;
    public int[] startHoursOfDay;
    public int[] endHoursOfDay;

    public LadderPriorities() {

    }

    public JSONObject toJson() {
        JSONObject output = new JSONObject();

        output.put("id", id);
        output.put("userId", userId);
        output.put("vetoedMaps", vetoedMaps);
        output.put("daysCanPlay", arrayFromBoolArray(daysCanPlay));
        output.put("startHoursOfDay", arrayFromIntArray(startHoursOfDay));
        output.put("endHoursOfDay", arrayFromIntArray(endHoursOfDay));

        return output;
    }

    public static JSONArray arrayFromIntArray(int[] intArray) {
        JSONArray output = new JSONArray();

        for(int i = 0; i < intArray.length; i++)
            output.add(i, intArray[i]);

        return output;
    }

    public static JSONArray arrayFromBoolArray(boolean[] boolArray) {
        JSONArray output = new JSONArray();

        for(int i = 0; i < boolArray.length; i++)
            output.add(i, boolArray[i]);

        return output;
    }

    public static LadderPriorities fromResultSet(ResultSet rs) throws SQLException {
        LadderPriorities output = new LadderPriorities();

        output.id = rs.getInt("Id");
        output.userId = rs.getInt("UserId");
        output.vetoedMaps = rs.getString("VetoedMaps");
        String daysCanPlayStr = rs.getString("DaysCanPlay");
        String startHourOfDayStr = rs.getString("StartHourOfDay");
        String endHourOfDayStr = rs.getString("EndHourOfDay");

        output.daysCanPlay = boolArrayFromStr(daysCanPlayStr);
        output.startHoursOfDay = intArrayFromStr(startHourOfDayStr);
        output.endHoursOfDay = intArrayFromStr(endHourOfDayStr);

        return output;
    }

    private static int[] intArrayFromStr(String hourOfDayStr) {
        String[] hourPerDay = hourOfDayStr.split(",");
        int[] output = new int[hourPerDay.length];

        for(int i = 0; i < hourPerDay.length; i++)
            output[i] = Integer.parseInt(hourPerDay[i]);

        return output;
    }

    private static boolean[] boolArrayFromStr(String daysCanPlayStr) {
        String[] canPlayPerDay = daysCanPlayStr.split(",");
        boolean[] output = new boolean[canPlayPerDay.length];

        for(int i = 0; i < canPlayPerDay.length; i++)
            output[i] = canPlayPerDay[i].compareTo("1") == 0;

        return output;
    }
}
