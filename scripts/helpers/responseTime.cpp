#include "responseTime.hpp"

ResponseTime::ResponseTime() {}
ResponseTime::~ResponseTime() {}

int ResponseTime::calculateResponseTime() {
  int PREV_TOTAL = 0, PREV_IDLE = 0, TOTAL = 100, DIFF_USAGE = 100, cont = 0,
      pos = 0;
  int IDLE = 0, DIFF_IDLE = 0, DIFF_TOTAL = 0;
  std::string text = "";
  // while (true) {
  while (DIFF_USAGE >= 0 || cont <= 2) {
    /**
     * cpu user nice system idle iowait irq softirq 0 0 0
     */
    std::string CPU =
        Generic::GetStdoutFromCommand("adb shell $(echo sed -n 1p /proc/stat)");
    text = "";
    TOTAL = 0;
    IDLE = 0;
    bool flag = true;
    pos = 0;
    for (int i = 0; i < CPU.size(); i++) {
      if (isdigit(CPU[i])) {
        text += CPU[i];
        flag = true;
      } else {
        flag = false;
      }
      if (!flag && text.size() > 0) {
        int val = stoi(text);
        TOTAL += val;
        if (pos == 3) {
          IDLE = val;
        }
        text = "";
        pos++;
      }
    }

    DIFF_IDLE = IDLE - PREV_IDLE;
    DIFF_TOTAL = TOTAL - PREV_TOTAL;
    DIFF_USAGE = 100 - ((100 * DIFF_IDLE) / DIFF_TOTAL) - 18;
    std::cout << "Cpu usage - (cat cost): " << DIFF_USAGE  +18<< std::endl;
    PREV_TOTAL = TOTAL;
    PREV_IDLE = IDLE;
    cont += 1;
    std::this_thread::sleep_for(std::chrono::milliseconds(100));
  }
  return cont - 2;
}