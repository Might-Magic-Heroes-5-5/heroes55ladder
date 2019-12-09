package models;

public enum RankPlacement {
    HigherRanked,
    ExactlyRanked,
    LowerRanked,
    CantPlay;

    public static RankPlacement fromInt(int rankPlacement) {
        switch(rankPlacement) {
            case 1:
                return HigherRanked;
            case 2:
                return ExactlyRanked;
            case 3:
                return LowerRanked;
            case 4:
                return CantPlay;
            default:
                return CantPlay;
        }
    }

    public int toInt() {
        switch(this) {
            case HigherRanked:
                return 1;
            case ExactlyRanked:
                return 2;
            case LowerRanked:
                return 3;
            case CantPlay:
                return 4;
            default:
                return 4;
        }
    }
}
