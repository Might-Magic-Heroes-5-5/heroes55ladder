package models;

import javax.servlet.ServletConfig;
import java.util.ServiceLoader;

public class UserRank {
    public String username;
    public int points;

    public UserRank(String username, int points) {
        this.username = username;
        this.points = points;


    }
}
