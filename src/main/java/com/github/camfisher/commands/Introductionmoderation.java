package com.github.camfisher.commands;

        import org.javacord.api.entity.message.MessageAuthor;
        import org.javacord.api.entity.message.embed.EmbedBuilder;
        import org.javacord.api.entity.server.Server;
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
        import java.util.concurrent.ExecutionException;

public class Introductionmoderation implements MessageCreateListener {
    /*
     * This is not a command, it allows Thea to moderate the intro channel
     *
     *
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event)
    {

        String prefix = "!";
        String theaID = "";
        String introID = "";
        String thumbsUp = "";
        try (InputStream input = new FileInputStream("config.cfg"))
        {

            Properties prop = new Properties();

            // Load config.cfg
            prop.load(input);

            // Get property values
            theaID = prop.getProperty("bot.id"); // Thea's user id
            introID = prop.getProperty("bot.intro"); // Introduction channel id
            thumbsUp = prop.getProperty("bot.thumbup");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Server server = event.getServer().get();
        Collection<User> members = server.getMembers();


        if (event.getChannel().getIdAsString().equals(introID)) // only listen to introductions channel
        {
            if (!event.getMessageAuthor().getIdAsString().equals(theaID)) //thea cant respond to herself
            {
                if (!event.getMessageContent().toLowerCase().contains("discord name") || !event.getMessageContent().contains(event.getMessageAuthor().getDiscriminatedName()) || !event.getMessageContent().toLowerCase().contains("preferred name") || !event.getMessageContent().toLowerCase().contains("preferred pronouns") || !event.getMessageContent().toLowerCase().contains("age") || !event.getMessageContent().toLowerCase().contains("social media") || !event.getMessageContent().toLowerCase().contains("other"))
                {
                    event.getMessage().delete();
                    //event.getChannel().sendMessage("Introduction does not match introduction template");
                    //TODO: Make this less janky and not brute force the user list
                    for (User author : members)
                    {
                        if (author.getIdAsString().equals(event.getMessageAuthor().getIdAsString()))
                        {
                            try {
                                EmbedBuilder embed = new EmbedBuilder()
                                        .setTitle("")
                                        .addField("Your Message", event.getMessageContent(), true)
                                        .setAuthor(author);
                                author.openPrivateChannel().get().sendMessage("Your Introduction does not follow the template \n \n For an example of the template please view the pinned message in <#638079574307110952>");
                                author.openPrivateChannel().get().sendMessage(embed);
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if (event.getMessageContent().toLowerCase().contains("discord name") && event.getMessageContent().contains(event.getMessageAuthor().getDiscriminatedName()) && event.getMessageContent().toLowerCase().contains("preferred name") && event.getMessageContent().toLowerCase().contains("preferred pronouns") && event.getMessageContent().toLowerCase().contains("age") && event.getMessageContent().toLowerCase().contains("social media") && event.getMessageContent().toLowerCase().contains("other"))
                {
                    event.getMessage().addReaction(thumbsUp);
                    //TODO: make thea give role to user
                }
            }
        }
    }
}
