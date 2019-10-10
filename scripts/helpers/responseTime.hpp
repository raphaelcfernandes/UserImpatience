#ifndef RESPONSETIME_H
#define RESPONSETIME_H

#include "generic.hpp"
#include <chrono>
#include <thread>

class ResponseTime {
 public:
  ResponseTime();
  virtual ~ResponseTime();
  static int calculateResponseTime();
};

#endif