#include <iostream>
#include <string>
#include <vector>
#include "appScripts/chromeScript.hpp"
#include "appScripts/gmail.hpp"
#include "helpers/adbManager.hpp"

using namespace std;

int main(int argc, char *argv[]) {
  AdbManager adb;
  if (argc < 3) {
    cout << "Number incorrect of parameters" << endl;
    return -1;
  }
  string cmd = argv[1];
  if (cmd == "set") {
    cout << "Here" << endl;
    cout << "C++/ADB setting " << argv[2] << endl;
    adb.setGovernorInUserImpatienceApp(argv[2]);
  } else {
    string app = argv[2];
    if (cmd == "search") {
      adb.keyevent(3);
      adb.tap(adb.mainMenuCoordinate);
      adb.tap(adb.quickSearchAppCoordinate);
      adb.typeWithKeyboard(app);
    }
    if (cmd == "run") {
      std::cout << "C++/ADB going to run " << app << endl;
      adb.tap(adb.appLocationCoordinate);
      if (app == "gmail") {
        Gmail g;
        g.gmailScript();
      }
      if (app == "chrome") {
        Chrome chrome;
        chrome.chromeScript();
      }
    }
  }
  // vector<string> apps = {"gmail", "youtube", "spotify", "chrome"};
  // vector<string> governors = {"conservative", "powersave", "interactive",
  // "performance",  "ondemand",  "userspace"};
  // Read interval that userspace background thread will read TA from top in
  // ms vector<int> timeToreadTA = {250, 500, 1000}; Time to decrease cpu
  // frequency in ms this is the parameter A vector<int> decreaseCPUInterval =
  // {1000, 2000, 4000, 8000};
  //(Max - current)/C
  //#Where C is the following measurements
  // vector<string> marginToIncreaseCpuFrequency = {"1/2", "1/4", "1/8"};
  // 0 is the user that does not complain at all
  // 5 is the user that complains with high frequency
  // vector<int> userImpatienceLevel = {0, 1, 2, 3, 4, 5};
  return 0;
}