#include "spotify.hpp"

Spotify::Spotify() {}
Spotify::~Spotify() {}

void Spotify::spotifyScript() {
  AdbManager adb;
  adb.tap("702 2302", true);
  adb.tap("735 759", true);
  adb.typeWithKeyboard("Ozzy Osbourne Dee", true);
  adb.tap("439 391", true);
  Generic::getInstance()->sleep(50000);
  adb.closeApp();
}