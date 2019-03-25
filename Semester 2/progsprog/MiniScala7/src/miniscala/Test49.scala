package miniscala

import miniscala.Ast._
import miniscala.Interpreter._
import miniscala.TypeChecker._
import miniscala.parser.Parser.parse

object Test49 {

  def main(args: Array[String]): Unit = {
    testVal("{ def f(x) = x; f(2)}", IntVal(2))
    testTypeFail("{ def f(x) = x; f(2)}")
    testVal("{ def f(x: Int): Int = x; f(2) }", IntVal(2))
    test("{def get(x: Int): Int = x; get(2) }", IntVal(2), IntType())
    test("{def f(x: Int) : Int = x; if(true) f(5) else f(3)}",IntVal(5),IntType())
    test("{def dyt(x: Int): Int = x*2; dyt(21)}",IntVal(42),IntType())
    test(" {def fac(n: Int) : Int = if (n == 0) 1 else n * fac(n - 1); fac(2)} ",IntVal(2),IntType())
    test("{def f(y: Int): Boolean = (y == y); f(2)}",BoolVal(true),BoolType())
    testFail("{ def f(x: Int): Int = x; f(2, 3) }")
    testFail("{def f(y: Int): Int = (y == y); f(2)}")
    testFail(" {def fac(n: Int) : Boolean = if (n == 0) 1 else n * fac(n - 1); fac(2)} ")
    testFail("{ def f(x: Float): Int = x; f(2f) }")

    //Week 6
    test("{ def te(u: Int => Int): Int = u(u(4)); te((u: Int) => u % 4) }",IntVal(0),IntType())
    testFail("{ def te(u: Boolean => Int): Int = u(u(4)); te((u: Int) => u % 4) }")
    testVal("{ def isEven(x) = if (x == 0) true else isOdd(x-1);" +
      " def isOdd(x) = if (x == 0) false else isEven(x-1);isEven(2) }",BoolVal(true))




    // <-- add more test cases here
  }


  def test(prg: String, rval: Val, rtype: Type) = {
    testVal(prg, rval)
    testType(prg, rtype)
  }

  def testFail(prg: String) = {
    testValFail(prg)
    testTypeFail(prg)
  }

  def testVal(prg: String, value: Val, env: Env = Map[Id, Val]()) = {
    assert(eval(parse(prg), env) == value)
  }

  def testType(prg: String, out: Type, tenv: TypeEnv = Map[Id, Type]()) = {
    assert(typeCheck(parse(prg), tenv) == out)
  }

  def testValFail(prg: String) = {
    try {
      eval(parse(prg), Map[Id, Val]())
      assert(false)
    } catch {
      case _: InterpreterError => assert(true)
    }
  }

  def testTypeFail(prg: String) = {
    try {
      typeCheck(parse(prg), Map[Id, Type]())
      assert(false)
    } catch {
      case _: TypeError => assert(true)
    }
  }
}