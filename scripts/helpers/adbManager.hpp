#ifndef ADBMANAGER_H
#define ADBMANAGER_H
#include <chrono>
#include <iostream>
#include <string>
#include <thread>

class AdbManager {
  // Mid button responsable to minimize all apps coordinate
  std::string midButtonAndroid = "734 2540";
  // Main menu coordinate on Nexus 6
  std::string mainMenuCoordinate = "745 2237";
  // Quick search app in the main menu coordinate on Nexus 6
  std::string quickSearchAppCoordinate = "218 228";
  // Coordinate after search for app
  std::string appLocationCoordinate = "236 478";

 public:
  AdbManager();
  virtual ~AdbManager();
  void touchScreenPosition(std::string position);
  void swipeScreen(std::string from, std::string to, int time);
  void typeWithKeyboard(std::string text);
  void turnScreenOnAndUnlock();
  void setGovernorInUserImpatienceApp(std::string governor);
  void sleep(int timeInMs);
};
#endif