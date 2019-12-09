package repo;
import services.*;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocalRepo implements IRepo {

    class PasswordMailIdentificator {
        public Long hashedPassword;
        public String email;
        public UUID identificator;
        public boolean confirmed;

        public PasswordMailIdentificator(Long hashedPassword, String email, UUID identificator) {
            this.hashedPassword = hashedPassword;
            this.email = email;
            this.identificator = identificator;
            confirmed = false;
        }
    }

    private static HashMap<String, PasswordMailIdentificator> UsernamePassword = null;
    private static HashMap<String, String> EmailToPassword = null;
    private static HashMap<Integer, ChallengeInfo> Challanges = null;
    private static HashMap<String, Integer> UsernameELO = null;
    private static ArrayList<ReportMatchService.MatchInfo> MatchHistory = null;
    private static HashMap<UUID, ReportMatchService.MatchReport> MatchReports = null;

    public LocalRepo() {
        if(UsernamePassword == null)
            UsernamePassword = new HashMap<String, PasswordMailIdentificator>();
        if(Challanges == null)
            Challanges = new HashMap<Integer, ChallengeInfo>();
        if(EmailToPassword == null)
            EmailToPassword = new HashMap<String, String>();
        if(UsernameELO == null)
            UsernameELO = new HashMap<String, Integer>();
        if(MatchHistory == null)
            MatchHistory = new ArrayList<ReportMatchService.MatchInfo>();
        if(MatchReports == null)
            MatchReports = new HashMap<UUID, ReportMatchService.MatchReport>();
    }

    @Override
    public List<UserRank> getTopUserRanks() {
        List<UserRank> topUsers = new ArrayList<UserRank>();

        for(int i=0; i < 10; i++) {
            topUsers.add(new UserRank("user" + i, 100 - i*10));
        }

        return topUsers;
    }

    @Override
    public boolean checkUsernamePassword(String username, long hashedPassword) {
        boolean match = false;

        if(UsernamePassword.containsKey(username)) {
            PasswordMailIdentificator stroedPassword = UsernamePassword.get(username);
            if(stroedPassword.hashedPassword == hashedPassword && stroedPassword.confirmed)
                match = true;
        }

        return match;
    }

    @Override
    public boolean addNewUser(String username, long hashedPassword, String email, UUID identificator) {
        if(UsernamePassword.containsKey(username))
            return false;

        if(EmailToPassword.containsKey(email))
            return false;

        PasswordMailIdentificator userInfo = new PasswordMailIdentificator(hashedPassword, email, identificator);
        UsernamePassword.put(username, userInfo);
        EmailToPassword.put(email, username);

        return true;
    }

    @Override
    public boolean existsUsername(String username) {
        if(UsernamePassword.containsKey(username))
            return true;
        else
            return false;
    }

    @Override
    public boolean createChallenge(ChallengeInfo challenge) {
        int key = Challanges.size() + 1;
        Challanges.put(key, challenge);
        return true;
    }

    @Override
    public List<ChallengeInfo> getChallenges(int maxNumOfChallenges) {
        ArrayList<ChallengeInfo> firstChallenges = new ArrayList<>();

        for(int i=1; firstChallenges.size() < maxNumOfChallenges && i < Challanges.size(); i++) {
            ChallengeInfo challenge = Challanges.get(i);
            if(challenge != null) {
                challenge.id = i;
                firstChallenges.add(challenge);
            }
        }
            ;

        return firstChallenges;
    }

    @Override
    public List<ReportMatchService.MatchInfo> getRecentMatches(int maxNumOfMatches) {
        return null;
    }

    @Override
    public boolean existsDiscord(String discord) {
        if(EmailToPassword.containsKey(discord))
            return true;
        else
            return false;
    }

    @Override
    public boolean confirmUser(String username, UUID identificator) {
        if(UsernamePassword.containsKey(username) == false)
            return false;

        PasswordMailIdentificator userInfo = UsernamePassword.get(username);
        if(userInfo.identificator.compareTo(identificator) != 0)
            return false;

        userInfo.confirmed = true;
        UsernameELO.put(username, 1000);
        return true;
    }

    @Override
    public void removeUser(String username) {
        if(UsernamePassword.containsKey(username) == false)
            return;

        PasswordMailIdentificator userInfo = UsernamePassword.get(username);
        EmailToPassword.remove(userInfo.email);
        UsernamePassword.remove(username);
    }

    @Override
    public String getDiscordTaglOfUsername(String username) {
        if(UsernamePassword.containsKey(username) == false)
            return null;

        PasswordMailIdentificator passwordMail = UsernamePassword.get(username);
        return passwordMail.email;
    }

    @Override
    public String getDiscordTagOfUser(int userId) {
        return null;
    }

    @Override
    public void saveReport(ReportMatchService.MatchReport report, UUID identificator) {
        MatchReports.put(identificator, report);
    }

    @Override
    public void saveMatch(ReportMatchService.MatchInfo info) {
        MatchHistory.add(info);
    }

    @Override
    public double getPlayerEloById(int losingUserId) {
        return 0;
    }

    @Override
    public void updateELO(int userid, double  newELO) {

    }

    @Override
    public ReportMatchService.MatchReport getReport(UUID identificator) {
        if(MatchReports.containsKey(identificator) == false)
            return null;

        ReportMatchService.MatchReport report = MatchReports.get(identificator);
        return report;
    }

    @Override
    public ReportMatchService.MatchReport pullReport(UUID identificator) {
        if(MatchReports.containsKey(identificator) == false)
            return null;

        ReportMatchService.MatchReport report = MatchReports.get(identificator);
        MatchReports.remove(identificator);
        return report;
    }

    @Override
    public ChallengeInfo getChallengeInfo(int challengeId) {
        return Challanges.get(challengeId);
    }

    @Override
    public int getUserId(String username) {
        return 0;
    }

    @Override
    public void setInitialEloForUser(int userId) {

    }

    @Override
    public void removeChallenge(int id) {

    }

    @Override
    public void saveFeedback(String message) {

    }

    @Override
    public boolean saveLadderPriorities(LadderPriorities ladderPriorities) {
        return false;
    }

    @Override
    public List<LadderPriorities> getAllLadderPriorities() {
        return null;
    }

    @Override
    public List<String> getMapPool() {
        return null;
    }

    @Override
    public boolean saveLadderMatch(LadderMatchInfo matchInfo) {
        return false;
    }

    @Override
    public boolean clearPriorities(int user1Id) {
        return false;
    }

    @Override
    public List<OngoingLadderMatch> getOngoingLadderMatchesOfUser(int userId) {
        return null;
    }

    @Override
    public OngoingLadderMatch getOngoingMatchById(int id) {
        return null;
    }

    @Override
    public List<OngoingLadderMatch> getOngoingLadderMatches(int maxRows) {
        return null;
    }

    @Override
    public boolean deleteLadderMatch(int ladderMatchId) {
        return false;
    }

    @Override
    public boolean existsLadderMatch(int ladderMatchId) {
        return false;
    }

    @Override
    public LadderPriorities getLadderPriorities(int userId) {
        return null;
    }

    @Override
    public boolean deleteLadderPriorities(int userId) {
        return false;
    }

    @Override
    public List<GladiatorStreak> getCurrentGladiatorStreaks(int maxRows) {
        return null;
    }

    @Override
    public List<GladiatorStreak> getHallOfFame(int maxRows) {
        return null;
    }

    @Override
    public HashMap<Integer, List<String>> getUsedHeroesOfGladiators() {
        return null;
    }

    @Override
    public String getUsername(int userId) {
        return null;
    }

    @Override
    public boolean saveGladiatorChallenge(int userId, int challengedUserId, RankPlacement rankPosition, int priority1, int priority2, int mapVeto, UUID identificator) {
        return false;
    }

    @Override
    public int getMapId(String mapName) {
        return 0;
    }

    @Override
    public GladiatorChallenge getGladiatorChallenge(UUID identificator) {
        return null;
    }

    @Override
    public GladiatorChallenge getGladiatorChallenge(int challengeId) {
        return null;
    }

    @Override
    public boolean deleteGladiatorChallenge(int userId, UUID identificator) {
        return false;
    }

    @Override
    public HashMap<Integer, String> getMapPoolHashMap() {
        return null;
    }

    @Override
    public void putMapToPlayInChallenge(int id, int mapToBePlayed) {

    }

    @Override
    public boolean updateChallengingUserHero(int challengeId, int heroId) {
        return false;
    }

    @Override
    public boolean updateChallengedUserHero(int challengeId, int heroId) {
        return false;
    }

    @Override
    public boolean resetHeroesForChallenge(int challengeId) {
        return false;
    }

    @Override
    public boolean addHeroAsUnavailibleForChallenge(int challengeId, int heroId) {
        return false;
    }

    @Override
    public int getHeroId(String heroName) {
        return 0;
    }

    @Override
    public List<Integer> getUsedHeroOfGladiator(int userId) {
        return null;
    }

    @Override
    public String getHeroName(int heroId) {
        return null;
    }

    @Override
    public List<Integer> getUnavailibleHeroesForChallenge(int challengeId) {
        return null;
    }

    @Override
    public boolean saveLadderGladiatorMatch(GladiatorChallenge challenge) {
        return false;
    }

    @Override
    public String getMapName(int mapToBePlayed) {
        return null;
    }

    @Override
    public boolean clearUnavailibleHeroesForChallenge(int id) {
        return false;
    }

    @Override
    public HashMap<Integer, List<String>> getHallOfFameUseddHeroes() {
        return null;
    }

    @Override
    public boolean gladiatorChallengeOfLadderMatch(int ladderMatchId) {
        return false;
    }

    @Override
    public int getHeroIdOfWinner(int ladderMatchId, int winningUserId) {
        return 0;
    }

    @Override
    public boolean addHeroToUsedHereoesOfUser(int winningUserId, int heroOfWinner) {
        return false;
    }

    @Override
    public boolean clearCurrentlyUsedHeroes(int ladderMatchId) {
        return false;
    }

    @Override
    public boolean clearUsedHeroesOfUser(int losingUserId) {
        return false;
    }

    @Override
    public int increaseStreakAndReturnValue(int winningUserId) {
        return 0;
    }

    @Override
    public int getHallOfFameStreakOfUser(int winningUserId) {
        return 0;
    }

    @Override
    public boolean insertHallOfFameRow(int winningUserId, int currentStreak) {
        return false;
    }

    @Override
    public boolean updateHallOfFameRow(int winningUserId, int currentStreak) {
        return false;
    }

    @Override
    public boolean putUsedHeroesToHallOfFameHeroes(int winningUserId) {
        return false;
    }

    @Override
    public boolean checkIfGladiator(int userId) {
        return false;
    }

    @Override
    public boolean addNewGladiator(int userId) {
        return false;
    }

    @Override
    public boolean clearStreakOfUser(int userId) {
        return false;
    }
}
