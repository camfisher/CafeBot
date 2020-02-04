package com.github.camfisher.commands;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PasserbyRolePurgeCommand implements MessageCreateListener {

    /*
     * Command to purge the Passerby role from users if they have a functional role that can replace it
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

        if (event.getMessageContent().equalsIgnoreCase(prefix + "PurgePssrby"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            if (author.isServerAdmin()) // Check if user invoking command is an admin
            {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Role Info")
                        .addField("Display Name", author.getDisplayName(), true)
                        .setAuthor(author);
            }
            else
            {
                event.getChannel().sendMessage("<@" + author.getIdAsString() + ">" + " tried to use a admin only command.");
            }
        }
    }
}
