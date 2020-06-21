// Discord Dev Portal:: https://discordapp.com/developers/applications/
// Discord Api Library:: https://javacord.org/wiki/getting-started/intellij-gradle/
// JAVACORD wiki:: https://javacord.org/wiki/getting-started/welcome/
// MSSQL :: https://docs.microsoft.com/en-us/sql/linux/quickstart-install-connect-ubuntu?view=sql-server-ver15

package com.github.camfisher;

import com.github.camfisher.commands.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

public class Main
{
    public static DiscordApi api;

    public static void main(String[] args)
    {
        String token = "";
        try (InputStream input = new FileInputStream("config.cfg"))
        {

            Properties prop = new Properties();

            // Load config.cfg
            prop.load(input);

            /*----------------------------------------------------------------------------------------------------------
            *
            *  Get property values
            *  check if sql is enabled in the config
            *  token: the bots login token
            *
            ----------------------------------------------------------------------------------------------------------*/

            if (!Boolean.parseBoolean(prop.getProperty("bot.SQL"))) //check if sql is disabled in the config
            {
                // Get bot token
                token = prop.getProperty("bot.tkn");

            }
            else
            {

                String connectionUrl =
                    "jdbc:sqlserver://" + prop.getProperty("bot.SQLIp") + ":1433;"
                            + "database=" + prop.getProperty("bot.SQLDB") + ";"
                            + "user=" + prop.getProperty("bot.SQLUser") + ";"
                            + "password=" + prop.getProperty("bot.SQLPass") + ";"
                            + "encrypt=" + prop.getProperty("bot.SQLEncrypt") + ";"
                            + "trustServerCertificate=" + prop.getProperty("bot.SQLTrustServerCert") + ";"
                            + "loginTimeout=" + prop.getProperty("bot.SQLTimeout") + ";";

                try (Connection connection = DriverManager.getConnection(connectionUrl); Statement statement = connection.createStatement();)
                {
                    String SQLselectToken = "SELECT Name From Config WHERE id = 'tkn'";
                    token = String.valueOf(statement.executeQuery(SQLselectToken));
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }

            }

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

        //----------------------------------------------------------------------------------------------------------
        // Command calls go hear
        //----------------------------------------------------------------------------------------------------------
        api.addMessageCreateListener(new HelpCommand());
        api.addMessageCreateListener(new UserInfoCommand());
        api.addMessageCreateListener(new UserTagMeCommand());
        api.addMessageCreateListener(new ShutDownCommand());
        //api.addMessageCreateListener(new RestartCommand());
        api.addMessageCreateListener(new PasserbyRolePurgeCommand());
        api.addMessageCreateListener(new HelloThea());
        api.addMessageCreateListener(new HaltCommand());
        api.addMessageCreateListener(new Introductionmoderation());
        //----------------------------------------------------------------------------------------------------------
        // End Command calls
        //----------------------------------------------------------------------------------------------------------
    }
}