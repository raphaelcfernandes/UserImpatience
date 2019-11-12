#include <chrono>
#include <ctime>
#include <iostream>
#include <ratio>
#include <string>
// #include "appScripts/chromeScript.hpp"
#include "appScripts/gmail.hpp"
// #include "helpers/adbManager.hpp"
#include "helpers/generic.hpp"
using namespace std;

int main(int argc, char* argv[]) {
  string governor = argv[3];
  string app = argv[2];
  string iteration = argv[4];
  Generic::getInstance()->createFile(governor, app, iteration);

  // auto duration = std::chrono::system_clock::now().time_since_epoch();
  // auto millis =
  //     std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();
  //     cout<<millis<<endl;
  AdbManager adb;
  // if (argc < 3) {
  //   cout << "Number incorrect of parameters" << endl;
  //   return -1;
  // }
  // string cmd = argv[1];
  // if (cmd == "set") {
  //   cout << "Here" << endl;
  //   cout << "C++/ADB setting " << argv[2] << endl;
  //   adb.setGovernorInUserImpatienceApp(argv[2]);
  // } else {
  //   string app = argv[2];
  //   if (cmd == "search") {
  adb.keyevent(3, false);
  adb.tap(adb.mainMenuCoordinate, false);
  adb.tap(adb.quickSearchAppCoordinate, false);
  adb.typeWithKeyboard(app, false);
  //   }
  //   if (cmd == "run") {
  //     std::cout << "C++/ADB going to run " << app << endl;
  adb.tap(adb.appLocationCoordinate, false);
  //     if (app == "gmail") {
  Gmail g;
  g.gmailScript();
  Generic::getInstance()->file.close();
  //     }
  //     if (app == "chrome") {
  //       Chrome chrome;
  //       chrome.chromeScript();
  //     }
  //   }
  // }
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