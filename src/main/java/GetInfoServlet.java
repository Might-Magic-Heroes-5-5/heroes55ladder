import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import models.ChallengeInfo;
import models.GladiatorStreak;
import models.OngoingLadderMatch;
import models.UserRank;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import repo.IRepo;
import repo.Repo;
import services.ReportMatchService;

public class GetInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        IRepo repo = Repo.getRepo(getServletContext());
        JSONObject responseToSend;
        String command = (String)req.getParameter("command");

        switch(command) {
            case "getTopRankedUsers":
                responseToSend = getTopRankedUsers(repo);
                break;
            case "getFirstChallenges":
                responseToSend = getFirstChallenges(repo);
                break;
            case "getMatchHisotry":
                responseToSend = getRecentMatches(repo);
                break;
            case "getCurrentLadderMatches":
                responseToSend = getCurrentLadderMatches(repo);
                break;
            case "getCurrentStreaks":
                responseToSend = getCurrentGladiatorStreaks(repo);
                break;
            case "getHallOfFame":
                responseToSend = getHallOfFame(repo);
                break;
            default:
                responseToSend = new JSONObject();
        }

        resp.getWriter().write(responseToSend.toJSONString());
    }

    private JSONObject getHallOfFame(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray streakssArray = new JSONArray();

        List<GladiatorStreak> streaks = repo.getHallOfFame(10);
        HashMap<Integer, List<String>> usedHeroesOfGladiators = repo.getHallOfFameUseddHeroes();

        for(GladiatorStreak streak : streaks) {
            JSONObject streakJson = new JSONObject();
            streakJson.put("username", streak.username);
            streakJson.put("streak", streak.streak);
            List<String> usedHeroes = usedHeroesOfGladiators.get(streak.userId);
            JSONArray heroesUsed = prepareUsedHeroesJson(usedHeroes);
            streakJson.put("usedHeroes", heroesUsed);
            streakssArray.add(streakJson);
        }

        objectToSends.put("streaks", streakssArray);

        return objectToSends;
    }

    private JSONObject getCurrentGladiatorStreaks(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray streaksArray = new JSONArray();

        List<GladiatorStreak> streaks = repo.getCurrentGladiatorStreaks(10);
        HashMap<Integer, List<String>> usedHeroesOfGladiators = repo.getUsedHeroesOfGladiators();

        for(GladiatorStreak streak : streaks) {
            JSONObject streakJson = new JSONObject();
            streakJson.put("username", streak.username);
            streakJson.put("streak", streak.streak);
            List<String> usedHeroes = usedHeroesOfGladiators.get(streak.userId);
            JSONArray heroesUsed = prepareUsedHeroesJson(usedHeroes);
            streakJson.put("usedHeroes", heroesUsed);
            streaksArray.add(streakJson);
        }

        objectToSends.put("streaks", streaksArray);

        return objectToSends;
    }

    private JSONArray prepareUsedHeroesJson(List<String> usedHeroes) {
        JSONArray heroesArray = new JSONArray();

        if(usedHeroes != null)
            for(String hero : usedHeroes)
                heroesArray.add(hero);

        return heroesArray;
    }

    private JSONObject getCurrentLadderMatches(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray matchesArray = new JSONArray();

        List<OngoingLadderMatch> matches = repo.getOngoingLadderMatches(10);

        for(OngoingLadderMatch match : matches) {
            JSONObject matchJson = new JSONObject();
            matchJson.put("user1", match.username1);
            matchJson.put("user2", match.username2);
            matchJson.put("map", match.mapPlayed);
            matchJson.put("startedOnDate", match.startedOn.getTime());
            matchesArray.add(matchJson);
        }

        objectToSends.put("matches", matchesArray);

        return objectToSends;
    }

    private JSONObject getRecentMatches(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray matchesArray = new JSONArray();

        List<ReportMatchService.MatchInfo> matches = repo.getRecentMatches(10);

        for(ReportMatchService.MatchInfo match : matches) {
            JSONObject matchJson = new JSONObject();
            matchJson.put("date", match.dateReported.getTime());
            String mapString = " on map " + match.mapName;
            if(match.mapName.compareTo("") == 0)
                mapString = "";
            matchJson.put("matchInfo", match.winningUsername + " defeated " + match.losingUsername + mapString);
            matchesArray.add(matchJson); //playerA defeated playerB on >map< with difficulity setting >difficulity<
        }

        objectToSends.put("matches", matchesArray);
        return objectToSends;
    }

    private JSONObject getFirstChallenges(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray challengesArray = new JSONArray();

        List<ChallengeInfo> challenges = repo.getChallenges(10);

        for(ChallengeInfo challenge : challenges) {
            challengesArray.add(challenge.toJsonObject());
        }

        objectToSends.put("challenges", challengesArray);
        return objectToSends;
    }

    private JSONObject getTopRankedUsers(IRepo repo) {
        JSONObject objectToSends = new JSONObject();
        JSONArray namesArray = new JSONArray();

        List<UserRank> topUsers = repo.getTopUserRanks();

        for(UserRank user : topUsers) {
            JSONObject nameAndPoints = new JSONObject();
            nameAndPoints.put("name", user.username);
            nameAndPoints.put("points", user.points);
            namesArray.add(nameAndPoints);
        }

        objectToSends.put("names", namesArray);

        return objectToSends;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
