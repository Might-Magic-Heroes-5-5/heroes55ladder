package models;

import java.sql.*;

public class OngoingLadderMatch {
    public int id;
    public int user1;
    public String username1;
    public int user2;
    public String username2;
    public String mapPlayed;
    public boolean isGladiatorMatch;
    public Timestamp startedOn;

    public OngoingLadderMatch() {

    }

    public static OngoingLadderMatch fromRuleSet(ResultSet rs) throws SQLException {
        OngoingLadderMatch output = new OngoingLadderMatch();

        output.id = rs.getInt("Id");
        output.user1 = rs.getInt("User1");
        output.username1 = rs.getString("Username1");
        output.user2 = rs.getInt("User2");
        output.username2 = rs.getString("Username2");
        output.mapPlayed = rs.getString("MapPlayed");
        output.isGladiatorMatch = rs.getBoolean("IsGladiatorArenaMatch");
        output.startedOn = rs.getTimestamp("StartedOn");

        return output;
    }
}
