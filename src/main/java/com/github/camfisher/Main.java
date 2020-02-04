// Discord Dev Portal:: https://discordapp.com/developers/applications/
// Discord Api Library:: https://javacord.org/wiki/getting-started/intellij-gradle/
// JAVACORD wiki:: https://javacord.org/wiki/getting-started/welcome/


package com.github.camfisher;

import com.github.camfisher.commands.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main
{

    public static void main(String[] args)
    {

        String token = "";
        try (InputStream input = new FileInputStream("config.cfg"))
        {

            Properties prop = new Properties();

            // Load config.cfg
            prop.load(input);

            // Get property values
            //----------------------------------------------------------------------------------------------------------
            // Get bot token
            token = prop.getProperty("bot.tkn");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Enable debugging, if no slf4j logger was found
        FallbackLoggerConfiguration.setDebug(true);



        if (token.length() < 1)
        {
            System.err.println("Please provide a valid token as the first argument!" + token.length());
            return;
        }

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();


        // Add listeners :: api.addMessageCreateListener(new 'command');
        api.addMessageCreateListener(new HelpCommand());
        api.addMessageCreateListener(new UserInfoCommand());
        api.addMessageCreateListener(new UserTagMeCommand());
        api.addMessageCreateListener(new ShutDownCommand());
        //api.addMessageCreateListener(new RestartCommand());
        api.addMessageCreateListener(new PasserbyRolePurgeCommand());

    }

}