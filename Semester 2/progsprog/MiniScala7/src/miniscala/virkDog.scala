package miniscala

import miniscala.Ast.{Exp, Id}
import miniscala.Interpreter.{Val, eval}
import miniscala.Lambda.{decodeNumber, encode}
import miniscala.parser.Parser

object virkDog {

  def evaltest(e: Exp): Val = eval(e,Map[Id,Val]())
  def helvete(p: String): Unit = {
    val program = Parser.parse(p)
    val encoded = Lambda.encode(program)
    println(s"Encoded program: ${Unparser.unparse(encoded)}")
    val initialEnv = Lambda.makeInitialEnv(program)
    val result = Interpreter.eval(encoded, initialEnv)
    println(s"Output from encoded program: ${Interpreter.valueToString(result)}")
    println(s"Decoded output: ${Lambda.decodeNumber(result)}")
  }

  def main(args: Array[String]): Unit = {
    println(Parser.parse("{val x = 1; val y = 2; val z = 3; x+y+z}"))
    println(encode(Parser.parse("{val x = 1; val y = 2; val z = 3; x+y+z}")))
    println(evaltest(Parser.parse("{val x = 1; val y = 2; val z = 3; x+y+z}")))
    println(Unparser.unparse(encode(Parser.parse("{val x = 1; val y = 2; val z = 3; x+y+z}"))))
    println(evaltest(encode(Parser.parse("{val x = 1; val y = 2; val z = 3; x+y+z}"))))
    println(decodeNumber(evaltest(encode(Parser.parse("{ def fac(n: Int): Int =    if (n == 0) 1    else n * fac(n - 1);  fac(4)}")))))
    helvete("{ def fac(n: Int): Int =    if (n == 0) 1    else n * fac(n - 1);  fac(4)}")
  }
}
