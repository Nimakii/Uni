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
    pupuTest("(2-3)*(4%5)")
    pupuTest("--(((5-12)))max(8*9)-(15-3)")
    pupuTest("--((((5-12)))max(8*9))-15")
  }
  def pupuTest(input: String): Unit ={
    val pupu = Unparser.unparse(Parser.parse(Unparser.unparse(Parser.parse(input))))
    println("The input was: "+input)
    println("The output is: "+pupu)
    val value = Interpreter.eval(Parser.parse(input))-Interpreter.eval(Parser.parse(pupu))
    println("Their difference should be zero: "+value)
  }
}
