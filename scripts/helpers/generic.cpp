#include "generic.hpp"

Generic* Generic::instance = NULL;

Generic* Generic::getInstance() {
  if (!instance) {
    instance = new Generic();
  }
  return instance;
}

std::time_t Generic::getCurrentTimestamp() {
  // return
  // std::chrono::system_clock::to_time_t(std::chrono::system_clock::now());
  auto duration = std::chrono::system_clock::now().time_since_epoch();
  auto millis =
      std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();
  return millis;
}

std::string Generic::GetStdoutFromCommand(std::string cmd) {
  std::string data;
  FILE* stream;
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

void Generic::createFile(std::string governor, std::string app,
                         std::string iteration) {
  std::string p =
      "adbTouchEvents/" + governor + '/' + app + "/" + iteration + ".txt";
  std::cout << p << std::endl;
  this->file.open(p);
}

void Generic::writeToFile(std::string event) {
  this->file << event + ", " + std::to_string(getCurrentTimestamp()) + "\n";
}