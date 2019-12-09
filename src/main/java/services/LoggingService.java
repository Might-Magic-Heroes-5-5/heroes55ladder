package services;

import config.Config;
import repo.IRepo;
import repo.Repo;
import services.DiscordService;
import services.SendEmailService;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LoggingService {
    HttpServletRequest request;
    ServletContext context;
    String siteUrl;
    IRepo repo;

    public class LoginResponse {
        public boolean isLoggedIn;
        public String username;
        public int userId;
        public boolean isGladiator;

        public LoginResponse(boolean isLoggedIn, String username, int userId, boolean isGladiator) {
            this.isLoggedIn = isLoggedIn;
            this.username = username;
            this.userId = userId;
            this.isGladiator = isGladiator;
        }
    }

    public class SignUpResponse {
        public boolean success;
        public String errorMessage;

        public SignUpResponse(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }

    public LoggingService(HttpServletRequest request, ServletContext context) {
        this.request = request;
        this.context = context;
        siteUrl = Config.getSiteUrl(context);
        repo = Repo.getRepo(context);
    }

    public void logOut() {
        HttpSession session = request.getSession(false);
        if(session != null)
            session.invalidate();
    }

    public LoginResponse checkLogin() {
        boolean isLoggedIn;
        String username = "null";
        int userId = -1;
        boolean isGladiator = false;

        HttpSession session = request.getSession(false);
        if(session == null)
            isLoggedIn = false;
        else
        {
            Object isLoggedInObj = session.getAttribute("isLoggedIn");
            if(isLoggedInObj == null)
                isLoggedIn = false;
            else
            {
                isLoggedIn = (boolean)isLoggedInObj;
                if(isLoggedIn) {
                    username = (String)session.getAttribute("username");
                    userId = (Integer)session.getAttribute("userId");
                    isGladiator = repo.checkIfGladiator(userId);
                }

            }
        }

        LoginResponse output = new LoginResponse(isLoggedIn, username, userId, isGladiator);
        return output;
    }

    public LoginResponse tryLogin() {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int userId = -1;
        boolean isGladiator = false;

        boolean successedLogin = repo.checkUsernamePassword(username, hashPassword(password));

        if(successedLogin) {
            HttpSession session = request.getSession(true);
            userId = repo.getUserId(username);
            isGladiator = repo.checkIfGladiator(userId);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            session.setAttribute("userId", userId);
        }

        LoginResponse output = new LoginResponse(successedLogin, username, userId, isGladiator);
        return output;
    }

    public LoginResponse loginWithUsernamePassword(String username, String password) {
        boolean successedLogin = repo.checkUsernamePassword(username, hashPassword(password));
        int userId = -1;
        boolean isGladiator = false;

        if(successedLogin) {
            HttpSession session = request.getSession(true);
            userId = repo.getUserId(username);
            isGladiator = repo.checkIfGladiator(userId);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            session.setAttribute("userId", userId);
        }

        LoginResponse output = new LoginResponse(successedLogin, username, userId, isGladiator);
        return output;
    }

    public boolean confirmSignup() {
        String username = request.getParameter("username");
        String identificatorStr = request.getParameter("identificator");

        UUID identificator = UUID.fromString(identificatorStr);
        boolean success = repo.confirmUser(username, identificator);
        if(success) {
            HttpSession session = request.getSession(true);
            int userId = repo.getUserId(username);
            repo.setInitialEloForUser(userId);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", username);
            session.setAttribute("userId", userId);
        }

        return success;
    }

    public SignUpResponse signUp() {
        boolean success = false;
        String errorMessage = "";
        String discordTag = request.getParameter("discord");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(repo.existsUsername(username) == false) {
            if(repo.existsDiscord(discordTag) == false) {
                UUID identificator = UUID.randomUUID();
                success = repo.addNewUser(username, hashPassword(password), discordTag, identificator);
                if(success)
                    success = sendConfirmationDiscordMessage(username, discordTag, identificator);//success = sendConfirmationMail(username, email, identificator);

                if(success == false) {
                    repo.removeUser(username);
                    errorMessage = "the user wasn't found on our server in discord, please join our server first";
                }
            }
            else
                errorMessage = "this discord tag is already used by some other user";
        }
        else
            errorMessage = "this username already exists, please pick some other username";

        return new SignUpResponse(success, errorMessage);
    }

    boolean sendConfirmationDiscordMessage(String username, String discordTag, UUID identificator) {
        boolean success = false;

        try {
            DiscordService discord = new DiscordService(context);
            discord.init();
            List<String> messageLines = new ArrayList<String>();
            messageLines.add("Click on the link below to confirm your account on heroes 5.5 ladder:");
            messageLines.add(siteUrl + "/confirmPage.jsp?username=" + username + "&iden=" + identificator.toString());

            success = discord.sendMessageToUser(discordTag, messageLines);
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    private boolean sendConfirmationMail(String username, String email, UUID identificator) {
        SendEmailService emailService = new SendEmailService(context);
        emailService.Init(context);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        boolean success;

        try {
            String body = "<p>Click on the link below to confirm your account</p><br/>" +
                    "<a href=\"" + siteUrl + "/confirmPage.jsp?username=" + username + "&iden=" + identificator.toString() +
                    "\">click here</a>";
            mimeBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            success = emailService.SendMail(multipart, "Confirm your account", email);
        } catch(Exception ex) {
            success = false;
        }

        return success;
    }

    private long hashPassword(String password) {
        return password.hashCode(); //TO-DO improve hashing
    }
}
