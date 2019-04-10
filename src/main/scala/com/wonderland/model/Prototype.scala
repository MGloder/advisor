package com.wonderland.model

class Prototype(input: Array[String]){
  private val count = input.size

  def parser(): String = {
    return input.head
  }

  def example() = {
    count
  }

}
