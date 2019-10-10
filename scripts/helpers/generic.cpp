#include "generic.hpp"

Generic::Generic(){};
Generic::~Generic(){};

std::time_t Generic::getCurrentTimestamp() {
  return std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());
}

std::string Generic::GetStdoutFromCommand(std::string cmd) {
  std::string data;
  FILE *stream;
  const int max_buffer = 256;
  char buffer[max_buffer];
  // cmd.append(" 2>&1");

  stream = popen(cmd.c_str(), "r");
  if (stream) {
    while (!feof(stream))
      if (fgets(buffer, max_buffer, stream) != NULL) data.append(buffer);
    pclose(stream);
  }
  return data;
}
