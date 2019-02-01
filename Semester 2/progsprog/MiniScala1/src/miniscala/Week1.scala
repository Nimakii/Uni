package miniscala
import miniscala.Ast._
import miniscala.parser.Parser

object Week1 {
  def main(args: Array[String]) = {

    val a1 = BinOpExp(IntLit(2),MinusBinOp(),IntLit(10))

    val a2 = Parser.parse("2-10")
    println("Invoking toString on the AST gives: "+a2)
    println("The ASTs are equal: " + (a1 == a2))
    println(Unparser.unparse(a2))
    println(Unparser.unparse(Parser.parse("-1+2*3-4%5/6")))
    println(Unparser.unparse(Parser.parse(Unparser.unparse(Parser.parse("--(-1+2)*3-4%(5-2)/(6-9)max(7*10)")))))
  }

}
