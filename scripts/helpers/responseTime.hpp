#ifndef RESPONSETIME_H
#define RESPONSETIME_H

#include <chrono>
#include <thread>
#include <iostream>
#include <time.h>
#include <math.h>
#include "generic.hpp"


class ResponseTime {
 public:
  ResponseTime();
  virtual ~ResponseTime();
  long calculateResponseTime(std::string governor);
};

#endif
