package models;

import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChallengeInfo {
    public int id;
    public int userId;
    public String username;
    public String mapName;
    public boolean isNormalDifficulty;
    public boolean isHardDifficulty;
    public boolean isHeroicDifficulty;
    public boolean isImpossibleDifficulty;
    public int startTime;
    public int endTime;

    public JSONObject toJsonObject() {
        JSONObject output = new JSONObject();

        output.put("id", id);
        output.put("username", username);
        output.put("userId", userId);
        output.put("mapName", mapName);
        output.put("isNomralDifficulty", isNormalDifficulty);
        output.put("isHardDifficulty", isHardDifficulty);
        output.put("isHeroicDifficulty", isHeroicDifficulty);
        output.put("isImpossibleDifficulty", isImpossibleDifficulty);
        output.put("startHour", startTime);
        output.put("endHour", endTime);
        return output;
    }

    public static ChallengeInfo fromResultSet(ResultSet rs) throws SQLException {
        ChallengeInfo info = new ChallengeInfo();
        //SELECT CH.Id as Id, UserId, Username, MapName, AllowNormal, AllowHard, AllowHeroic, AllowImpossible, StartingHour, EndingHour from Challenges CH inner join Users US on UserId = US.Id

        info.id = rs.getInt("Id");
        info.userId = rs.getInt("UserId");
        info.username = rs.getString("Username");
        info.mapName = rs.getString("MapName");
        info.isNormalDifficulty = rs.getBoolean("AllowNormal");
        info.isHardDifficulty = rs.getBoolean("AllowHard");
        info.isHeroicDifficulty = rs.getBoolean("AllowHeroic");
        info.isImpossibleDifficulty = rs.getBoolean("AllowImpossible");
        info.startTime = rs.getInt("StartingHour");
        info.endTime = rs.getInt("EndingHour");

        return info;
    }
}
