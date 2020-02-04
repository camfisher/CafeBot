package com.github.camfisher.commands;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



import com.github.camfisher.Main;

public class RestartCommand implements MessageCreateListener {
    /*
     * This is a test command to tag users
     *
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event)
    {

        String prefix = "!";
        try (InputStream input = new FileInputStream("config.cfg"))
        {

            Properties prop = new Properties();

            // Load config.cfg
            prop.load(input);

            // Get property values
            //----------------------------------------------------------------------------------------------------------
            // Get bot prefix
            if (prop.getProperty("bot.pfx").length() > 0)
            {
                prefix = prop.getProperty("bot.pfx");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Check if the message content equals "!userInfo"
        if (event.getMessageContent().equalsIgnoreCase(prefix + "Restart"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            event.getChannel().sendMessage("<@" + author.getIdAsString() + ">" + " :: Restarting!");
            Main.main(null);
        }
    }
}
