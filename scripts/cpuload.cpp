#include <iostream>
#include <string>
#include <vector>
#include "appScripts/chromeScript.hpp"
#include "appScripts/gmail.hpp"
#include "helpers/adbManager.hpp"

using namespace std;

int main(int argc, char *argv[]) {
  int i=0;
  while(true) {
    i+=1;
    std::cout<<i<<std::endl;
  }
  // AdbManager adb;
  // if (argc < 3) {
  //   cout << "Number incorrect of parameters" << endl;
  //   return -1;
  // }
  // string cmd = argv[1];
  // if (cmd == "set") {
  //   cout << "C++/ADB setting " << argv[2] << endl;
  //   adb.setGovernorInUserImpatienceApp(argv[2]);
  // } else if (cmd == "run") {
  //   string app = argv[2];
  //   std::cout << "C++/ADB going to run " << app << endl;

  //   if (app == "gmail") {
  //     for (int i = 0; i < 3; i++) {
  //       Gmail g;
  //       adb.keyevent(3);
  //       adb.tap(adb.mainMenuCoordinate);
  //       adb.tap(adb.quickSearchAppCoordinate);
  //       adb.typeWithKeyboard("gmail");
  //       adb.tap(adb.appLocationCoordinate);
  //       g.gmailScript();
  //     }
  //   }
  //   if (app == "chrome") {
  //     Chrome chrome;
  //     adb.tap(adb.appLocationCoordinate);
  //     chrome.chromeScript();
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