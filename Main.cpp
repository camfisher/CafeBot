//g++ main.cpp -Ilibdiscord/include -ldiscord -lboost_system -lcrypto -lssl -lcpprest -lz -lpthread -std=c++14 -o output_file

#include "discord.h"

using namespace discord;

int main() { // REMINDER: DO NOT PUSH TOKEN TO MASTER!!!!!
  std::string token = "YOUR_TOKEN"; //Get Bot Token From: https://discordapp.com/developers/applications/
  auto bot = Bot(token); // DON'T DO IT!!

  bot->on_message([](MessageEvent& event) {
    if (event.content() == "Ping!") {
      event.respond("Pong!");
    }
  });

  bot->run();
}
