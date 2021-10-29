package org.burbokop.exp_craft.utils

import com.google.common.base.Predicate


class FuncToPredicate[A](function: A => Boolean) {
  def googlePredicate: com.google.common.base.Predicate[A] = (input: A) => function(input)
  def akkaPredicate: akka.japi.Predicate[A] = (param: A) => function(param)
  def javaPredicate: java.util.function.Predicate[A] = googlePredicate
  def predicate: com.google.common.base.Predicate[A] = googlePredicate
}
object FuncToPredicate {
  implicit def apply[A](function: A => Boolean) = new FuncToPredicate[A](function)
}