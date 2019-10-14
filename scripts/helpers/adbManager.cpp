#include "adbManager.hpp"

AdbManager::AdbManager(){};
AdbManager::~AdbManager(){};

void AdbManager::touchScreenPosition(std::string position) {
  std::string cmd = "adb shell input tap " + position;
  popen(cmd.c_str(), "r");
  sleep(1000);
}

void AdbManager::swipeScreen(std::string from, std::string to, int time) {
  std::string cmd =
      "adb shell input swipe " + from + " " + to + " " + std::to_string(time);
  popen(cmd.c_str(), "r");
  sleep(1000);
}

void AdbManager::sleep(int timeInMs) {
  std::this_thread::sleep_for(std::chrono::milliseconds(timeInMs));
}

void AdbManager::typeWithKeyboard(std::string text) {
  std::string cmd = "";
  for (char const &c : text) {
    if (isspace(c)) {
      cmd = "adb shell input keyevent 62";
    } else {
      cmd = "adb shell input text ";
      cmd += c;
    }
    popen(cmd.c_str(), "r");
    sleep(400);
  }
}

void AdbManager::turnScreenOnAndUnlock() {
  std::string cmd = "adb shell input keyevent 26";
  popen(cmd.c_str(), "r");
  sleep(1000);
  cmd = "adb shell input keyevent 82";
  popen(cmd.c_str(), "r");
}

void AdbManager::setGovernorInUserImpatienceApp(std::string governor) {
  std::string cmd = "adb shell input keyevent 3";
  popen(cmd.c_str(), "r");
  sleep(1000);
  touchScreenPosition(mainMenuCoordinate);
  touchScreenPosition(quickSearchAppCoordinate);
  typeWithKeyboard("battery");
  touchScreenPosition(appLocationCoordinate);
  // Deactivate button
  touchScreenPosition("1045 570");
  // Config tab
  touchScreenPosition("1222 345");
  // Dropdown menu
  touchScreenPosition("423 528");
  if (governor.compare("conservative") == 0) {
    touchScreenPosition("271 755");
  } else if (governor.compare("powersave") == 0) {
    touchScreenPosition("186 1249");
  } else if (governor.compare("interactive") == 0) {
    touchScreenPosition("134 913");
  } else if (governor.compare("performance") == 0) {
    touchScreenPosition("147 1417");
  } else if (governor.compare("ondemand") == 0) {
    touchScreenPosition("203 1090");
  } else {  // userspace
    touchScreenPosition("185 1605");
  }
  // Save button
  touchScreenPosition("140 652");
  // Main menu
  touchScreenPosition("124 362");
  // Activate
  touchScreenPosition("479 602");
}

void AdbManager::inputKeyEvent(int code) {
  std::string cmd = "adb shell input keyevent " + std::to_string(code);
  popen(cmd.c_str(), "r");
  sleep(1000);
}