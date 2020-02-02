//g++ main.cpp -Ilibdiscord/include -ldiscord -lboost_system -lcrypto -lssl -lcpprest -lz -lpthread -std=c++14 -o output_file

#include "Dependencies/sleepy_discord/sleepy_discord.h"
#include "sleepy_discord/websocketpp_websocket.h"

class myClientClass : public SleepyDiscord::DiscordClient {
public:
    using SleepyDiscord::DiscordClient::DiscordClient;
    void onMessage(SleepyDiscord::Message message) {
        if (message.startsWith("hello CafeBot"))
            sendMessage(message.channelID, "Hello " + message.author.username);
    }
};

int main() { // DO NOT PUSH TOKEN TO MASTER!!!!!!!!!
    myClientClass client("Token", 2); //Token is the Bots Token and can be found here: https://discordapp.com/developers/applications/
    client.run();
}
