#ifndef YOUTUBE_H
#define YOUTUBE_H

#include "../helpers/generic.hpp"
#include "../helpers/adbManager.hpp"
#include "../helpers/responseTime.hpp"
  
#include <fstream>

class Youtube {
 public:
  Youtube();
  virtual ~Youtube();
  void youtubeScript(std::string device);
};
#endif