package models;

public class LadderMatchInfo {
    public int user1Id;
    public int user2Id;
    public String mapPlayed;

    public LadderMatchInfo(int user1Id, int user2Id, String mapPlayed) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.mapPlayed = mapPlayed;
    }
}
