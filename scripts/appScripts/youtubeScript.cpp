#include "youtubeScript.hpp"

Youtube::Youtube() {}
Youtube::~Youtube(){};

void Youtube::youtubeScript() {
  std::cout << "Going to execute youtube script" << std::endl;
  AdbManager adb;
  // Search Button
  adb.tap("1182 167");
  adb.typeWithKeyboard(
      "a night to remember launch cinematic - the witcher III: wild hunt");

  // Press enter to search
  adb.keyevent(66);

  adb.tap("310 512");
  adb.swipe("935 491", "1094 1731", 0);

  adb.tap("1324 2090");
}