package services;

import config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import java.util.List;

public class DiscordService {
    static JDA jda = null;
    static boolean completedJdaBuil = false;
    private ServletContext config;

    public DiscordService(ServletContext config) {
        this.config = config;
    }

    public void init() throws LoginException, InterruptedException {
        if(jda == null) {
            String token = Config.getDiscordBotToken(config);
            jda = new JDABuilder(token).build();
        }

        if(completedJdaBuil == false) {
            jda.awaitReady();
            completedJdaBuil = true;
        }
    }

    public boolean sendMessageToUser(String userTag, List<String> messageLines) {
        User user = jda.getUserByTag(userTag);
        if(user == null)
            return false;

        PrivateChannel channel = user.openPrivateChannel().complete();
        if(channel == null)
            return false;

        MessageBuilder builder = new MessageBuilder();
        for(String line : messageLines) {
            builder.append(line);
            builder.append('\n');
        }

        Message msg = builder.build();
        channel.sendMessage(msg).queue();

        return true;
    }

    public boolean sendMessageInDefaultChannel(String text) {
        long channelId = Config.getDiscordDefaultChannelId(config);
        return sendMessageInChannel(text, channelId);
    }

    public boolean sendMessageInChannel(String text, long channelId) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if(channel == null)
            return false;

        channel.sendMessage(text).queue();
        return true;
    }

    public User getUserIdByTag(String userDiscordTag) {
        User user = jda.getUserByTag(userDiscordTag);
        return user;
    }
}
