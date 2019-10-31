#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript() {
  AdbManager adb;
  ResponseTime::calculateResponseTime();
  // opens sideBar menu
  adb.tap("158 212");
  ResponseTime::calculateResponseTime();
  // Clicks on All Mail
  adb.tap("395 2040");
  ResponseTime::calculateResponseTime();
  // clicks on the first email and 'reads' it
  adb.tap("742 595");
  ResponseTime::calculateResponseTime();
  // Scroll down screen
  adb.swipe("880 1452", "880 342", 2000);
  ResponseTime::calculateResponseTime();
  // Click to return to all mails
  adb.tap("67 212");
  ResponseTime::calculateResponseTime();
  // Clicked to write email
  adb.tap("1303 2230");
  ResponseTime::calculateResponseTime();
  // Any address
  adb.typeWithKeyboard("aseaseasease@gmail.com");
  ResponseTime::calculateResponseTime();
  adb.tap("707 782");
  ResponseTime::calculateResponseTime();
  // Subject
  adb.tap("465 776");
  ResponseTime::calculateResponseTime();
  adb.typeWithKeyboard("This is a test message from UImpatience");
  ResponseTime::calculateResponseTime();
  // Email text
  adb.tap("292 986");
  ResponseTime::calculateResponseTime();
  adb.typeWithKeyboard(
      "Creating a simple e-mail to test UImpatience project. If you are "
      "receiving this email feel blessed about it, you are the chosen on!");
  ResponseTime::calculateResponseTime();
  // Close keyboard
  adb.tap("310 2482");
  ResponseTime::calculateResponseTime();
  // send email
  adb.tap("1213 187");
  ResponseTime::calculateResponseTime();
  // delete last message sent
  adb.swipe("705 514", "705 514", 2500);
  ResponseTime::calculateResponseTime();
  adb.tap("1060 199");
  // opens sideBar menu
  adb.tap("158 212");
  ResponseTime::calculateResponseTime();
  // Clicks on All Mail
  adb.tap("452 411");
  ResponseTime::calculateResponseTime();
  adb.tap("1159 2481");
  adb.swipe("1014 2132", "84 2132", 100);
}