package models;

import java.sql.*;

public class GladiatorStreak {
    public int id;
    public int userId;
    public String username;
    public int streak;
    public int rank;

    public GladiatorStreak()
    {

    }

    public static GladiatorStreak fromResultSet(ResultSet rs) throws SQLException {
        GladiatorStreak streak = new GladiatorStreak();

        streak.id = rs.getInt("Id");
        streak.userId = rs.getInt("UserId");
        streak.username = rs.getString("Username");
        streak.streak = rs.getInt("CurrentStreak");

        return streak;
    }

    public static GladiatorStreak fromHallOfFameResultSet(ResultSet rs) throws SQLException {
        GladiatorStreak streak = new GladiatorStreak();

        streak.id = rs.getInt("Id");
        streak.userId = rs.getInt("UserId");
        streak.username = rs.getString("Username");
        streak.streak = rs.getInt("Streak");

        return streak;
    }
}
