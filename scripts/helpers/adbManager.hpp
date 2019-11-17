#ifndef ADBMANAGER_H
#define ADBMANAGER_H

#include <iostream>
#include <string>
#include "responseTime.hpp"

class AdbManager {
  ResponseTime responseTime;
 public:
  // Mid button responsable to minimize all apps coordinate
  std::string midButtonAndroid = "734 2540";
  // Main menu coordinate on Nexus 6
  std::string mainMenuCoordinate = "745 2237";
  // Quick search app in the main menu coordinate on Nexus 6
  std::string quickSearchAppCoordinate = "218 228";
  // Coordinate after search for app
  std::string appLocationCoordinate = "236 478";

  AdbManager();
  virtual ~AdbManager();
  void tap(std::string position, bool saveToFile);
  void swipe(std::string from, std::string to, int time, bool saveToFile);
  void typeWithKeyboard(std::string text, bool saveToFile);
  void turnScreenOnAndUnlock();
  void setGovernorInUserImpatienceApp(std::string governor);
  void sleep(int timeInMs);
  void keyevent(int code, bool saveToFile);
  void closeApp();
};
#endif