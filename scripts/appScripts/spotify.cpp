#include "spotify.hpp"

Spotify::Spotify() {}
Spotify::~Spotify() {}

void Spotify::spotifyScript(std::string device) {
    AdbManager adb(device);
    if(device == "Nexus 5") {
      adb.tap("548 1662", true);
        adb.tap("476 628", true);
        adb.typeWithKeyboard("Ozzy Osbourne Dee", true);
        adb.tap("374 336", true);
        Generic::getInstance()->sleep(50000);
    }
    if (device == "Nexus 6") {
        adb.tap("702 2302", true);
        adb.tap("735 759", true);
        adb.typeWithKeyboard("Ozzy Osbourne Dee", true);
        adb.tap("439 391", true);
        Generic::getInstance()->sleep(50000);
    }
    adb.closeApp(device);
}