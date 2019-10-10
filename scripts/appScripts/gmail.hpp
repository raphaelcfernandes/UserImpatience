#ifndef GMAIL_H
#define GMAIL_H

#include "../helpers/generic.hpp"
#include "../helpers/adbManager.hpp"
#include "../helpers/responseTime.hpp"

#include <iostream>
#include <fstream>
#include <chrono>

class Gmail {
 public:
  Gmail();
  virtual ~Gmail();
  void gmailScript(std::string governor);
};
#endif