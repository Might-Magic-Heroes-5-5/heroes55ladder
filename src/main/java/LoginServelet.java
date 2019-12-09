import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.json.simple.JSONObject;
import services.LoggingService;

public class LoginServelet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("good", "yes");
        resp.getWriter().write(jsonResponse.toJSONString());
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String command = req.getParameter("command");
        JSONObject jsonResponse;
        LoggingService logService = new LoggingService(req, getServletContext());

        switch(command)
        {
            case "login":
                jsonResponse = tryLogin(logService);
                break;
            case "checkLogin":
                jsonResponse = checkLogin(logService);
                break;
            case "logout":
                jsonResponse = logout(logService);
                break;
            case "signUp":
                jsonResponse = signUp(logService);
                break;
            case "confirm":
                jsonResponse = confirmEmail(logService);
                break;
            default:
                jsonResponse = new JSONObject();
        }

        resp.getWriter().write(jsonResponse.toJSONString());
    }

    private JSONObject confirmEmail(LoggingService logService) {
        JSONObject outputJson = new JSONObject();

        boolean success = logService.confirmSignup();
        outputJson.put("success", success);

        return outputJson;
    }

    private JSONObject logout(LoggingService logService) {
        JSONObject outputJson = new JSONObject();

        logService.logOut();
        outputJson.put("loggedIn", false);

        return outputJson;
    }

    private JSONObject checkLogin(LoggingService logService) {
        JSONObject outputJson = new JSONObject();

        LoggingService.LoginResponse logResponse = logService.checkLogin();
        outputJson.put("loggedIn", logResponse.isLoggedIn);
        outputJson.put("username", logResponse.username);
        outputJson.put("userId", logResponse.userId);
        outputJson.put("isGladiator", logResponse.isGladiator);

        return outputJson;
    }

    private JSONObject tryLogin(LoggingService logService) {
        JSONObject outputJson = new JSONObject();

        LoggingService.LoginResponse logResponse = logService.tryLogin();
        outputJson.put("loggedIn", logResponse.isLoggedIn);
        outputJson.put("username", logResponse.username);

        return outputJson;
    }

    private JSONObject signUp(LoggingService logService) {
        JSONObject outputJson = new JSONObject();

        LoggingService.SignUpResponse response = logService.signUp();
        outputJson.put("success", response.success);
        outputJson.put("errorMessage", response.errorMessage);
        return outputJson;
    }
}
