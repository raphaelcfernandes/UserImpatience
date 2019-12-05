#ifndef GENERIC_H
#define GENERIC_H

#include <chrono>
#include <fstream>
#include <iostream>
#include <string>
#include <thread>

class Generic {
 private:
  Generic(){};
  static Generic* instance;
  virtual ~Generic(){};
 public:
  std::ofstream file;
  static Generic* getInstance();
  std::time_t getCurrentTimestamp();
  std::string GetStdoutFromCommand(std::string cmd);
  void createFile(std::string governor, std::string app, std::string iteration,std::string device);
  void writeToFile(std::string event);
  void sleep(int timeInMs);
};

#endif
