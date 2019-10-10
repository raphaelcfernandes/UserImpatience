#ifndef GENERIC_H
#define GENERIC_H

#include <chrono>
#include <string>
#include <iostream>

class Generic {
 public:
  Generic();
  virtual ~Generic();
  static std::string GetStdoutFromCommand(std::string cmd);
  static std::time_t getCurrentTimestamp();
};
#endif