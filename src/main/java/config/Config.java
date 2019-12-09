package config;

import javax.servlet.ServletContext;

public class Config {

    public static boolean isLocal(ServletContext config) {
        String isLocalParam = config.getInitParameter("isLocal");
        if(isLocalParam.compareTo("true") == 0)
            return true;
        else
            return false;
    }

    public static String getConnectionString(ServletContext config) {
        String connStr = config.getInitParameter("connectionString");
        return connStr;
    }

    public static String getSMTPhost(ServletContext config) {
        String host = config.getInitParameter("SMTPhost");
        return host;
    }

    public static int getSMTPhostPort(ServletContext config) {
        String PortStr = config.getInitParameter("SMTPhostPort");
        int Port = Integer.parseInt(PortStr);
        return Port;
    }

    public static String getSMTPusername(ServletContext config) {
        String username = config.getInitParameter("SMTPusername");
        return username;
    }

    public static String getSMTPpassword(ServletContext config) {
        String password = config.getInitParameter("SMTPpassword");
        return password;
    }

    public static String getSentFromMail(ServletContext config) {
        String sentFromMail = config.getInitParameter("sentFromMail");
        return sentFromMail;
    }

    public static String getSiteUrl(ServletContext config) {
        String siteUrl = config.getInitParameter("siteUrl");
        return siteUrl;
    }

    public static String getDiscordBotToken(ServletContext config) {
        String token = config.getInitParameter("DiscordBotToken");
        return token;
    }

    public static long getDiscordDefaultChannelId(ServletContext config) {
        String channelIdStr =  config.getInitParameter("DiscordDefaultChannelId");
        Long channelId = Long.parseLong(channelIdStr);
        return channelId;
    }

    public static String getServerIp(ServletContext config) {
        String ServerIp = config.getInitParameter("ServerIp");
        return ServerIp;
    }

    public static String getDbUsername(ServletContext config) {
        String username = config.getInitParameter("DatabaseUsername");
        return username;
    }

    public static String getDbPassword(ServletContext config) {
        String password = config.getInitParameter("DatabasePassword");
        return password;
    }
}
