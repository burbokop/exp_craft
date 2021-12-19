package org.burbokop.exp_craft.tools

object Runner {

  def main(args: Array[String]): Unit = {
    println("GGG BEFORE TESTS")

    org.scalatest.tools.Runner.main(args)

    println("GGG AFTER TESTS")
  }
}
