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

public class HelloThea implements MessageCreateListener
{
    /*
     * This is a help command
     *
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event)
    {

        String[] prefix = {"hi", "hello", "hey"};



        // Check if the message content equals "!Help"
        for(int k = 0; k<3; k++)
        {
            if (event.getMessageContent().equalsIgnoreCase( prefix[k] + " thea"))
            {
                if(k==2)
                {
                    event.getChannel().sendMessage("WHAT?!?!");
                    break;
                }
                MessageAuthor author = event.getMessage().getAuthor();
                event.getChannel().sendMessage("Hello <@" + author.getIdAsString() + ">");
                break;
            }
        }
    }
}
