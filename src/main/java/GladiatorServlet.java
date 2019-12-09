import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiConsumer;

import config.Config;
import models.GladiatorStreak;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;
import org.json.JSONArray;
import repo.IRepo;
import repo.Repo;
import services.DiscordService;
import services.LoggingService;
import models.*;
import services.ReportMatchService;

public class GladiatorServlet  extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoggingService logService = new LoggingService(req, getServletContext());
        LoggingService.LoginResponse logResponse = logService.checkLogin();
        IRepo repo = Repo.getRepo(getServletContext());
        JSONObject response = new JSONObject();

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String json = "";
        if(br != null)
            json = br.readLine();
        JSONObject input = new JSONObject(json);

        String command = (String)input.get("command");

        switch (command) {
            case "getGladiatorsCanChallenge":
                response = getGladiatorsCanChallenge(input, logResponse, repo);
                break;
            case "challengeGaldiator":
                response = challengeAnotherGladiator(input, logResponse, repo);
                break;
            case "getChallengeAndIsLoggedByIden":
                response = getChallengeAndCheckIfLoggedByIdentifier(input, logService, repo);
                break;
            case "loginByIdenAndPassword":
                response = loginByIdentifierAndPassword(input, logService, repo);
                break;
            case "declineChallenge":
                response = declineGladiatorChallenge(input, logResponse, repo);
                break;
            case "acceptChallenge":
                response = acceptGladiatorChallengeAndPickMap(input, logResponse, repo);
                break;
            case "pickHeroForChallenge":
                response = pickHeroForChallenge(input, logResponse, repo);
                break;
            case "getUserOngoingMatches":
                response = getOngoingGladiatorMatchesOfUser(logResponse, repo);
                break;
            case "joinArena":
                response = addUserToArena(logResponse, repo);
                break;
        }

        resp.getWriter().write(response.toString());
    }

    private JSONObject addUserToArena(LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        boolean success = false;
        String errorMessage = null;

        try {
            if(logResponse.isLoggedIn == false)
                errorMessage = "you are not logged in, please log in first (or sign up if you don't have an account)";
            else
            {
                if(repo.checkIfGladiator(logResponse.userId))
                    errorMessage = "you are already a gladiator, you can't join twice";
                else
                {
                    success = repo.addNewGladiator(logResponse.userId);
                    if(success)
                        errorMessage = "welcome to the arena";
                    else
                        errorMessage = "an error occured, please try again later";
                }
            }
        } catch (Exception ex) {
            success = false;
            errorMessage = ex.getMessage();
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);

        return output;
    }

    private JSONObject getOngoingGladiatorMatchesOfUser(LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        JSONArray matchesArray = new JSONArray();
        int opponentUserId;
        String opponentUsername;

        boolean success = false;
        String errorMessage = null;

        try {
            List<OngoingLadderMatch> matches = repo.getOngoingLadderMatchesOfUser(logResponse.userId);

            for(OngoingLadderMatch match : matches) {
                if(match.isGladiatorMatch == false)
                    continue;;
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
                matchesArray.put(matchJson);
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

    private JSONObject pickHeroForChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        boolean success = true;
        String errorMsg = null;

        if(logResponse.isLoggedIn) {
            int challengeId = input.getInt("challengeId");
            String heroName = input.getString("heroName");
            int heroId = repo.getHeroId(heroName);
            if(heroId > 0 && isHeroEligible(heroId, logResponse.userId, challengeId, repo)) {
                GladiatorChallenge challenge = repo.getGladiatorChallenge(challengeId);
                if(challenge != null) {
                    if(challenge.MapToBePlayed > 0) {
                        if(challenge.ChallengingUserId == logResponse.userId || challenge.ChallengedUserId == logResponse.userId) {
                            if(challenge.ChallengingUserId == logResponse.userId) {
                                if(challenge.ChallengedUserHero > 0 && challenge.ChallengedUserHero != heroId) {
                                    challenge.ChallengingUserHero = heroId;
                                    success = startGladiatorMatch(challenge, repo);
                                    if(success)
                                        errorMsg = "The match has started, you are tagged with more info in our discord server.";
                                    else
                                        errorMsg = "We had some problems starting the match, please try again.";
                                } else {
                                    if(challenge.ChallengedUserHero <= 0) {
                                        repo.updateChallengingUserHero(challengeId, heroId);
                                        success = true;
                                        errorMsg = "We saved your pick, once your opponent picks a hero as well it will be revealed to you so that you can start your match. You will be notified by Disocrd about that";
                                    }
                                    else {
                                        repo.resetHeroesForChallenge(challengeId);
                                        repo.addHeroAsUnavailibleForChallenge(challengeId, heroId);
                                        success = false;
                                        errorMsg = "You opponent picked the same hero as you. From now on this hero can't be picked for this match, please pick a different eligible hero";
                                    }
                                }
                            } else {
                                if(challenge.ChallengingUserHero > 0 && challenge.ChallengingUserHero != heroId) {
                                    challenge.ChallengedUserHero = heroId;
                                    success = startGladiatorMatch(challenge, repo);
                                    if(success)
                                        errorMsg = "The match has started, you are tagged with more info in our discord server.";
                                    else
                                        errorMsg = "We had some problems starting the match, please try again.";
                                } else {
                                    if(challenge.ChallengingUserHero <= 0) {
                                        repo.updateChallengedUserHero(challengeId, heroId);
                                        success = true;
                                        errorMsg = "We saved your pick, once your opponent picks a hero as well it will be revealed to you so that you can start your match. You will be notified by Discord about that";
                                    }
                                    else {
                                        repo.resetHeroesForChallenge(challengeId);
                                        repo.addHeroAsUnavailibleForChallenge(challengeId, heroId);
                                        success = false;
                                        errorMsg = "You opponent picked the same hero as you. From now on this hero can't be picked for this match, please pick a different eligible hero";
                                    }
                                }
                            }
                        } else {
                            success = false;
                            errorMsg = "You have nothing to do with this challenge";
                        }
                    } else {
                        success = false;
                        errorMsg = "You can't pick a hero for this challenge, the map wasn't figured out, yet.";
                    }
                } else {
                    success = false;
                    errorMsg = "The challenge wasn't found. It was either declined or started";
                }
            } else {
                success = false;
                errorMsg = "Please pick an eligible hero, check the rules if you aren't sure which heroes you can play.";
            }
        } else {
            success = false;
            errorMsg = "You are not logged in, please log in and try again";
        }

        output.put("success", success);
        output.put("errorMsg", errorMsg);

        return output;
    }

    private boolean startGladiatorMatch(GladiatorChallenge challenge, IRepo repo) {
        repo.saveLadderGladiatorMatch(challenge);
        repo.clearUnavailibleHeroesForChallenge(challenge.Id);

        return sendGladiatorDiscordMatchMessage(challenge, repo);
    }

    private boolean sendGladiatorDiscordMatchMessage(GladiatorChallenge challenge, IRepo repo) {
        DiscordService discord = new DiscordService(getServletContext());
        boolean success;

        try {
            discord.init();
            String userDiscordTag = repo.getDiscordTagOfUser(challenge.ChallengingUserId);
            String challengeddUserTag = repo.getDiscordTagOfUser(challenge.ChallengedUserId);
            User userDiscord = discord.getUserIdByTag(userDiscordTag);
            User challengedUserDiscord = discord.getUserIdByTag(challengeddUserTag);
            String challenginHero = repo.getHeroName(challenge.ChallengingUserHero);
            String challengedHero = repo.getHeroName(challenge.ChallengedUserHero);
            String map = repo.getMapName(challenge.MapToBePlayed);

            if(map != null) {
                String message = "Two gladiators decided to enter the arena for another match. This time <@" + userDiscord.getId().toString() + "> (" + challenginHero + ") challenged <@" + challengedUserDiscord.getId().toString()
                        + "> (" + challengedHero + "), who accepted the challenge. They will play on the map " + map + ". The winner will increase his streak, the loser will go down to a 0 streak.";

                success = discord.sendMessageInDefaultChannel(message);
            }
            else success = false;
        } catch(Exception ex) {
            success = false;
        }

        return success;
    }

    private boolean isHeroEligible(int heroId, int userId, int challengeId, IRepo repo) {
        String heroName = repo.getHeroName(heroId);
        if(heroName == null)
            return false;

        List<Integer> usedHeroesByGladiator = repo.getUsedHeroOfGladiator(userId);
        if(usedHeroesByGladiator.contains(heroId))
            return false;

        List<Integer> unavailibleHeroesForChallenge = repo.getUnavailibleHeroesForChallenge(challengeId);
        if(unavailibleHeroesForChallenge.contains(heroId))
            return false;

        return true;
    }

    private JSONObject loginByIdentifierAndPassword(JSONObject input, LoggingService logService, IRepo repo) {
        LoggingService.LoginResponse logResponse = logService.checkLogin();
        ReportMatchService reportService = new ReportMatchService(getServletContext());
        JSONObject output = new JSONObject();
        String identificatorStr = (String)input.get("identificator");
        if(identificatorStr == null) {
            output.put("success", false);
            output.put("errorMsg", "No identificator found");
            return output;
        }
        UUID identificator = UUID.fromString(identificatorStr);
        GladiatorChallenge challenge = repo.getGladiatorChallenge(identificator);

        if(challenge == null) {
            output.put("success", false);
            output.put("errorMsg", "No such challenge was found, perhaps it was already confirmed or the challenger canceled it");
            return output;
        }

        String password = (String)input.get("password");
        String username = (String)input.get("username");
        int userId = repo.getUserId(username);
        if(userId != challenge.ChallengedUserId && userId != challenge.ChallengingUserId) {
            output.put("success", false);
            output.put("errorMsg", "You have nothing to do with this challenge!");
            return output;
        }

        if(userId == challenge.ChallengingUserId && challenge.MapToBePlayed <= 0) {
            output.put("success", false);
            output.put("errorMsg", "Please wait for the peson you challenged to accept the challenge first");
            return output;
        }

        LoggingService.LoginResponse response = logService.loginWithUsernamePassword(username, password);

        if(response.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "Bad password");
            return output;
        }

        output.put("success", true);
        output.put("isLoggedIn", true);
        output.put("challengeId", challenge.Id);
        output.put("RankPositionOfChallengingUser", challenge.RankPositionOfChallengingUser.toInt());
        output.put("challengingUsername", repo.getUsername(challenge.ChallengingUserId));
        if(challenge.MapToBePlayed > 0)
            output.put("mapToBePlayed", repo.getMapName(challenge.MapToBePlayed));
        return output;
    }

    private JSONObject getChallengeAndCheckIfLoggedByIdentifier(JSONObject input, LoggingService logService, IRepo repo) {
        LoggingService.LoginResponse logResponse = logService.checkLogin();
        JSONObject output = new JSONObject();
        String identificatorStr = (String)input.get("identificator");
        if(identificatorStr == null) {
            output.put("success", false);
            output.put("errorMsg", "No identificator found");
            return output;
        }
        UUID identificator = UUID.fromString(identificatorStr);

        GladiatorChallenge challenge = repo.getGladiatorChallenge(identificator);
        if(challenge == null) {
            output.put("success", false);
            output.put("errorMsg", "The challenge wasn't found, perhaps the link you used is a long one.");
            return output;
        }

        if(logResponse.isLoggedIn) {
            if(challenge.ChallengedUserId != logResponse.userId) {
                logService.logOut();
                output.put("success", true);
                output.put("isLoggedIn", false);
                output.put("challengingUsername", "");
                return output;
            }
            else {
                output.put("success", true);
                output.put("isLoggedIn", true);
                output.put("challengeId", challenge.Id);
                output.put("RankPositionOfChallengingUser", challenge.RankPositionOfChallengingUser.toInt());
                output.put("challengingUsername", repo.getUsername(challenge.ChallengingUserId));
                output.put("mapToBePlayed", repo.getMapName((challenge.MapToBePlayed)));
                return output;
            }
        }
        else {
            output.put("success", true);
            output.put("isLoggedIn", false);
            output.put("challengingUsername", "");
            return output;
        }
    }

    private class AcceptChallengeParameters {
        public boolean Success;
        public String ErrorMsg;
        public int MapVeto;
        public int Priority1;
        public int Priority2;
    }

    public AcceptChallengeParameters getAndVarifyParametersForAccepting(JSONObject input, GladiatorChallenge challenge, HashMap<Integer, String> mapPool) {
        AcceptChallengeParameters parameters = new AcceptChallengeParameters();

        ;
        String mapVetoStr = "", priority1Str = "", priority2Str = "";
        if(input.has("mapVeto"))
            mapVetoStr = input.getString("mapVeto");
        else {
            JSONArray mapPriorities = input.getJSONArray("mapPriorities");
            if(mapPriorities != null) {
                priority1Str = mapPriorities.getString(0);
                priority2Str = mapPriorities.getString(1);
            }
        }

        String finalMapVetoStr = mapVetoStr;
        String finalPriority1Str = priority1Str;
        String finalPriority2Str = priority2Str;
        parameters.MapVeto = 0; parameters.Priority1 = 0; parameters.Priority2 = 0;
        BiConsumer<Integer, String> finIndexes = (index, name) -> {
            if(name.compareTo(finalMapVetoStr) == 0)
                parameters.MapVeto = index;
            if(name.compareTo(finalPriority1Str) == 0)
                parameters.Priority1 = index;
            if(name.compareTo(finalPriority2Str) == 0)
                parameters.Priority2 = index;
        };
        mapPool.forEach(finIndexes);

        parameters.Success = true;
        return parameters;
    }

    private JSONObject acceptGladiatorChallengeAndPickMap(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        boolean success = true;
        String errorMsg = null;
        int challengeId = input.getInt("challengeId");

        if(logResponse.isLoggedIn) {
            GladiatorChallenge challenge = repo.getGladiatorChallenge(challengeId);
            if(challenge != null) {
                if(challenge.MapToBePlayed == 0) {
                    HashMap<Integer, String> mapPool = repo.getMapPoolHashMap();
                    AcceptChallengeParameters parameters = getAndVarifyParametersForAccepting(input, challenge, mapPool);
                    if(parameters.Success) {
                        int mapToBePlayed = -1;
                        switch(challenge.RankPositionOfChallengingUser) {
                            case LowerRanked:
                                mapToBePlayed = challenge.MapPriority1 != parameters.MapVeto ? challenge.MapPriority1 : challenge.MapPriority2;
                                break;
                            case ExactlyRanked:
                                mapPool.remove(challenge.MapVetoed);
                                mapPool.remove(parameters.MapVeto);
                                Random random = new Random();
                                final int[] index = {0, 0};
                                int randomIndex = random.nextInt(mapPool.size());
                                mapPool.forEach(new BiConsumer<Integer, String>() {
                                    @Override
                                    public void accept(Integer integer, String s) {
                                        if(index[0] == randomIndex)
                                            index[1] = integer;
                                        index[0]++;
                                    }
                                });
                                mapToBePlayed = index[1];
                                break;
                            case HigherRanked:
                                mapToBePlayed = parameters.Priority1 != challenge.MapVetoed ? parameters.Priority1 : parameters.Priority2;
                                break;
                        }
                        repo.putMapToPlayInChallenge(challenge.Id, mapToBePlayed);
                        sendChallenngGladiatorThePickedMap(challenge, mapToBePlayed, repo);
                        output.put("mapToBePlayed", mapPool.get(mapToBePlayed));
                    } else
                        errorMsg = parameters.ErrorMsg;
                } else {
                    success = false;
                    errorMsg = "you already made your map choices for this challenge";
                }
            } else {
                success = false;
                errorMsg = "this challenge not longer exists";
            }
        } else {
            success = false;
            errorMsg = "you aren't logged it";
        }

        output.put("success", success);
        output.put("errorMsg", errorMsg);
        return output;
    }

    private JSONObject declineGladiatorChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        boolean success = true;
        String errorMsg = null;

        if(logResponse.isLoggedIn) {
            String identificatorStr = (String)input.get("identificator");
            if(identificatorStr != null) {
                UUID identificator = UUID.fromString(identificatorStr);
                success = repo.deleteGladiatorChallenge(logResponse.userId, identificator);
                if(success == false)
                    errorMsg = "the challenge was not found, it was either already confirmed or may be not even your challenge";
            } else {
                success = false;
                errorMsg = "not challenge identificator found";
            }
        } else {
            success = false;
            errorMsg = "you aren't logged it";
        }

        output.put("success", success);
        output.put("errorMsg", errorMsg);
        return output;
    }

    private JSONObject reportArenaMatch(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        return null;
    }

    private JSONObject challengeAnotherGladiator(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        boolean success = true;
        String errorMsg = null;

        if(logResponse.isLoggedIn) {
            List<GladiatorStreak> gladiators = repo.getCurrentGladiatorStreaks(1000);
            assignRanksToGladiators(gladiators);
            int rankOfUser = findRankOfGladiator(gladiators, logResponse.userId);
            int challengedUserId = (int)(input.get("userToChallenge"));
            int rankOfChallengedUser = findRankOfGladiator(gladiators, challengedUserId);
            RankPlacement rankPosition = findRankPosition(rankOfUser, rankOfChallengedUser);
            if(rankPosition != RankPlacement.CantPlay) {
                UUID identificator = UUID.randomUUID();
                int priority1 = 1;
                int priority2 = 1;
                int mapVeto = 1;
                if( rankPosition == RankPlacement.LowerRanked) {
                    JSONArray mapPrioritiesJson = input.getJSONArray("mapPriorities");
                    String priority1Str = (String)mapPrioritiesJson.get(0);
                    String priority2Str = (String)mapPrioritiesJson.get(1);
                    if(priority1Str != null || priority2Str != null) {
                        priority1 = repo.getMapId(priority1Str);
                        priority2 = repo.getMapId(priority2Str);
                        if(priority1 == priority2) {
                            success = false;
                            errorMsg = "You must choose two different maps to veto!";
                        }
                    }
                    else {
                        success = false;
                        errorMsg = "Choose maps that you priorotize, if you didn't have this option please refresh the page and challenge again.";
                    }
                } else {
                    String mapVetoStr = (String)input.get("mapVeto");
                    if(mapVetoStr != null)
                        mapVeto = repo.getMapId(mapVetoStr);
                    else {
                        success = false;
                        errorMsg = "Please veto a map, if you didn't have such an option please refresh the page and challenge again.";
                    }
                }

                if(success) {
                    success = sendDiscordGladiatorChallenge(logResponse.userId, logResponse.username, challengedUserId, identificator, repo);
                    if(success)
                        repo.saveGladiatorChallenge(logResponse.userId, challengedUserId, rankPosition, priority1, priority2, mapVeto, identificator);
                    else
                        errorMsg = "an error occured with our discord bot, please try challenging later.";
                }
            } else {
                success = false;
                errorMsg = "You cannot challenge the user you play against due to the difference between your ranks, check the rules to see who you can really challenge";
            }
        } else {
            success = false;
            errorMsg = "you aren't logged it";
        }

        output.put("success", success);
        output.put("errorMsg", errorMsg);
        return output;
    }

    private boolean sendChallenngGladiatorThePickedMap(GladiatorChallenge challenge, int mapToBePlayed, IRepo repo) {
        DiscordService discord = new DiscordService(getServletContext());
        String challengingdUserDiscordTag = repo.getDiscordTagOfUser(challenge.ChallengingUserId);
        boolean success;
        String siteUrl = Config.getSiteUrl(getServletContext());

        try {
            String mapName = repo.getMapName(mapToBePlayed);
            String username = repo.getUsername(challenge.ChallengedUserId);
            List<String> messageLines = new ArrayList<String>();
            messageLines.add("The gladiator " + username + " accepted your challenge in the gladiator arena. The match will be played on  " + mapName + ", its time to pick your hero for the match." +
                    "Please click down here pick your hero..");
            messageLines.add(siteUrl + "/confirmChallengePage.jsp?iden=" + challenge.identificator.toString());
            discord.sendMessageToUser(challengingdUserDiscordTag, messageLines);
            success = true;
        } catch(Exception ex) {
            success = false;
        }

        return success;
    }

    private boolean sendDiscordGladiatorChallenge(int userId, String username, int challengedUserId, UUID identificator, IRepo repo) {
        DiscordService discord = new DiscordService(getServletContext());
        String challengedUserDiscordTag = repo.getDiscordTagOfUser(challengedUserId);
        boolean success;
        String siteUrl = Config.getSiteUrl(getServletContext());

        try {
            List<String> messageLines = new ArrayList<String>();
            messageLines.add("The gladiator " + username + " wants to challenge you in Gladiator Arena tournament. " +
                    "Please click down here to either confirm or decline that challenge.");
            messageLines.add(siteUrl + "/confirmChallengePage.jsp?iden=" + identificator.toString());
            discord.init();
            success = discord.sendMessageToUser(challengedUserDiscordTag, messageLines);
        } catch(Exception ex) {
            success = false;
        }

        return success;
    }

    private RankPlacement findRankPosition(int challengedUserId, int rankOfChallengedUser) {
        if(challengedUserId < rankOfChallengedUser -1 || challengedUserId > rankOfChallengedUser + 1)
            return RankPlacement.CantPlay;

        if(challengedUserId > rankOfChallengedUser)
            return RankPlacement.LowerRanked;

        if(challengedUserId < rankOfChallengedUser)
            return RankPlacement.HigherRanked;

        return RankPlacement.ExactlyRanked;
    }

    private JSONObject getGladiatorsCanChallenge(JSONObject input, LoggingService.LoginResponse logResponse, IRepo repo) {
        JSONObject output = new JSONObject();
        JSONArray gladiatorsArray = new JSONArray();
        String usedHeroesStr;

        boolean success = false;
        String errorMessage = null;

        try {
            List<GladiatorStreak> gladiators = repo.getCurrentGladiatorStreaks(1000);
            assignRanksToGladiators(gladiators);
            int rankOfUser = findRankOfGladiator(gladiators, logResponse.userId);
            HashMap<Integer, List<String>> usedHeroesOfGladiators = repo.getUsedHeroesOfGladiators();
            int userRank = 0;

            for(GladiatorStreak gladiator : gladiators) {
                JSONObject gladiatorJson = new JSONObject();
                if(gladiator.userId == logResponse.userId) {
                    userRank = gladiator.rank;
                    continue;
                }
                if(gladiator.rank > rankOfUser +1 || gladiator.rank < rankOfUser -1)
                    continue;

                List<String> usedHeroes = usedHeroesOfGladiators.get(gladiator.userId);
                usedHeroesStr = writeListToString(usedHeroes);
                gladiatorJson.put("userId", gladiator.userId);
                gladiatorJson.put("streak", gladiator.streak);
                gladiatorJson.put("username", gladiator.username);
                gladiatorJson.put("usedHeroes", usedHeroesStr);
                gladiatorJson.put("rank", gladiator.rank);
                gladiatorsArray.put(gladiatorJson);
            }

            output.put("gladiators", gladiatorsArray);
            output.put("userRank", userRank);
            success = true;
        } catch (Exception ex) {
            success = false;
            errorMessage = ex.getMessage();
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);

        return output;
    }

    private String writeListToString(List<String> usedHeroes) {
        if(usedHeroes == null)
            return "";

        StringBuilder SB = new StringBuilder();
        boolean first = true;

        for(String hero : usedHeroes)
        {
            if(first == false)
                SB.append(", ");
            else
                first = false;

            SB.append(hero);
        }

        return SB.toString();
    }

    private void assignRanksToGladiators(List<GladiatorStreak> gladiators) {
        int rank = 0;
        int previosStreak = 1000;

        for(GladiatorStreak gladiator : gladiators) {
            if(gladiator.streak < previosStreak) {
                previosStreak = gladiator.streak;
                rank++;
            }
            gladiator.rank = rank;
        }
    }

    private int findRankOfGladiator(List<GladiatorStreak> gladiators, int userId) {
        for(GladiatorStreak gladiator : gladiators)
            if(gladiator.userId == userId)
                return gladiator.rank;

        return 1000;
    }
}
