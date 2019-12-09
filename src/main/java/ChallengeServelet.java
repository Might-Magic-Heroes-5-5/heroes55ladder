import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

import models.ChallengeInfo;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import repo.IRepo;
import repo.Repo;
import services.DiscordService;
import services.LoggingService;

public class ChallengeServelet extends HttpServlet {

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
            case "createChallenge":
                response = createChallenge(input, logResponse, repo);
                break;
            case "acceptChallenge":
                response = acceptChallenge(input, logResponse, repo);
                break;
            case "removeChallenge":
                response = removeChallenge(input, logResponse, repo);
                break;
        }

        resp.getWriter().write(response.toJSONString());
    }

    private JSONObject removeChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();

        if(logResponse.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "You aren't logged in to the server. Please log in first before trying accepting any challenges");
            return output;
        }
        int challengeId = (int)((long)input.get("challengeId"));
        ChallengeInfo info = repo.getChallengeInfo(challengeId);
        if(info == null) {
            output.put("success", false);
            output.put("errorMsg", "The challenge wasn't found.");
            return output;
        }

        if(info.userId != logResponse.userId) {
            output.put("success", false);
            output.put("errorMsg", "You are trying to remove a challenge that isn't yours");
            return output;
        }

        repo.removeChallenge(info.id);

        output.put("success", true);
        output.put("errorMsg", "The challenge was removed");
        return output;
    }

    private JSONObject acceptChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        DiscordService discord = new DiscordService(getServletContext());

        if(logResponse.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "You aren't logged in to the server. Please log in first before trying accepting any challenges");
            return output;
        }
        int challengeId = (int)((long)input.get("challengeId"));
        ChallengeInfo info = repo.getChallengeInfo(challengeId);
        if(info == null) {
            output.put("success", false);
            output.put("errorMsg", "The challenge wasn't found, perhaps somebody already accepted it by now, refresh the page to see the current challenges.");
            return output;
        }

        if(info.userId == logResponse.userId) {
            output.put("success", false);
            output.put("errorMsg", "You can't accept your own challenge. Please pick a different challenge to accept or wait for somebody to accpet your challenge");
            return output;
        }

        String userDiscordTag = repo.getDiscordTagOfUser(logResponse.userId);
        String challengeddUserTag = repo.getDiscordTagOfUser(info.userId);
        boolean success = false;

        try {
            discord.init();
            List<User> mentionedUsers = new ArrayList<User>();
            User userDiscord = discord.getUserIdByTag(userDiscordTag);
            User challengedUserDiscord = discord.getUserIdByTag(challengeddUserTag);

            success = discord.sendMessageInDefaultChannel("<@" + userDiscord.getId().toString() + "> decided to accept <@" + challengedUserDiscord.getId().toString() + "> challenge. Its battle time now.");
        } catch (Exception ex) {
            success = false;
        }

        if(success == false) {
            output.put("success", false);
            output.put("errorMsg", "Sorry, an error has occured.");
            return output;
        }

        repo.removeChallenge(info.id);

        output.put("success", true);

        return output;
    }

    private JSONObject getInfoOfChallengeOpener(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();

        int challengeId = (int)((long)input.get("challengeId"));

        ChallengeInfo info = repo.getChallengeInfo(challengeId);

        if(info == null) {
            output.put("success", false);
            output.put("errorMsg", "The challenge wasn't found, perhaps somebody already accepted it");
            return output;
        }

        output.put("challengeId", info.id);

        return output;
    }

    private JSONObject createChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();

        boolean success = false;
        String errorMessage = null;

        if(logResponse.isLoggedIn == false) {
            errorMessage = "user isn't logged in";
        }
        else {
            ChallengeInfo challengeInfo = new ChallengeInfo();
            challengeInfo.userId = logResponse.userId;
            challengeInfo.mapName = (String)input.get("mapName");
            challengeInfo.isNormalDifficulty = (boolean)input.get("normalDifficulty");
            challengeInfo.isHardDifficulty = (boolean)input.get("hardDifficulty");
            challengeInfo.isHeroicDifficulty = (boolean)input.get("heroicDifficulty");
            challengeInfo.isImpossibleDifficulty = (boolean)input.get("impossibleDifficulty");
            challengeInfo.startTime = (int)((long)input.get("startHour"));
            challengeInfo.endTime = (int)((long)input.get("endHour"));

            success = repo.createChallenge(challengeInfo);
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);

        return output;
    }
}
