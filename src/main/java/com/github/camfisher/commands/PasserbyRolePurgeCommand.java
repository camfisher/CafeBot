package com.github.camfisher.commands;

import org.javacord.api.DiscordApi;
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

        if (event.getMessageContent().equalsIgnoreCase(prefix + "PurgePasserby"))
        {
            MessageAuthor author = event.getMessage().getAuthor();
            Server server = event.getServer().get();
            Collection<User> members = server.getMembers();
            if (author.isServerAdmin()) // Check if user invoking command is an admin
            {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Passerby Role Purge");
                for (User u : members)
                {
                    boolean otherroles = false;
                    /*for(Role r : u.getApi().getRoles())
                    {
                        embed.addField("User: ", u.getDisplayName(server), true);
                        if (r == server.get)
                        {
                            embed.addField("Role: ", String.valueOf(r), true);
                            server.removeRoleFromUser(u,r);
                        }
                    }*/


                    for (Role r : u.getRoles(server))
                    {
                        if (String.valueOf(r.getName()).contains("Passerby"))
                        {
                            for (Role j : u.getRoles(server))
                            {
                                if (String.valueOf(j.getName()).contains("Customers") || String.valueOf(j.getName()).contains("Vendor"))
                                {
                                    embed.addField(u.getDisplayName(server) + " Role Purged", "Had Passerby and Customers/Vendor Role", true);
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
