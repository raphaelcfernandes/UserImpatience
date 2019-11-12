#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript() {
  AdbManager adb;
  ResponseTime responseTime;

  responseTime.calculateResponseTime();
  // opens sideBar menu
  adb.tap("158 212", true);
  responseTime.calculateResponseTime();
  // Clicks on All Mail
  adb.tap("395 2040", true);
  responseTime.calculateResponseTime();
  // clicks on the first email and 'reads' it
  adb.tap("742 595", true);
  responseTime.calculateResponseTime();
  // Scroll down screen
  adb.swipe("880 1452", "880 342", 2000, true);
  responseTime.calculateResponseTime();
  // Click to return to all mails
  adb.tap("67 212", true);
  responseTime.calculateResponseTime();
  // Clicked to write email
  adb.tap("1303 2230", true);
  responseTime.calculateResponseTime();
  // Any address
  adb.typeWithKeyboard("aseaseasease@gmail.com", true);
  responseTime.calculateResponseTime();
  adb.tap("707 782", true);
  responseTime.calculateResponseTime();
  // Subject
  adb.tap("465 776", true);
  responseTime.calculateResponseTime();
  adb.typeWithKeyboard("This is a test message from UImpatience", true);
  responseTime.calculateResponseTime();
  // Email text
  adb.tap("292 986", true);
  responseTime.calculateResponseTime();
  adb.typeWithKeyboard(
      "Creating a simple e-mail to test UImpatience project. If you are "
      "receiving this email feel blessed about it, you are the chosen on!",
      true);
  responseTime.calculateResponseTime();
  // Close keyboard
  adb.tap("310 2482", true);
  responseTime.calculateResponseTime();
  // send email
  adb.tap("1213 187", true);
  responseTime.calculateResponseTime();
  // delete last message sent
  adb.swipe("705 514", "705 514", 2500, true);
  responseTime.calculateResponseTime();
  adb.tap("1060 199", true);
  // opens sideBar menu
  adb.tap("158 212", true);
  responseTime.calculateResponseTime();
  // Clicks on All Mail
  adb.tap("452 411", true);
  responseTime.calculateResponseTime();
  adb.tap("1159 2481", true);
  adb.swipe("1014 2132", "84 2132", 100, true);
}