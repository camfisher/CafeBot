//g++ main.cpp -Ilibdiscord/include -ldiscord -lboost_system -lcrypto -lssl -lcpprest -lz -lpthread -std=c++14 -o output_file

#include "Dependencies/sleepy_discord/sleepy_discord.h"
#include "sleepy_discord/websocketpp_websocket.h"

class myClientClass : public SleepyDiscord::DiscordClient {
public:
    using SleepyDiscord::DiscordClient::DiscordClient;
    void onMessage(SleepyDiscord::Message message) {
        if (message.startsWith("whcg hello"))
            sendMessage(message.channelID, "Hello " + message.author.username);
    }
};

int main() {
    myClientClass client("token", 2);
    client.run();
}
