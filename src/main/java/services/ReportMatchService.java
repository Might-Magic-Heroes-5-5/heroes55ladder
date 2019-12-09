package services;

import config.Config;
import models.DifficultySetting;
import net.dv8tion.jda.api.entities.User;
import repo.IRepo;
import repo.Repo;

import java.sql.*;

import javax.servlet.ServletContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReportMatchService {
    public class ReportMatchResponse {
        public boolean success;
        public String errorMsg;
        public String successMsg;

        public ReportMatchResponse(boolean success, String successMsg, String errorMsg) {
            this.success = success;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    public class ELOresponse {
        public double winningPlayerNewELO;
        public double losingPlayerNewELO;

        public ELOresponse(double winningPlayerNewELO, double losingPlayerNewELO) {
            this.winningPlayerNewELO = winningPlayerNewELO;
            this.losingPlayerNewELO = losingPlayerNewELO;
        }
    }

    public class ELOchangeInput {
        public String winningUsername;
        public String losingUsername;

        public ELOchangeInput(String winningUsername, String losingUsername) {
            this.winningUsername = winningUsername;
            this.losingUsername = losingUsername;
        }
    }

    public static class MatchReport {
        public String reportingUsername;
        public int reportingUserId;
        public String oppositeUsername;
        public int oppoeisteUserId;
        public boolean didWin;
        public DifficultySetting difficultyLevel;
        public String mapName;
        public Timestamp dateReported;
        public int ladderMatchId = -1;

        public MatchReport() {

        }

        public MatchReport(String reportingUsername, int reportingUserId, String oppositeUsername, boolean didWin, DifficultySetting difficultyLevel, String mapName) {
            this.reportingUsername = reportingUsername;
            this.reportingUserId = reportingUserId;
            this.oppositeUsername = oppositeUsername;
            this.didWin = didWin;
            this.difficultyLevel = difficultyLevel;
            this.mapName = mapName;
            dateReported = new Timestamp(System.currentTimeMillis());
        }

        public MatchReport(int ladderMatchId, String reportingUsername, int reportingUserId, String oppositeUsername, int oppoeisteUserId, boolean didWin, DifficultySetting difficultyLevel, String mapName) {
            this.ladderMatchId = ladderMatchId;
            this.reportingUsername = reportingUsername;
            this.reportingUserId = reportingUserId;
            this.oppositeUsername = oppositeUsername;
            this.oppoeisteUserId = oppoeisteUserId;
            this.didWin = didWin;
            this.difficultyLevel = difficultyLevel;
            this.mapName = mapName;
            dateReported = new Timestamp(System.currentTimeMillis());
        }

        public static MatchReport fromResutSet(ResultSet rs) throws SQLException {
            MatchReport report = new MatchReport();

            report.ladderMatchId = rs.getInt("LadderMatchId");
            report.reportingUserId = rs.getInt("User1");
            report.reportingUsername = rs.getString("Username1");
            report.oppoeisteUserId = rs.getInt("User2");
            report.oppositeUsername = rs.getString("UserName2");
            report.didWin = rs.getBoolean("User1Won");
            report.difficultyLevel = DifficultySetting.fromInt(rs.getInt("DifficultySetting"));
            report.mapName = rs.getString("MapName");
            report.dateReported = rs.getTimestamp("DateReported");

            return report;
        }
    }

    public static class MatchInfo {
        public int ladderMatchId;
        public String winningUsername;
        public int winnigUserId;
        public String losingUsername;
        public int losingUserId;
        public DifficultySetting difficultyLevel;
        public String mapName;
        public Date dateReported;

        public MatchInfo(String winningUsername, int winnigUserId, String losingUsername, int losingUserId, DifficultySetting difficultyLevel, String mapName, Date dateReported) {
            this.winningUsername = winningUsername;
            this.winnigUserId = winnigUserId;
            this.losingUsername = losingUsername;
            this.losingUserId = losingUserId;
            this.difficultyLevel = difficultyLevel;
            this.mapName = mapName;
            this.dateReported = dateReported;
        }

        public MatchInfo(MatchReport matchReport) {
            ladderMatchId = matchReport.ladderMatchId;
            mapName = matchReport.mapName;
            difficultyLevel = matchReport.difficultyLevel;
            dateReported = matchReport.dateReported;

            if(matchReport.didWin) {
                winningUsername = matchReport.reportingUsername;
                winnigUserId = matchReport.reportingUserId;
                losingUsername = matchReport.oppositeUsername;
                losingUserId = matchReport.oppoeisteUserId;
            }
            else {
                winningUsername = matchReport.oppositeUsername;
                winnigUserId = matchReport.oppoeisteUserId;
                losingUsername = matchReport.reportingUsername;
                losingUserId = matchReport.reportingUserId;
            }
        }

        public static MatchInfo fromResultSet(ResultSet results) throws SQLException {
            String winningUsername = "";
            int winningPlayerId = -1;
            String losingUsername = "";
            int losingUserId = -1;
            String mapName = "";
            DifficultySetting difficulty = DifficultySetting.NORMAL;


            boolean didPlayer1Win = results.getBoolean("User1Won");
            if(didPlayer1Win) {
                winningPlayerId = results.getInt("User1");
                winningUsername = results.getString("Username1");
                losingUserId = results.getInt("User2");
                losingUsername = results.getString("UserName2");
            } else {
                losingUserId = results.getInt("User1");
                losingUsername = results.getString("Username1");
                winningPlayerId = results.getInt("User2");
                winningUsername = results.getString("UserName2");
            }

            mapName = results.getString("MapName");
            difficulty = DifficultySetting.fromInt(results.getInt("DifficultySetting"));
            Date dateReported = results.getDate("DateReported");

            MatchInfo match = new MatchInfo(winningUsername, winningPlayerId, losingUsername, losingUserId, difficulty, mapName, dateReported);
            return match;
        }
    }

    private IRepo repo;
    private ServletContext config;
    String siteUrl;

    public ReportMatchService(ServletContext config) {
        repo = Repo.getRepo(config);
        this.config = config;
        siteUrl = Config.getSiteUrl(config);
    }

    public MatchReport getMatchReport(UUID identificator) {
        MatchReport report = repo.getReport(identificator);
        return report;
    }

    public boolean confirmReport(UUID identificator) {
        MatchReport report = repo.pullReport(identificator);
        if(report == null)
            return false;

        ELOresponse response = saveMatchAndCalcELO(report);
        if(response == null)
            return false;

        if(report.ladderMatchId != -1)
            repo.deleteLadderMatch(report.ladderMatchId);
        return true;
    }

    public boolean declineReport(UUID identificator) {
        MatchReport report = repo.pullReport(identificator);
        if(report == null)
            return false;
        else
            return true;
    }

    public ReportMatchResponse reportMatch(MatchReport report) {
        if(repo.existsUsername(report.reportingUsername) == false)
            return new ReportMatchResponse(false, "", "The reporting user does not exist.");

        if(repo.existsUsername(report.oppositeUsername) == false)
            return new ReportMatchResponse(false, "", "The opponent user does not exist.");

        report.oppoeisteUserId = repo.getUserId(report.oppositeUsername);

        if(report.reportingUserId == report.oppoeisteUserId)
            return new ReportMatchResponse(false, "", "The winning player must differ from the losing player");

        ReportMatchResponse response;

        if(report.didWin)
            response = saveReportAndSendConfirmation(report);
        else {
            ELOresponse elOresponse = saveMatchAndCalcELO(report);
            if(report.ladderMatchId != -1)
                repo.deleteLadderMatch(report.ladderMatchId);
            if(elOresponse == null)
                return  new ReportMatchResponse(false, "", "ELO calculation problem, ELO didn't change");
            response = new ReportMatchResponse(true, "Your new ELO is " + String.format("%.0f", elOresponse.losingPlayerNewELO), "");
        }

        return response;
    }

    private ELOresponse saveMatchAndCalcELO(MatchReport report) {
        MatchInfo info = new MatchInfo(report);
        return saveMatchCalcAndSaveELO(info);
    }

    private ELOresponse saveMatchCalcAndSaveELO(MatchInfo info) {
        if(repo.existsLadderMatch(info.ladderMatchId) == false)
            return null;
        repo.saveMatch(info);
        boolean isGladiatorMatch = repo.gladiatorChallengeOfLadderMatch(info.ladderMatchId);
        if(isGladiatorMatch)
            dealWithGladiatorMatchReport(info.ladderMatchId, info.winnigUserId, info.losingUserId);

        double winningPlayerELO = repo.getPlayerEloById(info.winnigUserId);
        double losingPlayerELO = repo.getPlayerEloById(info.losingUserId);

        if(winningPlayerELO == -1 || losingPlayerELO == -1) {
            return null;
        }

        double probablityOfWinningPlayer = calcProbablityForWinning(winningPlayerELO, losingPlayerELO);
        double probablityOfLosingPlayer = calcProbablityForWinning(losingPlayerELO, winningPlayerELO);

        winningPlayerELO = winningPlayerELO + 50 * (1 - probablityOfWinningPlayer);
        losingPlayerELO = losingPlayerELO + 50 * (0 - probablityOfLosingPlayer);

        repo.updateELO(info.winnigUserId, winningPlayerELO);
        repo.updateELO(info.losingUserId, losingPlayerELO);

        return new ELOresponse(winningPlayerELO, losingPlayerELO);
    }

    private void dealWithGladiatorMatchReport(int ladderMatchId, int winningUserId, int losingUserId) {
        int heroOfWinner = repo.getHeroIdOfWinner(ladderMatchId, winningUserId);

        repo.addHeroToUsedHereoesOfUser(winningUserId, heroOfWinner);
        repo.clearCurrentlyUsedHeroes(ladderMatchId);
        repo.clearUsedHeroesOfUser(losingUserId);
        repo.clearStreakOfUser(losingUserId);

        int currentStreak = repo.increaseStreakAndReturnValue(winningUserId);
        int hallOfFameStreak = repo.getHallOfFameStreakOfUser(winningUserId);

        if(hallOfFameStreak < currentStreak) {
            if(hallOfFameStreak == 0)
                repo.insertHallOfFameRow(winningUserId, currentStreak);
            else
                repo.updateHallOfFameRow(winningUserId, currentStreak);
            repo.putUsedHeroesToHallOfFameHeroes(winningUserId);
        }

    }

    private double calcProbablityForWinning(double playerRating, double oppositePlayerRating) {
        return 1.0f * 1.0f / (1 + 1.0f * (float)(Math.pow(10, 1.0f * (oppositePlayerRating - playerRating) / 400)));
    }

    private ReportMatchService.ReportMatchResponse saveReportAndSendConfirmation(ReportMatchService.MatchReport report) {
        String errorMessage = "";
        String successMsg = "";
        UUID identificator = UUID.randomUUID();
        this.repo.saveReport(report, identificator);
        String discordTagOfLosingPlayer = this.repo.getDiscordTagOfUser(report.oppoeisteUserId);

        boolean success;
        try {
            DiscordService discord = new DiscordService(this.config);
            discord.init();
            List<String> messageLines = new ArrayList();
            messageLines.add("The player " + report.reportingUsername + " reported that he won against you. Please click down here to either confirm or decline his report.");
            messageLines.add(this.siteUrl + "/confirmMatchPage.jsp?iden=" + identificator.toString());
            success = discord.sendMessageToUser(discordTagOfLosingPlayer, messageLines);
            successMsg = "Thank you for reporting the match, please wait until the losing player confirms his loss.";
        } catch (Exception var9) {
            success = false;
            errorMessage = "an error occured, please try reporting the match later";
        }

        return new ReportMatchService.ReportMatchResponse(success, successMsg, errorMessage);
    }
}
