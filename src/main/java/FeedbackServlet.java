import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import repo.IRepo;
import repo.Repo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String json = "";
        if(br != null)
            json = br.readLine();
        JSONParser parser = new JSONParser();
        JSONObject input = null, response = new JSONObject();
        try {
            input = (JSONObject)parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("errorMsg", "we couldn't parse the input");
            resp.getWriter().write(response.toJSONString());
            return;
        }

        IRepo repo = Repo.getRepo(getServletContext());
        repo.saveFeedback((String)input.get("message"));

        response.put("success", true);
        resp.getWriter().write(response.toJSONString());
    }
}