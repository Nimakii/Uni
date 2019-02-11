package miniscala
import parser.Parser

object Test {
  def main(args: Array[String]): Unit ={
    assert(Interpreter.simplify(Parser.parse("3%5"))==Parser.parse("3"))
    assert(Interpreter.simplify(Parser.parse("3-3"))==Parser.parse("0"))
    assert(Interpreter.simplify(Parser.parse("a/a"))==Parser.parse("1"))
    assert(Interpreter.simplify(Parser.parse("10*0"))==Parser.parse("0"))
    assert(Interpreter.simplify(Parser.parse("0*10"))==Parser.parse("0"))
    assert(Interpreter.simplify(Parser.parse("55+0"))==Parser.parse("55"))
    assert(Interpreter.simplify(Parser.parse("0-12"))==Parser.parse("-12"))
    assert(Interpreter.simplify(Parser.parse("5*1"))==Parser.parse("5"))
    assert(Interpreter.simplify(Parser.parse("0/4"))==Parser.parse("0"))
    assert(Interpreter.simplify(Parser.parse("5max5"))==Parser.parse("5"))
    assert(Interpreter.simplify(Parser.parse("(3*3-9)max(0*9)"))==Parser.parse("(((3*3)-9)max0)"))
    assert(Interpreter.simplify(Parser.parse("(5*(a/(--a)))*(5*(1-1))"))==Parser.parse("0"))
    assert(Interpreter.simplify(Parser.parse("{val x=3*1;x*0}"))==Parser.parse("{ val x = 3 ; 0 }"))
    assert(Interpreter.simplify(Parser.parse("{val x={val z = 7/7 ; z*1};z*x*0}"))==Parser.parse("{ val x = { val z = 1 ; z } ; 0 }"))
  }
}
