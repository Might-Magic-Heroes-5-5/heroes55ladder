package repo;

import models.*;
import config.*;
import services.*;

import javax.servlet.ServletContext;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Repo implements IRepo {
    private String connStr;
    private String serverIp;
    private String usernmaeDB;
    private String passwordDB;

    private static Queue<Connection> connectionPool = null;
    ServletContext config;

    public Repo(ServletContext config) {
        connStr = Config.getConnectionString(config);
        serverIp = Config.getServerIp(config);
        usernmaeDB = Config.getDbUsername(config);
        passwordDB = Config.getDbPassword(config);
        this.config = config;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(connectionPool == null){
            connectionPool =  new ConcurrentLinkedQueue<Connection>();
        }

        Connection con = connectionPool.poll();
        if(con == null) {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(serverIp, usernmaeDB, passwordDB);
        }
        else {
            if(con.isClosed())
                con = DriverManager.getConnection(serverIp, usernmaeDB, passwordDB);
        }

        return con;
    }

    private void releaseConnection(Connection con) {
        connectionPool.add(con);
    }

    @Override
    public List<UserRank> getTopUserRanks() {
        List<UserRank> TopUsers = null;
        String query = "SELECT Username, Rating from Users US inner join LadderElo LE on US.Id = LE.UserId ORDER BY LE.Rating DESC LIMIT 10";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            TopUsers = new ArrayList<UserRank>();
            while(results.next()) {
                String username = results.getString("Username");
                int rating = (int)Math.round(results.getDouble("Rating"));
                TopUsers.add(new UserRank(username, rating));
            }
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getTopUserRanks failed on exception ", ex);
            TopUsers = new ArrayList<UserRank>();
        }

        return TopUsers;
    }

    @Override
    public boolean checkUsernamePassword(String username, long hashedPassword) {
        boolean result = false;
        String query = "SELECT Username, PasswordHash from Users where Username = '" + username + "' AND PasswordHash = " + hashedPassword + " AND Confirmed = 1";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = true;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("checkUsernamePassword failed on exception ", ex);
            result = false;
        }

        return result;
    }

    @Override
    public boolean addNewUser(String username, long hashedPassword, String discordTag, UUID identificator) {
        boolean success = false;
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String command = "INSERT INTO Users (Username, PasswordHash, DiscordTag, Identificator, Confirmed, DateInserted) " + 
        "VALUES ('" + username + "', " + hashedPassword + ", '" + discordTag + "', '" + identificator + "', 0, '" + formatter.format(currentTime) + "');";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("addNewUser failed on exception ", ex);
            success = false;
        }

        return success;
    }


    @Override
    public boolean existsUsername(String username) {
        boolean result = false;
        String query = "SELECT Username from Users where Username = '" + username + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = true;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("existsUsername failed on exception ", ex);
            result = false;
        }

        return result;
    }

    @Override
    public boolean createChallenge(ChallengeInfo challenge) {
        boolean success = false;
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String command = "INSERT INTO Challenges (UserId, MapName, AllowNormal, AllowHard, AllowHeroic, AllowImpossible, StartingHour, EndingHour, DateInserted) " +
                "VALUES (" + challenge.userId + ", '" + challenge.mapName + "', " + challenge.isNormalDifficulty + ", " + challenge.isHardDifficulty + ", " + challenge.isHeroicDifficulty +
                ", " + challenge.isImpossibleDifficulty + ", " + challenge.startTime + ", " + challenge.endTime + ", '" + formatter.format(currentTime) + "');";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("createChallenge failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public List<ChallengeInfo> getChallenges(int maxNumOfChallenges) {
        List<ChallengeInfo> challenges = null;
        String query = "SELECT CH.Id as Id, UserId, Username, MapName, AllowNormal, AllowHard, AllowHeroic, AllowImpossible, StartingHour, EndingHour from Challenges CH inner join Users US on UserId = US.Id";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            challenges = new ArrayList<ChallengeInfo>();
            while(results.next())
                challenges.add(ChallengeInfo.fromResultSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getChallenges failed on exception ", ex);
            challenges = new ArrayList<ChallengeInfo>();
        }

        return challenges;
    }

    @Override
    public List<ReportMatchService.MatchInfo> getRecentMatches(int maxNumOfMatches) {
        List<ReportMatchService.MatchInfo> matches = null;
        String query = "SELECT User1, Username1, User2, Username as UserName2, User1Won, DifficultySetting, MapName, DateReported FROM (SELECT User1, US.Username as Username1, User2, User1Won," +
                "DifficultySetting, MapName, DateReported from MatchHistory MH inner join Users US on User1 = US.Id ) TMP inner join Users US on User2 = Id ORDER BY DateReported DESC LIMIT " + maxNumOfMatches;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            matches = new ArrayList<ReportMatchService.MatchInfo>();
            while(results.next())
                matches.add(ReportMatchService.MatchInfo.fromResultSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getRecentMatches failed on exception ", ex);
            matches = new ArrayList<ReportMatchService.MatchInfo>();
        }

        return matches;
    }

    @Override
    public boolean existsDiscord(String discord) {
        boolean result = false;
        String query = "SELECT Username from Users where DiscordTag = '" + discord + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = true;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("existsDiscord failed on exception ", ex);
            result = false;
        }

        return result;
    }

    @Override
    public boolean confirmUser(String username, UUID identificator) {
        boolean result = false;
        String command = "UPDATE Users SET Confirmed = 1 WHERE Username = '" + username + "' and Identificator = '" + identificator + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            int affectedRows = statement.executeUpdate(command);
            if(affectedRows > 0)
                result = true;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("confirmUser failed on exception ", ex);
            result = false;
        }

        return result;
    }

    @Override
    public void removeUser(String username) {
        boolean result = false;
        String command = "DELETE FROM Users WHERE Username = '" + username + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            result = true;
        } catch (Exception ex) {
            config.log("removeUser failed on exception ", ex);
            result = false;
        }
    }

    @Override
    public String getDiscordTaglOfUsername(String username) {
        String result = null;
        String query = "SELECT DiscordTag from Users where Username = '" + username + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = rs.getString("DiscordTag");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getDiscordTaglOfUsername failed on exception ", ex);
            result = null;
        }

        return result;
    }

    @Override
    public String getDiscordTagOfUser(int userId) {
        String result = null;
        String query = "SELECT DiscordTag from Users where Id = " + userId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = rs.getString("DiscordTag");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getDiscordTagOfUser failed on exception ", ex);
            result = null;
        }

        return result;
    }

    @Override
    public void saveReport(ReportMatchService.MatchReport report, UUID identificator) {
        boolean success = false;
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        int User1Won = report.didWin ? 1 : 0;
        String command = "INSERT INTO MatchReports (LadderMatchId, User1, User2, User1Won, DifficultySetting, MapName, Identificator, DateReported) " +
                "VALUES (" + report.ladderMatchId + ", " + report.reportingUserId + ", " + report.oppoeisteUserId + ", " + User1Won + ", " + report.difficultyLevel.toInt() + ", '" +
                report.mapName + "', '" + identificator.toString() + "', '" + formatter.format(currentTime) + "')";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("saveReport failed on exception ", ex);
            success = false;
        }
    }

    @Override
    public ReportMatchService.MatchReport getReport(UUID identificator) {
        ReportMatchService.MatchReport report = null;
        String query = "SELECT LadderMatchId, User1, Username1, User2, Username as UserName2, User1Won, DifficultySetting, MapName, DateReported FROM (SELECT User1, US.Username as Username1, " +
                "LadderMatchId, User2, User1Won, DifficultySetting, MapName, MR.Identificator as Identificator, DateReported from MatchReports MR inner join Users US on User1 = US.Id where " +
                "MR.Identificator = '" + identificator.toString() + "') TMP inner join Users US on User2 = Id";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                report = ReportMatchService.MatchReport.fromResutSet(rs);

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("services.ReportMatchService failed on exception ", ex);
            report = null;
        }

        return report;
    }

    @Override
    public void saveMatch(ReportMatchService.MatchInfo info) {
        boolean success = false;
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String command = "INSERT INTO MatchHistory (User1, User2, User1Won, DifficultySetting, MapName, DateReported) " +
                "VALUES (" + info.winnigUserId + ", " + info.losingUserId + ", 1, " + info.difficultyLevel.toInt() + ", '" + info.mapName + "', '" + formatter.format(info.dateReported) + "')";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("saveMatch failed on exception ", ex);
            success = false;
        }
    }

    @Override
    public double getPlayerEloById(int UserId) {
        double result = -1;
        String query = "SELECT Rating from LadderElo where UserId = " + UserId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = rs.getDouble("Rating");

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getPlayerEloById failed on exception ", ex);
            result = -1;
        }

        return result;
    }

    @Override
    public void updateELO(int userid, double newELO) {
        String command = "UPDATE LadderElo SET Rating = " + newELO + " WHERE UserId = " + userid;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            int affectedRows = statement.executeUpdate(command);
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("updateELO failed on exception ", ex);
        }
    }

    @Override
    public ReportMatchService.MatchReport pullReport(UUID identificator) {
        ReportMatchService.MatchReport report = null;
        String query = "SELECT TMP.Id as Id, User1, Username1, User2, Username as UserName2, User1Won, DifficultySetting, MapName, DateReported, LadderMatchId FROM (SELECT MR.Id as Id, User1, " +
                "US.Username as Username1, User2, User1Won, DifficultySetting, MapName, MR.Identificator as Identificator, DateReported, LadderMatchId from MatchReports MR inner join Users US on User1 = US.Id " +
                "where MR.Identificator = '" + identificator + "') TMP inner join Users US on User2 = US.Id";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
                report = ReportMatchService.MatchReport.fromResutSet(rs);
                int rowId = rs.getInt("Id");
                statement.execute("DELETE FROM MatchReports where Id = " + rowId);
            }

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("pullReport failed on exception ", ex);
            report = null;
        }

        return report;
    }

    @Override
    public ChallengeInfo getChallengeInfo(int challengeId) {
        ChallengeInfo challenge = null;
        String query = "SELECT CH.Id as Id, UserId, Username, MapName, AllowNormal, AllowHard, AllowHeroic, AllowImpossible, StartingHour, EndingHour from Challenges CH inner join Users US on UserId = US.Id " +
                "WHERE CH.Id = " + challengeId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                challenge = ChallengeInfo.fromResultSet(results);

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("models.ChallengeInfo failed on exception ", ex);
            challenge = null;
        }

        return challenge;
    }

    @Override
    public int getUserId(String username) {
        int result = -1;
        String query = "SELECT Id from Users where Username = '" + username + "'";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                result = rs.getInt("Id");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getUserId failed on exception ", ex);
            result = -1;
        }

        return result;
    }

    @Override
    public void setInitialEloForUser(int userId) {
        boolean success;
        String command = "INSERT INTO LadderElo (UserId, Rating) VALUES (" + userId + ", " + Double.toString(1500) + ")";


        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("setInitialEloForUser failed on exception ", ex);
            success = false;
        }
    }

    @Override
    public void removeChallenge(int id) {
        String command = "DELETE FROM Challenges WHERE Id = " + id;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("removeChallenge failed on exception ", ex);
        }
    }

    @Override
    public int getHeroId(String heroName) {
        if(heroName == null)
            return 0;

        String command = "SELECT Id FROM Heroes WHERE HeroName = ?";
        int heroId = 0;

        try {
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement(command);
            statement.setString(1, heroName);
            ResultSet rs = statement.executeQuery();
            if(rs.next())
                heroId = rs.getInt("Id");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("saveFeedback failed on exception ", ex);
            heroId = 0;
        }

        return heroId;
    }

    @Override
    public void saveFeedback(String message) {
        if(message == null)
            return;

        String command = "INSERT INTO Feedback (Message) Values (?)";

        try {
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement(command);
            statement.setString(1, message);
            statement.execute();
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("saveFeedback failed on exception ", ex);
        }
    }

    @Override
    public boolean saveLadderPriorities(LadderPriorities ladderPriorities) {
        boolean success;
        String query = "SELECT Id FROM LadderSearchingMatches WHERE UserId = " + ladderPriorities.userId;
        int id;
        PreparedStatement statement;

        try {
            Connection con = getConnection();
            Statement queryStatement = con.createStatement();
            ResultSet rs = queryStatement.executeQuery(query);
            if(rs.next()) {
                id = rs.getInt("Id");
                statement = prepareUpdateLadderStatement(ladderPriorities, id, con);
            } else
                statement = prepareInsertLadderStatement(ladderPriorities, con);

            statement.executeUpdate();
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("saveLadderPriorities failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public LadderPriorities getLadderPriorities(int userId) {
        LadderPriorities priorities;
        String query = "SELECT Id, UserId, VetoedMaps, DaysCanPlay, StartHourOfDay, EndHourOfDay FROM LadderSearchingMatches WHERE UserId = " + userId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                priorities = LadderPriorities.fromResultSet(results);
            else
                priorities = null;

            releaseConnection(con);
        }catch (Exception ex) {
            config.log("getLadderPriorities failed on exception ", ex);
            priorities = null;
        }

        return priorities;
    }

    @Override
    public boolean deleteLadderPriorities(int userId) {
        boolean success = false;
        String command = "DELETE FROM LadderSearchingMatches WHERE UserId = " + userId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("deleteLadderPriorities failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public List<GladiatorStreak> getCurrentGladiatorStreaks(int maxRows) {
        ArrayList<GladiatorStreak> streaks = new ArrayList<GladiatorStreak>();
        String query = "SELECT GL.Id as Id, UserId, Username, CurrentStreak FROM Gladiators GL INNER JOIN Users U on UserId = U.Id ORDER BY CurrentStreak DESC LIMIT " + maxRows;;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                streaks.add(GladiatorStreak.fromResultSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getCurrentGladiatorStreaks failed on exception ", ex);
        }

        return streaks;
    }

    @Override
    public List<GladiatorStreak> getHallOfFame(int maxRows) {
        ArrayList<GladiatorStreak> streaks = new ArrayList<GladiatorStreak>();
        String query = "SELECT GL.Id as Id, UserId, Username, Streak FROM GladiatorHallOfFame GL INNER JOIN Users U on UserId = U.Id ORDER BY Streak DESC LIMIT " + maxRows;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                streaks.add(GladiatorStreak.fromHallOfFameResultSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getHallOfFame failed on exception ", ex);
        }

        return streaks;
    }

    @Override
    public List<Integer> getUsedHeroOfGladiator(int userId) {
        ArrayList<Integer> usedHeroes = new ArrayList<Integer>();
        String query = "SELECT HeroId FROM GladiatorHeroesUsed WHERE UserId = " + userId + " UNION SELECT HeroId FROM GladiatorsCurrentlyHeroesUsed WHERE UserId = " + userId +
                " UNION WITH SELECT ChallengingUserHero as HeroId FROM GladiatorChallenges WHERE ChallengingUserId = " + userId + " UNION WITH " +
                " SELECT ChallengedUserHero as HeroId FROM GladiatorChallenges WHERE ChallengedUserId = " + userId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                usedHeroes.add(results.getInt("HeroId"));
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getUsedHeroOfGladiator failed on exception ", ex);
        }

        return usedHeroes;
    }

    @Override
    public String getHeroName(int heroId) {
        String name = null;
        String query = "SELECT HeroName FROM Heroes WHERE Id = " + heroId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                name = results.getString("HeroName");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getHeroName failed on exception ", ex);
            name = null;
        }

        return name;
    }

    @Override
    public List<Integer> getUnavailibleHeroesForChallenge(int challengeId) {
        ArrayList<Integer> unavalibleHeroes = new ArrayList<Integer>();
        String query = "SELECT HeroId FROM UnavailibleHeroesForChallenge WHERE ChallengeId = " + challengeId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                unavalibleHeroes.add(results.getInt("HeroId"));
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getUnavailibleHeroesForChallenge failed on exception ", ex);
        }

        return unavalibleHeroes;
    }

    @Override
    public HashMap<Integer, List<String>> getUsedHeroesOfGladiators() {
        HashMap<Integer, List<String>> userToUsedHero = new HashMap<>();
        String query = "SELECT UserId, HeroName FROM GladiatorHeroesUsed GHU INNER JOIN Heroes H ON GHU.HeroId = H.Id UNION SELECT UserId, HeroName " +
                "FROM GladiatorsCurrentlyHeroesUsed GCHU INNER JOIN Heroes H ON GCHU.HeroId = H.Id";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                int userId = results.getInt("UserId");
                String heroName = results.getString("HeroName");
                List<String> usedHeroes;

                if(userToUsedHero.containsKey(userId) == false) {
                    usedHeroes = new ArrayList<String>();
                    userToUsedHero.put(userId, usedHeroes);
                }
                else
                    usedHeroes = userToUsedHero.get(userId);
                usedHeroes.add(heroName);
            }

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getUsedHeroesOfGladiators failed on exception ", ex);
        }

        return userToUsedHero;
    }

    @Override
    public HashMap<Integer, List<String>> getHallOfFameUseddHeroes() {
        HashMap<Integer, List<String>> userToUsedHero = new HashMap<>();
        String query = "SELECT UserId, HeroName FROM (SELECT GHOF.UserId as UserId, HeroId FROM GladiatorHallOfFame GHOF INNER JOIN HallOfFameUsedHeroes HOFUH ON GHOF.UserId = HOFUH.UserId) TMP "
                + "INNER JOIN Heroes H ON TMP.HeroId = H.Id";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                int userId = results.getInt("UserId");
                String heroName = results.getString("HeroName");
                List<String> usedHeroes;

                if(userToUsedHero.containsKey(userId) == false) {
                    usedHeroes = new ArrayList<String>();
                    userToUsedHero.put(userId, usedHeroes);
                }
                else
                    usedHeroes = userToUsedHero.get(userId);
                usedHeroes.add(heroName);
            }

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getHallOfFameUseddHeroes failed on exception ", ex);
        }

        return userToUsedHero;
    }

    @Override
    public boolean gladiatorChallengeOfLadderMatch(int ladderMatchId) {
        boolean isGladiatorMatch = false;
        String query = "SELECT IsGladiatorArenaMatch FROM LadderOngoingMatches WHERE Id = " + ladderMatchId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                isGladiatorMatch = results.getBoolean("IsGladiatorArenaMatch");

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("gladiatorChallengeOfLadderMatch failed on exception ", ex);
            isGladiatorMatch = false;
        }

        return isGladiatorMatch;
    }

    @Override
    public int getHeroIdOfWinner(int ladderMatchId, int UserId) {
        int heroId = 0;
        String query = "SELECT HeroId FROM GladiatorsCurrentlyHeroesUsed WHERE LadderMatchId = " + ladderMatchId + " AND UserId = " + UserId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                heroId = results.getInt("HeroId");

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getHeroIdOfWinner failed on exception ", ex);
            heroId = 0;
        }

        return heroId;
    }

    @Override
    public boolean addHeroToUsedHereoesOfUser(int userId, int heroId) {
        String command = "INSERT INTO GladiatorHeroesUsed (UserId, HeroId) VALUES (" + userId + ", " + heroId + ")";
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("addHeroToUsedHereoesOfUser failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean clearCurrentlyUsedHeroes(int ladderMatchId) {
        String command = "DELETE FROM GladiatorsCurrentlyHeroesUsed WHERE LadderMatchId = " + ladderMatchId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("clearCurrentlyUsedHeroes failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean clearUsedHeroesOfUser(int userId) {
        String command = "DELETE FROM GladiatorHeroesUsed WHERE UserId = " + userId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("clearUsedHeroesOfUser failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public int increaseStreakAndReturnValue(int userId) {
        String command = "UPDATE Gladiators SET CurrentStreak = CurrentStreak + 1 WHERE UserId = " + userId;
        String query = "SELECT CurrentStreak FROM Gladiators WHERE UserId = " + userId;
        int streak = 0;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                streak = rs.getInt("CurrentStreak");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("increaseStreakAndReturnValue failed on exception ", ex);
            streak = -1;
        }

        return streak;
    }

    @Override
    public int getHallOfFameStreakOfUser(int userId) {
        String query = "SELECT Streak FROM GladiatorHallOfFame WHERE UserId = " + userId;
        int streak = 0;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                streak = rs.getInt("Streak");
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getHallOfFameStreakOfUser failed on exception ", ex);
            streak = -1;
        }

        return streak;
    }

    @Override
    public boolean checkIfGladiator(int userId) {
        String query = "SELECT Id FROM Gladiators WHERE UserId = " + userId;
        boolean isGladiator = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                isGladiator = true;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("checkIfGladiator failed on exception ", ex);
            isGladiator = false;
        }

        return isGladiator;
    }

    @Override
    public boolean addNewGladiator(int userId) {
        String command = "INSERT INTO Gladiators (UserId, CurrentStreak) VALUES (" + userId + ", 0)";
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("addNewGladiator failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean clearStreakOfUser(int userId) {
        String command = "UPDATE Gladiators SET CurrentStreak = 0 WHERE UserId = " + userId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("clearStreakOfUser failed on exception ", ex);
            success = false;
        }

        return success;
    }


    @Override
    public boolean insertHallOfFameRow(int userId, int streak) {
        String command = "INSERT INTO GladiatorHallOfFame (UserId, Streak) VALUES (" + userId + ", "  + streak + ")";
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("insertHallOfFameRow failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean updateHallOfFameRow(int userId, int streak) {
        String command = "UPDATE GladiatorHallOfFame SET Streak = " + streak + " WHERE UserId = " + userId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("updateHallOfFameRow failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean putUsedHeroesToHallOfFameHeroes(int userId) {
        String command1 = "DELETE FROM HallOfFameUsedHeroes WHERE UserId = " + userId;
        String command2 = "INSERT INTO HallOfFameUsedHeroes (UserId, HeroId) SELECT UserId, HeroId FROM GladiatorHeroesUsed WHERE UserId = " + userId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command1);
            statement.execute(command2);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("putUsedHeroesToHallOfFameHeroes failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public String getUsername(int userId) {
        String username = null;
        String query = "SELECT Username FROM Users WHERE Id = " + userId;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                username = results.getString("Username");

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getUsername failed on exception ", ex);
            username = null;
        }

        return username;
    }

    @Override
    public boolean saveGladiatorChallenge(int userId, int challengedUserId, RankPlacement rankPosition, int priority1, int priority2, int mapVeto, UUID identificator) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String command = "INSERT INTO GladiatorChallenges (ChallengingUserId, ChallengedUserId, RankPositionOfChallengingUser, MapVetoed, MapPriority1, MapPriority2, Identificator, ChallengedOn) " +
                "VALUES (" + userId + ", " + challengedUserId + ", " + rankPosition.toInt() + ", " + mapVeto + ", " + priority1 + ", " + priority2 + ", '" + identificator + "', '" + formatter.format(currentTime) + "')";
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("saveGladiatorChallenge failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public int getMapId(String mapName) {
        String query = "SELECT Id FROM LadderMapPool WHERE MapName = ?";
        int mapId = -1;

        try {
            Connection con = getConnection();
            PreparedStatement selectQuery = con.prepareStatement(query);
            selectQuery.setString(1, mapName);
            ResultSet rs = selectQuery.executeQuery();
            if(rs.next())
                mapId = rs.getInt("Id");
        } catch (Exception ex) {
            config.log("getMapId failed on exception ", ex);
            mapId = -1;
        }

        return mapId;
    }

    @Override
    public GladiatorChallenge getGladiatorChallenge(UUID identificator) {
        String query = "SELECT Id, ChallengingUserId, ChallengedUserId, RankPositionOfChallengingUser, MapVetoed, MapPriority1, MapPriority2, MapToBePlayed, ChallengedUserHero, " +
                "ChallengingUserHero, Identificator, ChallengedOn FROM GladiatorChallenges WHERE Identificator = ?";
        GladiatorChallenge challenge = null;

        try {
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, identificator.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next())
                challenge = GladiatorChallenge.fromResultSet(rs);
        } catch (Exception ex) {
            config.log("getGladiatorChallenge failed on exception ", ex);
            challenge = null;
        }

        return challenge;
    }

    @Override
    public void putMapToPlayInChallenge(int id, int mapToBePlayed) {
        String command = "Update GladiatorChallenges SET MapToBePlayed = " + mapToBePlayed + " WHERE Id = " + id;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("putMapToPlayInChallenge failed on exception ", ex);
        }
    }

    @Override
    public boolean updateChallengingUserHero(int challengeId, int heroId) {
        String command = "Update GladiatorChallenges SET ChallengingUserHero = " + heroId + " WHERE Id = " + challengeId;
        boolean success = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("updateChallengingUserHero failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean updateChallengedUserHero(int challengeId, int heroId) {
        String command = "Update GladiatorChallenges SET ChallengedUserHero = " + heroId + " WHERE Id = " + challengeId;
        boolean success = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("updateChallengedUserHero failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean resetHeroesForChallenge(int challengeId) {
        String command = "Update GladiatorChallenges SET ChallengedUserHero = NULL, ChallengingUserHero = NUUL WHERE Id = " + challengeId;
        boolean success = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("resetHeroesForChallenge failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean addHeroAsUnavailibleForChallenge(int challengeId, int heroId) {
        String command = "INSERT INTO UnavailibleHeroesForChallenge (ChallengeId, HeroId) VALUES (" + challengeId + "," + heroId +")";
        boolean success = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("addHeroAsUnavailibleForChallenge failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public GladiatorChallenge getGladiatorChallenge(int challengeId) {
        String query = "SELECT Id, ChallengingUserId, ChallengedUserId, RankPositionOfChallengingUser, MapVetoed, MapPriority1, MapPriority2, MapToBePlayed, ChallengedUserHero, " +
                "ChallengingUserHero, Identificator, ChallengedOn FROM GladiatorChallenges WHERE Id = " + challengeId ;
        GladiatorChallenge challenge = null;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                challenge = GladiatorChallenge.fromResultSet(rs);
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getGladiatorChallenge failed on exception ", ex);
            challenge = null;
        }

        return challenge;
    }

    @Override
    public boolean deleteGladiatorChallenge(int userId, UUID identificator) {
        String command = "DELETE FROM GladiatorChallenges WHERE ChallengedUserId = ? AND Identificator = ? AND MapToBePlayed is NULL";
        boolean success;

        try {
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement(command);
            statement.setInt(1, userId);
            statement.setString(2, identificator.toString());
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("deleteLadderMatch failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public List<LadderPriorities> getAllLadderPriorities() {
        ArrayList<LadderPriorities> allPriorities = new ArrayList<LadderPriorities>();
        String query = "SELECT Id, UserId, VetoedMaps, DaysCanPlay, StartHourOfDay, EndHourOfDay FROM LadderSearchingMatches WHERE Id > 0";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                allPriorities.add(LadderPriorities.fromResultSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getRecentMatches failed on exception ", ex);
        }

        return allPriorities;
    }

    @Override
    public List<String> getMapPool() {
        String query = "SELECT MapName FROM LadderMapPool where Id > 0";
        ArrayList<String> mapNames = new ArrayList<String>();

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                mapNames.add(results.getString("MapName"));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getMapPool failed on exception ", ex);
        }

        return mapNames;
    }

    @Override
    public HashMap<Integer, String> getMapPoolHashMap() {
        String query = "SELECT Id, MapName FROM LadderMapPool where Id > 0";
        HashMap<Integer, String> mapPool = new HashMap<Integer, String>();

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                mapPool.put(results.getInt("Id"), results.getString("MapName"));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getMapPoolHashMap failed on exception ", ex);
        }

        return mapPool;
    }

    @Override
    public List<OngoingLadderMatch> getOngoingLadderMatchesOfUser(int userId) {
        String query = "SELECT TMP.Id as Id, User1, Username1, User2, U.Username as Username2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM (SELECT LM.Id as Id, User1, " +
                "U.Username as Username1, User2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM LadderOngoingMatches LM INNER JOIN Users U on User1 = U.Id WHERE User1 = " + userId +
                " OR User2 = " + userId + ") TMP INNER JOIN Users U on User2 = U.Id";
        ArrayList<OngoingLadderMatch> matches = new ArrayList<OngoingLadderMatch>();

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                matches.add(OngoingLadderMatch.fromRuleSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getOngoingLadderMatchesOfUser failed on exception ", ex);
        }

        return matches;
    }

    @Override
    public List<OngoingLadderMatch> getOngoingLadderMatches(int maxRows) {
        String query = "SELECT TMP.Id as Id, User1, Username1, User2, U.Username as Username2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM (SELECT LM.Id as Id, User1, " +
                "U.Username as Username1, User2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM LadderOngoingMatches LM INNER JOIN Users U on User1 = U.Id) TMP INNER JOIN Users U on User2 = U.Id ORDER BY StartedOn LIMIT " + maxRows;
        ArrayList<OngoingLadderMatch> matches = new ArrayList<OngoingLadderMatch>();

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                matches.add(OngoingLadderMatch.fromRuleSet(results));

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getOngoingLadderMatchesOfUser failed on exception ", ex);
        }

        return matches;
    }

    @Override
    public OngoingLadderMatch getOngoingMatchById(int id) {
        String query = "SELECT TMP.Id as Id, User1, Username1, User2, U.Username as Username2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM " +
                "(SELECT LM.Id as Id, User1, U.Username as Username1, User2, MapPlayed, IsGladiatorArenaMatch, StartedOn FROM LadderOngoingMatches LM INNER JOIN Users U " +
                "ON User1 = U.Id WHERE LM.Id = " + id + ") TMP INNER JOIN Users U on User2 = U.Id";

        OngoingLadderMatch match = null;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                match = OngoingLadderMatch.fromRuleSet(results);

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("getOngoingMatchById failed on exception ", ex);
        }

        return match;
    }

    @Override
    public boolean existsLadderMatch(int ladderMatchId) {
        String query = "SELECT Id, User1 FROM LadderOngoingMatches WHERE Id = " + ladderMatchId;
        boolean matchFound = false;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);
            if(results.next())
                matchFound = true;

            releaseConnection(con);
        } catch (Exception ex) {
            config.log("existsLadderMatch failed on exception ", ex);
        }

        return matchFound;
    }

    @Override
    public boolean deleteLadderMatch(int ladderMatchId) {
        String command = "DELETE FROM LadderOngoingMatches WHERE Id = " + ladderMatchId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("deleteLadderMatch failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean saveLadderMatch(LadderMatchInfo matchInfo) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String command = "INSERT INTO LadderOngoingMatches (User1, User2, MapPlayed, IsGladiatorArenaMatch, StartedOn) VALUES (" + matchInfo.user1Id + ", " + matchInfo.user2Id + ", '" +
                matchInfo.mapPlayed + "', 0, '" + formatter.format(currentTime) + "')";
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("saveLadderMatch failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean saveLadderGladiatorMatch(GladiatorChallenge challenge) {
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String query = "SELECT MapName FROM LadderMapPool WHERE Id = " + challenge.MapToBePlayed;
        String query2 = "SELECT LAST_INSERT_ID();";
        String command2;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String MapName = "";
            if(rs.next())
                MapName = rs.getString("MapName");

            String command1 = "INSERT INTO LadderOngoingMatches (User1, User2, MapPlayed, IsGladiatorArenaMatch, StartedOn) VALUES (" + challenge.ChallengingUserId + ", " + challenge.ChallengedUserId +
                    ", '" + MapName + "', 1, '" + formatter.format(currentTime) + "')";
            statement.execute(command1);
            rs = statement.executeQuery(query2);
            if(rs.next()) {
                int ladderId = rs.getInt(1);
                command2 = "INSERT INTO GladiatorsCurrentlyHeroesUsed (UserId, LadderMatchId, HeroId) VALUES (" + challenge.ChallengingUserId + ", " + ladderId + ", " +
                        challenge.ChallengingUserHero + "), ( " + challenge.ChallengedUserId + ", " + ladderId + ", " + challenge.ChallengedUserHero + ")";
                statement.execute(command2);
                command2 = "DELETE FROM GladiatorChallenges WHERE Id = " + challenge.Id;
                statement.execute(command2);
                success = true;
            } else
                success = false;
            releaseConnection(con);
        } catch (Exception ex) {
            config.log("saveLadderGladiatorMatch failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public String getMapName(int mapToBePlayed) {
        String query = "SELECT MapName FROM LadderMapPool WHERE ID = " + mapToBePlayed;
        String map = null;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next())
                map = rs.getString("MapName");
            releaseConnection(con);
        } catch(Exception ex) {
            config.log("getMapName failed on exception ", ex);
            map = null;
        }

        return map;
    }

    @Override
    public boolean clearUnavailibleHeroesForChallenge(int id) {
        String command = "DELETE FROM UnavailibleHeroesForChallenge WHERE ChallengeId = " + id;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("clearUnavailibleHeroesForChallenge failed on exception ", ex);
            success = false;
        }

        return success;
    }

    @Override
    public boolean clearPriorities(int userId) {
        String command = "DELETE FROM LadderSearchingMatches WHERE UserId = " + userId;
        boolean success;

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.execute(command);
            releaseConnection(con);
            success = true;
        } catch (Exception ex) {
            config.log("clearPriorities failed on exception ", ex);
            success = false;
        }

        return success;
    }

    private PreparedStatement prepareUpdateLadderStatement(LadderPriorities ladderPriorities, int id, Connection con) throws SQLException {
        String updateCommand = "UPDATE LadderSearchingMatches SET VetoedMaps = ?, DaysCanPlay = ?, StartHourOfDay = ?, " +
                "EndHourOfDay = ?, CreatedDate = ? WHERE Id = ?";

        PreparedStatement updateStatement = con.prepareStatement(updateCommand);
        updateStatement.setString(1, ladderPriorities.vetoedMaps);
        updateStatement.setString(2, boolArrayToStr(ladderPriorities.daysCanPlay));
        updateStatement.setString(3, intArrayToStr(ladderPriorities.startHoursOfDay));
        updateStatement.setString(4, intArrayToStr(ladderPriorities.endHoursOfDay));
        updateStatement.setDate(5, new Date(System.currentTimeMillis()));
        updateStatement.setInt(6, id);

        return updateStatement;
    }

    private PreparedStatement prepareInsertLadderStatement(LadderPriorities ladderPriorities, Connection con) throws SQLException {
        String insertCommand = "INSERT INTO LadderSearchingMatches (UserId, VetoedMaps, DaysCanPlay, StartHourOfDay, EndHourOfDay,CreatedDate) " +
                "VALUES (?,?,?,?,?,?)";

        PreparedStatement insertStatement = con.prepareStatement(insertCommand);
        insertStatement.setInt(1, ladderPriorities.userId);
        insertStatement.setString(2, ladderPriorities.vetoedMaps);
        insertStatement.setString(3, boolArrayToStr(ladderPriorities.daysCanPlay));
        insertStatement.setString(4, intArrayToStr(ladderPriorities.startHoursOfDay));
        insertStatement.setString(5, intArrayToStr(ladderPriorities.endHoursOfDay));
        insertStatement.setDate(6, new Date(System.currentTimeMillis()));

        return insertStatement;
    }

    private String boolArrayToStr(boolean[] boolArray) {
        StringBuilder SB = new StringBuilder();

        for(int i = 0; i < boolArray.length; i++) {
            if(i != 0)
                SB.append(",");

            int intValue = boolArray[i] ? 1 : 0;
            SB.append(intValue);
        }

        return SB.toString();
    }

    private String intArrayToStr(int[] intArray) {
        StringBuilder SB = new StringBuilder();

        for(int i = 0; i < intArray.length; i++) {
            if(i != 0)
                SB.append(",");

            SB.append(intArray[i]);
        }

        return SB.toString();
    }

    public static IRepo getRepo(ServletContext config) {
        if(Config.isLocal(config))
            return new LocalRepo();

        return new Repo(config);
    }
}
