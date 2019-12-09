import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class GetLatestGamesServelet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JSONObject objectToSends = new JSONObject();
        JSONArray gamesArray = new JSONArray();
        long timeInMillis = System.currentTimeMillis();
        Date currentDate = new Date(timeInMillis);
        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i<10; i++)
        {
            calendar.add(Calendar.DATE, -1);
            JSONObject gameAndDate = new JSONObject();
            gameAndDate.put("game", "game " + i);
            gameAndDate.put("date", calendar.getTimeInMillis());
            gamesArray.add(gameAndDate);
        }

        objectToSends.put("games", gamesArray);
        resp.getWriter().write(objectToSends.toJSONString());
    }
}
