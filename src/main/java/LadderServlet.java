import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import models.DifficultySetting;
import models.LadderPriorities;
import models.OngoingLadderMatch;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import repo.IRepo;
import repo.Repo;
import services.DiscordService;
import services.LoggingService;
import services.ReportMatchService;

import models.*;

public class LadderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoggingService logService = new LoggingService(req, getServletContext());
        LoggingService.LoginResponse logResponse = logService.checkLogin();
        IRepo repo = Repo.getRepo(getServletContext());
        JSONObject response = new JSONObject();

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String json = "";
        if(br != null)
            json = br.readLine();
        JSONParser parser = new JSONParser();
        JSONObject input = null;
        try {
            input = (JSONObject)parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String command = (String)input.get("command");

        switch (command) {
            case "submitLadderPriorities":
                response = submitLadderPriorities(input, logResponse, repo);
                break;
            case "getUserOngoingMatches":
                response = getOngoingMatches(input, logResponse, repo);
                break;
            case "reportMatch":
                response = reportMatch(input, logResponse, repo);
                break;
            case "getLadderPriorities":
                response = getLadderPriorities(input, logResponse, repo);
                break;
            case "clearLadderPriorities":
                response = clearLadderPriorities(logResponse, repo);
                break;
        }

        resp.getWriter().write(response.toJSONString());
    }

    private JSONObject clearLadderPriorities(LoggingService.LoginResponse logResponse, IRepo repo) {
        boolean success;
        String errorMessage="";

        JSONObject output = new JSONObject();

        if(logResponse.isLoggedIn) {
            try {
                int userId = logResponse.userId;
                success = repo.deleteLadderPriorities(userId);
                if(success == false)
                    errorMessage = "You weren't searching for an opponent";
            } catch (Exception ex) {
                success = false;
                errorMessage = ex.getMessage();
            }
        } else {
            success = false;
            errorMessage = "You are not logged in!";
        }


        output.put("success", success);
        output.put("errorMsg", errorMessage);

        return  output;
    }

    private JSONObject getLadderPriorities(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        boolean success, foundPriorities;
        String errorMessage="";
        JSONObject output = new JSONObject();

        if(logResponse.isLoggedIn) {
            try {
                int userId = logResponse.userId;
                LadderPriorities priorities = repo.getLadderPriorities(userId);
                if(priorities != null) {
                    foundPriorities = true;
                    output.put("priorities", priorities.toJson());
                }
                else
                    foundPriorities = false;


                success = true;
            } catch (Exception ex) {
                success = false;
                foundPriorities = false;
                errorMessage = ex.getMessage();
            }
        } else {
            success = false;
            foundPriorities = false;
            errorMessage = "You are not logged in!";
        }


        output.put("success", success);
        output.put("foundPriorities", foundPriorities);
        output.put("errorMsg", errorMessage);

        return  output;
    }

    private JSONObject reportMatch(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        boolean success;
        String successMessage="", errorMessage;
        JSONObject output = new JSONObject();
        int opponentUserId;
        String opponentUsername;

        ReportMatchService reportService = new ReportMatchService(getServletContext());
        if(logResponse.isLoggedIn) {
            int ladderMatchId = (int)((long)input.get("matchId"));
            OngoingLadderMatch match = repo.getOngoingMatchById(ladderMatchId);
            if(match.user1 == logResponse.userId) {
                opponentUserId = match.user2;
                opponentUsername = match.username2;
            } else {
                if(match.user2 != logResponse.userId) {
                    output.put("success", false);
                    output.put("errorMsg", "This match isn't played by you!");
                    output.put("successMsg", successMessage);
                    return output;
                }
                opponentUserId = match.user1;
                opponentUsername = match.username1;
            }

            String resultStr = (String)input.get("result");
            boolean didWin = false;
            if(resultStr.compareToIgnoreCase("won") == 0)
                didWin = true;
            DifficultySetting difficutlyLevel = DifficultySetting.IMPOSSIBLE;
            ReportMatchService.MatchReport matchInfo = new ReportMatchService.MatchReport(ladderMatchId, logResponse.username, logResponse.userId, opponentUsername, opponentUserId, didWin, difficutlyLevel, match.mapPlayed);
            ReportMatchService.ReportMatchResponse reportResponse = reportService.reportMatch(matchInfo);
            success = reportResponse.success;
            successMessage = reportResponse.successMsg;
            errorMessage = reportResponse.errorMsg;
        }
        else {
            success = false;
            errorMessage = "You are not logged in!";
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);
        output.put("successMsg", successMessage);
        return output;
    }

    private JSONObject getOngoingMatches(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        JSONArray matchesArray = new JSONArray();
        int opponentUserId;
        String opponentUsername;

        boolean success = false;
        String errorMessage = null;

        try {
            List<OngoingLadderMatch> matches = repo.getOngoingLadderMatchesOfUser(logResponse.userId);

            for(OngoingLadderMatch match : matches) {
                JSONObject matchJson = new JSONObject();
                matchJson.put("id", match.id);
                if(match.user1 == logResponse.userId) {
                    opponentUserId = match.user2;
                    opponentUsername = match.username2;
                } else {
                    opponentUserId = match.user1;
                    opponentUsername = match.username1;
                }

                matchJson.put("opponentId", opponentUserId);
                matchJson.put("opponentUsername", opponentUsername);
                matchJson.put("mapPlayed", match.mapPlayed);
                matchJson.put("startedOn", match.startedOn.getTime());
                matchesArray.add(matchJson);
            }

            output.put("matches", matchesArray);
            success = true;
        } catch (Exception ex) {
            success = false;
            errorMessage = ex.getMessage();
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);

        return output;
    }

    private JSONObject submitLadderPriorities(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();

        boolean success = false;
        boolean opponentFound = false;
        String errorMessage = null;

        try {
            if(logResponse.isLoggedIn == false) {
                errorMessage = "user isn't logged in";
            }
            else {
                LadderPriorities ladderPriorities = new LadderPriorities();
                ladderPriorities.userId = logResponse.userId;
                ladderPriorities.vetoedMaps = (String)input.get("vetoedMaps");
                ladderPriorities.daysCanPlay = getDaysCanPlay(input);
                ladderPriorities.startHoursOfDay = getStartHoursOfDay(input);
                ladderPriorities.endHoursOfDay = getEndHoursOfDay(input);

                LadderMatchInfo matchInfo = findProperMatch(ladderPriorities, repo);
                if(matchInfo == null) {
                    success = repo.saveLadderPriorities(ladderPriorities);
                    opponentFound = false;
                }
                else {
                    repo.saveLadderMatch(matchInfo);
                    repo.clearPriorities(matchInfo.user1Id);
                    repo.clearPriorities(matchInfo.user2Id);
                    sendLadderDiscordMessage(matchInfo, repo);
                    opponentFound = true;
                    success = true;
                }
            }
        } catch (Exception ex) {
            success = false;
            errorMessage = ex.getMessage();
            opponentFound = false;
        }


        output.put("success", success);
        output.put("errorMsg", errorMessage);
        output.put("opponentFound", opponentFound);

        return output;
    }

    private boolean sendLadderDiscordMessage(LadderMatchInfo matchInfo, IRepo repo) {
        DiscordService discord = new DiscordService(getServletContext());
        boolean success = false;
        String user1DiscordTag = repo.getDiscordTagOfUser(matchInfo.user1Id);
        String user2DiscordTag = repo.getDiscordTagOfUser(matchInfo.user2Id);

        try {
            discord.init();
            User user1Discord = discord.getUserIdByTag(user1DiscordTag);
            User user2Discord = discord.getUserIdByTag(user2DiscordTag);

            discord.sendMessageInDefaultChannel("<@" + user1Discord.getId().toString() + "> an opponent was found for you on the ladder, you play " +
                    "<@" + user2Discord.getId().toString() + "> next on the map " + matchInfo.mapPlayed + ", don't forget to report us the result of that match");
            success = true;
        } catch (Exception ex) {
            success = false;
        }

        return success;
    }

    private LadderMatchInfo findProperMatch(LadderPriorities priorities, IRepo repo) {
        List<LadderPriorities> otherUsersPriorities = repo.getAllLadderPriorities();
        LadderPriorities bestCandidate = null;
        int maxOverlappedHoursFound = 0;

        for(LadderPriorities usersPriorities : otherUsersPriorities) {
            if(usersPriorities.userId == priorities.userId)
                continue;
            int overlappedHours = findOverlappedHours(priorities, usersPriorities);
            if(overlappedHours >= 5 && overlappedHours > maxOverlappedHoursFound) {
                maxOverlappedHoursFound = overlappedHours;
                bestCandidate = usersPriorities;
            }
        }

        if(bestCandidate == null)
            return null;
        else
            return createMatchInfo(priorities, bestCandidate, repo);
    }

    private LadderMatchInfo createMatchInfo(LadderPriorities prioritiesUser1, LadderPriorities prioritiesUser2, IRepo repo) {
        List<String> mapPool = repo.getMapPool();

        String[] vetoedMapsUser1 = prioritiesUser1.vetoedMaps.split(",");
        String[] vetoedMapsUser2 = prioritiesUser2.vetoedMaps.split(",");

        ArrayList<String> allowedMaps = new ArrayList<String>();

        for(String mapName : mapPool) {
            boolean mapFits = true;
            for(int i = 0; i < vetoedMapsUser1.length; i++)
                if(vetoedMapsUser1[i].compareTo(mapName) == 0)
                    mapFits = false;
            if(mapFits == false)
                continue;;
            for(int i = 0; i < vetoedMapsUser2.length; i++)
                if(vetoedMapsUser2[i].compareTo(mapName) == 0)
                    mapFits = false;
            if(mapFits)
                allowedMaps.add(mapName);
        }

        Random random = new Random();
        int mapIndex = random.nextInt(allowedMaps.size());

        return new LadderMatchInfo(prioritiesUser1.userId, prioritiesUser2.userId, allowedMaps.get(mapIndex));
    }

    private int findOverlappedHours(LadderPriorities prioritiesUser1, LadderPriorities prioritiesUser2) {
        int totalHours = 0;
        int endHourUser1, endHourUser2;

        for(int i = 0; i < 7; i++) {
            endHourUser1 = prioritiesUser1.startHoursOfDay[i] >= prioritiesUser1.endHoursOfDay[i] ? prioritiesUser1.endHoursOfDay[i] + 24 : prioritiesUser1.endHoursOfDay[i];
            endHourUser2 = prioritiesUser2.startHoursOfDay[i] >= prioritiesUser2.endHoursOfDay[i] ? prioritiesUser2.endHoursOfDay[i] + 24 : prioritiesUser2.endHoursOfDay[i];

            int startOverlapHour = Math.max(prioritiesUser1.startHoursOfDay[i], prioritiesUser2.startHoursOfDay[i]);
            int endOverlapHour = Math.min(endHourUser1, endHourUser2);
            if(endOverlapHour - startOverlapHour > 1)
                totalHours += endOverlapHour - startOverlapHour;
        }

        return totalHours;
    }

    private int[] getEndHoursOfDay(JSONObject input) {
        int[] output = new int[7];
        int[] startHourOfDay = new int[7];

        startHourOfDay[0] = (int)((long)input.get("monStartHour"));
        startHourOfDay[1] = (int)((long)input.get("tueStartHour"));
        startHourOfDay[2] = (int)((long)input.get("wedStartHour"));
        startHourOfDay[3] = (int)((long)input.get("thrStartHour"));
        startHourOfDay[4] = (int)((long)input.get("friStartHour"));
        startHourOfDay[5] = (int)((long)input.get("satStartHour"));
        startHourOfDay[6] = (int)((long)input.get("sunStartHour"));

        output[0] = (int)((long)input.get("monEndHour"));
        output[1] = (int)((long)input.get("tueEndHour"));
        output[2] = (int)((long)input.get("wedEndHour"));
        output[3] = (int)((long)input.get("thrEndHour"));
        output[4] = (int)((long)input.get("friEndHour"));
        output[5] = (int)((long)input.get("satEndHour"));
        output[6] = (int)((long)input.get("sunEndHour"));

        for(int i=0; i < 7; i++)
            output[i] = output[i] == startHourOfDay[i] ? 24 : output[i];

        return output;
    }

    private int[] getStartHoursOfDay(JSONObject input) {
        int[] output = new int[7];
        int[] endHourOfDay = new int[7];

        endHourOfDay[0] = (int)((long)input.get("monEndHour"));
        endHourOfDay[1] = (int)((long)input.get("tueEndHour"));
        endHourOfDay[2] = (int)((long)input.get("wedEndHour"));
        endHourOfDay[3] = (int)((long)input.get("thrEndHour"));
        endHourOfDay[4] = (int)((long)input.get("friEndHour"));
        endHourOfDay[5] = (int)((long)input.get("satEndHour"));
        endHourOfDay[6] = (int)((long)input.get("sunEndHour"));

        output[0] = (int)((long)input.get("monStartHour"));
        output[1] = (int)((long)input.get("tueStartHour"));
        output[2] = (int)((long)input.get("wedStartHour"));
        output[3] = (int)((long)input.get("thrStartHour"));
        output[4] = (int)((long)input.get("friStartHour"));
        output[5] = (int)((long)input.get("satStartHour"));
        output[6] = (int)((long)input.get("sunStartHour"));

        for(int i=0; i < 7; i++)
            output[i] = output[i] == endHourOfDay[i] ? 0 : output[i];

        return output;
    }

    private boolean[] getDaysCanPlay(JSONObject input) {
        boolean[] output = new boolean[7];

        output[0] = (boolean)input.get("playOnMonday");
        output[1] = (boolean)input.get("playOnTuesday");
        output[2] = (boolean)input.get("playOnWednesday");
        output[3] = (boolean)input.get("playOnThursday");
        output[4] = (boolean)input.get("playOnFriday");
        output[5] = (boolean)input.get("playOnSaturday");
        output[6] = (boolean)input.get("playOnSunday");

        return output;
    }
}
