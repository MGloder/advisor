package com.machinedoll.advisor.utils

import com.typesafe.config.{Config, ConfigFactory}

class ConfigLoader {
  def load: Config  = {
    ConfigFactory load
  }
}
