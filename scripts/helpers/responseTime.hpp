#ifndef RESPONSETIME_H
#define RESPONSETIME_H

#include <chrono>
#include <thread>
#include "generic.hpp"

class ResponseTime {
 public:
  ResponseTime();
  virtual ~ResponseTime();
  int calculateResponseTime();
};

#endif