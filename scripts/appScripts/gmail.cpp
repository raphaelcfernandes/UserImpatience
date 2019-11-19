#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript() {
  AdbManager adb;
  // opens sideBar menu
  adb.tap("158 212", true);
  // Clicks on All Mail
  adb.tap("395 2040", true);
  // clicks on the first email and 'reads' it
  adb.tap("742 595", true);
  // Scroll down screen
  adb.swipe("880 1452", "880 342", 2000, true);
  // Click to return to all mails
  adb.keyevent(4, true);
  // Clicked to write email
  adb.tap("1303 2230", true);
  // Any address
  adb.typeWithKeyboard("aseaseasease@gmail.com", true);
  adb.tap("707 782", true);
  // Subject
  adb.tap("465 776", true);
  adb.typeWithKeyboard("This is a test message from UImpatience", true);
  // Email text
  adb.tap("292 986", true);
  adb.typeWithKeyboard(
      "Creating a simple e-mail to test UImpatience project. If you are "
      "receiving this email feel blessed about it, you are the chosen one!",
      true);
  // Close keyboard
  adb.tap("310 2482", true);
  // send email
  adb.tap("1213 187", true);
  // delete last message sent
  adb.swipe("705 514", "705 514", 2500, true);
  adb.tap("1060 199", true);
  // opens sideBar menu
  adb.tap("158 212", true);
  // Clicks on All Mail
  adb.tap("452 411", true);
  adb.closeApp();
}