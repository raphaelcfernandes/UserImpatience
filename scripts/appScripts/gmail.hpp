#ifndef GMAIL_H
#define GMAIL_H

#include "../helpers/adbManager.hpp"
#include "../helpers/responseTime.hpp"

class Gmail {
 public:
  Gmail();
  virtual ~Gmail();
  void gmailScript(std::string device);
};
#endif