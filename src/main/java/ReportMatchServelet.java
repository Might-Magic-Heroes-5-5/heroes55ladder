import models.DifficultySetting;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.LoggingService;
import services.ReportMatchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class ReportMatchServelet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject response = new JSONObject();
        boolean success = true;
        LoggingService logService = new LoggingService(req, getServletContext());

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String json = "";
        if(br != null)
            json = br.readLine();
        JSONParser parser = new JSONParser();
        JSONObject input = null;
        try {
            input = (JSONObject)parser.parse(json);
        } catch (ParseException e) {
            response.put("success", false);
            response.put("errorMsg", "couldn't parse json");
            resp.getWriter().write(response.toJSONString());
            return;
        }

        String command = (String)input.get("command");
        switch(command) {
            case "matchReport":
                response = reportMatch(input, logService);
                break;
            case "getReportAndIsLoggedByIden":
                response = getReportAndCheckIfLoggedByIdentifier(input, logService);
                break;
            case "loginByIdenAndPassword":
                response = loginByIdentifierAndPassword(input, logService);
                break;
            case "confirmReport":
                response = confirmReport(input, logService);
                break;
            case "declineReport":
                response = declineReport(input, logService);
                break;
        }

        resp.getWriter().write(response.toJSONString());
    }

    private JSONObject loginByIdentifierAndPassword(JSONObject input, LoggingService logService) {
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
        ReportMatchService.MatchReport report = reportService.getMatchReport(identificator);

        if(report == null) {
            output.put("success", false);
            output.put("errorMsg", "No such match was found, perhaps it was already confirmed or declined");
            return output;
        }

        String password = (String)input.get("password");
        LoggingService.LoginResponse response = logService.loginWithUsernamePassword(report.oppositeUsername, password);

        if(response.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "Bad password");
            return output;
        }

        output.put("success", true);
        output.put("reportingUsername", report.reportingUsername);
        return output;
    }

    private JSONObject declineReport(JSONObject input, LoggingService logService) {
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
        ReportMatchService.MatchReport report = reportService.getMatchReport(identificator);

        if(report == null) {
            output.put("success", false);
            output.put("errorMsg", "No such match was found, perhaps it was already confirmed or declined");
            return output;
        }

        if(logResponse.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "You are not logged in!");
            return output;
        }

        if(report.oppositeUsername.compareTo(logResponse.username) != 0) {
            output.put("success", false);
            output.put("errorMsg", "You are not the user this report talks about");
            return output;
        }

        if(reportService.declineReport(identificator) == false) {
            output.put("success", false);
            output.put("errorMsg", "An error has occured, please try again later");
            return output;
        }
        else {
            output.put("success", true);
            output.put("successMsg", "Thank you for declining the match result.");
            return output;
        }
    }

    private JSONObject confirmReport(JSONObject input, LoggingService logService) {
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
        ReportMatchService.MatchReport report = reportService.getMatchReport(identificator);

        if(report == null) {
            output.put("success", false);
            output.put("errorMsg", "No such match was found, perhaps it was already confirmed or declined");
            return output;
        }

        if(logResponse.isLoggedIn == false) {
            output.put("success", false);
            output.put("errorMsg", "You are not logged in!");
            return output;
        }

        if(report.oppositeUsername.compareTo(logResponse.username) != 0) {
            output.put("success", false);
            output.put("errorMsg", "You are not the user this report talks about");
            return output;
        }

        if(reportService.confirmReport(identificator) == false) {
            output.put("success", false);
            output.put("errorMsg", "An error has occured, please try again later");
            return output;
        }
        else {
            output.put("success", true);
            output.put("successMsg", "Thank you for confirming the match, good luck in the following ones");
            return output;
        }
    }

    private JSONObject getReportAndCheckIfLoggedByIdentifier(JSONObject input, LoggingService logService) {
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
        ReportMatchService.MatchReport report = reportService.getMatchReport(identificator);
        if(report == null) {
            output.put("success", false);
            output.put("errorMsg", "The report wasn't found, perhaps it was already declined or confirmed");
            return output;
        }

        if(logResponse.isLoggedIn) {
            if(report.oppositeUsername.compareTo(logResponse.username) != 0) {
                logService.logOut();
                output.put("success", true);
                output.put("isLoggedIn", false);
                output.put("reportingUsername", report.reportingUsername);
                return output;
            }
            else {
                output.put("success", true);
                output.put("isLoggedIn", true);
                output.put("reportingUsername", report.reportingUsername);
                return output;
            }
        }
        else {
            output.put("success", true);
            output.put("isLoggedIn", false);
            output.put("reportingUsername", report.reportingUsername);
            return output;
        }
    }

    private JSONObject reportMatch(JSONObject input, LoggingService logService) {
        boolean success;
        String successMessage="", errorMessage;
        JSONObject output = new JSONObject();

        ReportMatchService reportService = new ReportMatchService(getServletContext());
        LoggingService.LoginResponse logResponse = logService.checkLogin();
        if(logResponse.isLoggedIn) {
            String opponentUsername = (String)input.get("opponentUsername");
            String resultStr = (String)input.get("result");
            boolean didWin = false;
            if(resultStr.compareToIgnoreCase("won") == 0)
                didWin = true;
            DifficultySetting difficutlyLevel = DifficultySetting.parseString((String)input.get("difficulty"));
            String mapName = (String)input.get("mapName");
            ReportMatchService.MatchReport matchInfo = new ReportMatchService.MatchReport(logResponse.username, logResponse.userId, opponentUsername, didWin, difficutlyLevel, mapName);
            ReportMatchService.ReportMatchResponse reportResponse = reportService.reportMatch(matchInfo);
            success = reportResponse.success;
            successMessage = reportResponse.successMsg;
            errorMessage = reportResponse.errorMsg;
        }
        else {
            success = false;
            errorMessage = "You are not logged in!";
        }

        output.put("success", success);
        output.put("errorMsg", errorMessage);
        output.put("successMsg", successMessage);
        return output;
    }
}
