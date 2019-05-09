package miniscala
import miniscala.parser.Parser.parse

object TestVersion6JKsMor {


  def main(args: Array[String]): Unit = {
    println(parse("{ def f(x: Int): Int = x; f(2) }"))
  }
}
