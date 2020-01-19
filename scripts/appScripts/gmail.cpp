#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript(std::string device) {
    AdbManager::getInstance()->openAppWithShellMonkey("gmail");
    if (device == "Nexus 5") {
        // opens sideBar menu
        AdbManager::getInstance()->tap("108 180", true);
        // Clicks on All Mail
        AdbManager::getInstance()->tap("345 1709", true);
        // clicks on the first email and 'reads' it
        AdbManager::getInstance()->tap("492 489", true);
        // Scroll down screen
        AdbManager::getInstance()->swipe("726 1712", "726 550", 2000, true);
        // Click to return to all mails
        AdbManager::getInstance()->keyevent(4, true);
        // Clicked to write email
        AdbManager::getInstance()->tap("956 1643", true);
        // Any address
        AdbManager::getInstance()->typeWithKeyboard("aseaseasease@gmail.com", true);
        // Click to write subject
        AdbManager::getInstance()->tap("260 661", true);
        // Subject
        AdbManager::getInstance()->tap("455 717", true);
        AdbManager::getInstance()->typeWithKeyboard("This is a test message from UImpatience", true);
        // Email text
        AdbManager::getInstance()->tap("164 857", true);
        AdbManager::getInstance()->typeWithKeyboard(
            "Creating a simple e-mail to test UImpatience project.", true);
        // send email
        AdbManager::getInstance()->tap("872 179", true);
        // delete last message sent
        AdbManager::getInstance()->swipe("591 489", "591 489", 2500, true);
        // Click on trash bin
        AdbManager::getInstance()->tap("734 165", true);
        // opens sideBar menu
        AdbManager::getInstance()->tap("108 180", true);
        // Clicks on primary
        AdbManager::getInstance()->tap("473 353", true);
    }
    if (device == "Nexus 6") {
        // opens sideBar menu
        AdbManager::getInstance()->tap("158 212", true);
        // Clicks on All Mail
        AdbManager::getInstance()->tap("395 2040", true);
        // clicks on the first email and 'reads' it
        AdbManager::getInstance()->tap("742 595", true);
        // Scroll down screen
        AdbManager::getInstance()->swipe("880 1452", "880 342", 2000, true);
        // Click to return to all mails
        AdbManager::getInstance()->keyevent(4, true);
        // Clicked to write email
        AdbManager::getInstance()->tap("1303 2230", true);
        // Any address
        AdbManager::getInstance()->typeWithKeyboard("aseaseasease@gmail.com", true);
        AdbManager::getInstance()->tap("707 782", true);
        // Subject
        AdbManager::getInstance()->tap("465 776", true);
        AdbManager::getInstance()->typeWithKeyboard("This is a test message from UImpatience", true);
        // Email text
        AdbManager::getInstance()->tap("292 986", true);
        AdbManager::getInstance()->typeWithKeyboard(
            "Creating a simple e-mail to test UImpatience project.", true);
        // Close keyboard
        AdbManager::getInstance()->tap("310 2482", true);
        // send email
        AdbManager::getInstance()->tap("1213 187", true);
        // delete last message sent
        AdbManager::getInstance()->swipe("705 514", "705 514", 2500, true);
        AdbManager::getInstance()->tap("1060 199", true);
        // opens sideBar menu
        AdbManager::getInstance()->tap("158 212", true);
        // Clicks on All Mail
        AdbManager::getInstance()->tap("452 411", true);
    }
    AdbManager::getInstance()->closeApp(device);
}