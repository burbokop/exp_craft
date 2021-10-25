package org.burbokop.exp_craft.utils


class FuncToPredicate[A](function: A => Boolean) {
  def googlePredicate(): com.google.common.base.Predicate[A] = new com.google.common.base.Predicate[A] {
    override def apply(input: A): Boolean = function(input)
  }
  def akkaPredicate(): akka.japi.Predicate[A] = new akka.japi.Predicate[A] {
    override def test(param: A): Boolean = function(param)
  }
  def javaPredicate(): java.util.function.Predicate[A] = googlePredicate()

  def predicate() = googlePredicate()
}
object FuncToPredicate {
  implicit def apply[A](function: A => Boolean) = new FuncToPredicate[A](function)
}