package models;

public enum DifficultySetting {
    NORMAL, HARD, HEROIC, IMPOSSIBLE;

    public static DifficultySetting parseString(String difficultyName) {
        difficultyName = difficultyName.toUpperCase();

        if(difficultyName.compareToIgnoreCase("normal") == 0)
            return NORMAL;
        if(difficultyName.compareToIgnoreCase("hard") == 0)
            return HARD;
        if(difficultyName.compareToIgnoreCase("heroic") == 0)
            return HEROIC;
        if(difficultyName.compareToIgnoreCase("impossible") == 0)
            return IMPOSSIBLE;

        return NORMAL;
    }

    public static DifficultySetting fromInt(int difficulty) {
        switch(difficulty) {
            case 1:
                return NORMAL;
            case 2:
                return HARD;
            case 3:
                return HEROIC;
            case 4:
                return IMPOSSIBLE;
            default:
                return NORMAL;
        }
    }

    public int toInt() {
        switch(this) {
            case NORMAL:
                return 1;
            case HARD:
                return 2;
            case HEROIC:
                return 3;
            case IMPOSSIBLE:
                return 4;
            default:
                return -1;
        }
    }
}
