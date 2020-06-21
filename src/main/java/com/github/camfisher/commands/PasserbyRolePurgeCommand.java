package com.github.camfisher.commands;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.*;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

public class PasserbyRolePurgeCommand implements MessageCreateListener
{

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

        if (event.getMessageContent().equalsIgnoreCase(prefix + "PurgeUnverified") || event.getMessageContent().equalsIgnoreCase("thea " + "PurgeUnverified"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            Server server = event.getServer().get();
            Collection<User> members = server.getMembers();
            if (author.isServerAdmin()) // Check if user invoking command is an admin
            {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Here is the list of users affected by Unverified Role Purge");
                for (User u : members)
                {
                    boolean otherroles = false;

                    for (Role r : u.getRoles(server))
                    {
                        if (String.valueOf(r.getName()).contains("Unverified"))
                        {
                            for (Role j : u.getRoles(server))
                            {
                                if (String.valueOf(j.getName()).contains("Verified"))
                                {
                                    embed.addField(u.getDisplayName(server) + " Role Purged", "Had Unverified and Verified Role", true);
                                    server.removeRoleFromUser(u,r);
                                    break;
                                }
                            }
                        }
                    }
                }

                        embed.setAuthor(author);
                event.getChannel().sendMessage(embed)
                        .exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
            }
            else
            {
                event.getChannel().sendMessage("<@" + author.getIdAsString() + ">" + " tried to use a admin only command.");
            }
        }
    }
}
