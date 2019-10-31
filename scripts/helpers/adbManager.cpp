#include "adbManager.hpp"

AdbManager::AdbManager(){};
AdbManager::~AdbManager(){};

void AdbManager::tap(std::string position) {
  std::string cmd = "adb shell input tap " + position;
  popen(cmd.c_str(), "r");
  sleep(1000);
}

void AdbManager::swipe(std::string from, std::string to, int time) {
  std::string cmd = "";
  if (time != 0) {
    cmd =
        "adb shell input swipe " + from + " " + to + " " + std::to_string(time);
  } else {
    cmd = "adb shell input swipe " + from + " " + to;
  }
  std::cout << cmd << std::endl;
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
    sleep(500);
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
  tap(mainMenuCoordinate);
  tap(quickSearchAppCoordinate);
  typeWithKeyboard("battery");
  tap(appLocationCoordinate);
  // Deactivate button
  tap("1045 570");
  // Config tab
  tap("1222 345");
  // Dropdown menu
  tap("423 528");
  if (governor.compare("conservative") == 0) {
    tap("271 755");
  } else if (governor.compare("powersave") == 0) {
    tap("186 1249");
  } else if (governor.compare("interactive") == 0) {
    tap("134 913");
  } else if (governor.compare("performance") == 0) {
    tap("147 1417");
  } else if (governor.compare("ondemand") == 0) {
    tap("203 1090");
  } else {  // userspace
    tap("185 1605");
  }
  // Save button
  tap("140 652");
  // Main menu
  tap("124 362");
  // Activate
  tap("479 602");
}

void AdbManager::keyevent(int code) {
  std::string cmd = "adb shell input keyevent " + std::to_string(code);
  popen(cmd.c_str(), "r");
  sleep(1000);
}