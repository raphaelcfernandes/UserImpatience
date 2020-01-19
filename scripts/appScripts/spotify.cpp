#include "spotify.hpp"

Spotify::Spotify() {}
Spotify::~Spotify() {}

void Spotify::spotifyScript(std::string device) {
    AdbManager::getInstance()->openAppWithShellMonkey("spotify");
    if (device == "Nexus 5") {
        AdbManager::getInstance()->tap("548 1662", true);
        AdbManager::getInstance()->tap("476 628", true);
        AdbManager::getInstance()->typeWithKeyboard("Ozzy Osbourne Dee", true);
        AdbManager::getInstance()->tap("374 336", true);
        Generic::getInstance()->sleep(50000);
    }
    if (device == "Nexus 6") {
        AdbManager::getInstance()->tap("702 2302", true);
        AdbManager::getInstance()->tap("735 759", true);
        AdbManager::getInstance()->typeWithKeyboard("Ozzy Osbourne Dee", true);
        auto start = std::chrono::high_resolution_clock::now();
        AdbManager::getInstance()->tap("439 391", true);
        auto stop = std::chrono::high_resolution_clock::now();
        Generic::getInstance()->sleep(
            50000 -
            (std::chrono::duration_cast<std::chrono::milliseconds>(stop - start)
                 .count()));
    }
    AdbManager::getInstance()->closeApp(device);
}