package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class GladiatorChallenge {
    public int Id;
    public int ChallengingUserId;
    public int ChallengedUserId;
    public RankPlacement RankPositionOfChallengingUser;
    public int MapVetoed;
    public int MapPriority1;
    public int MapPriority2;
    public int MapToBePlayed;
    public int ChallengedUserHero;
    public int ChallengingUserHero;
    public UUID identificator;
    public Timestamp ChallengdOn;

    public GladiatorChallenge() {

    }

    public static GladiatorChallenge fromResultSet(ResultSet rs) throws SQLException {
        GladiatorChallenge output = new GladiatorChallenge();

        output.Id = rs.getInt("Id");
        output.ChallengingUserId = rs.getInt("ChallengingUserId");
        output.ChallengedUserId = rs.getInt("ChallengedUserId");
        output.RankPositionOfChallengingUser = RankPlacement.fromInt(rs.getInt("RankPositionOfChallengingUser"));
        output.MapVetoed = rs.getInt("MapVetoed");
        output.MapPriority1 = rs.getInt("MapPriority1");
        output.MapPriority2 = rs.getInt("MapPriority2");
        output.MapToBePlayed = rs.getInt("MapToBePlayed");
        output.ChallengedUserHero = rs.getInt("ChallengedUserHero");
        output.ChallengingUserHero = rs.getInt("ChallengingUserHero");
        output.identificator = UUID.fromString(rs.getString("Identificator"));
        output.ChallengdOn = rs.getTimestamp("ChallengedOn");

        return output;
    }
}
