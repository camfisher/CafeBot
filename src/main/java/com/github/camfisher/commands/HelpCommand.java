package com.github.camfisher.commands;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelpCommand implements MessageCreateListener
{
    /*
     * This is a help command
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



        // Check if the message content equals "!Help"
        if (event.getMessageContent().toLowerCase().contains( prefix + "bothelp") || event.getMessageContent().toLowerCase().contains("thea " + "bothelp"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            event.getChannel().sendMessage("<@" + author.getIdAsString() + ">");
            EmbedBuilder embed = new EmbedBuilder() // create the embeded help message
                    .setTitle("Bot Command Help")
                    .addField("Current Bot Prefix", "Thea \n" + prefix, true)
                    .addField("Active Commands", "Help \n BotHelp \n Hello" , false)
                    .addField("Active Features", "Introductions Assisted Moderation", false)
                    .setAuthor(author);
            event.getChannel().sendMessage(embed)
                    .exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
        }
        else if (event.getMessageContent().toLowerCase().contains("thea " + "help"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            event.getChannel().sendMessage("<@" + author.getIdAsString() + ">");
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("User Help")
                    .addField("If you need help from staff, please view the", "<#648312790179905559>", true)
                    .setAuthor(author);
            event.getChannel().sendMessage(embed)
                    .exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
        }
    }
}
