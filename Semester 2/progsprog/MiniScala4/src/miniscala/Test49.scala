package miniscala

import miniscala.Ast._
import miniscala.Interpreter._
import miniscala.TypeChecker.{FunTypeEnv, TypeError, VarTypeEnv, typeCheck}
import miniscala.parser.Parser.parse

object Test49 {

  def main(args: Array[String]): Unit = {
    test("{ def f(x: Int): Int = x; f(2) }", IntVal(2),IntType())
    testVal("{ val x = 1;{ def q(a) = x + a;{ val x = 2;q(3)}}}",IntVal(4))
    testFail("{ def f(x: Int): Int = x; f(2, 3) }")
    test("{ def isEven(n: Int): Boolean = " +
      "if (n == 0) true " +
      "else isOdd(n - 1) ; " +
      "def isOdd(n: Int): Boolean = " +
      "if (n == 0) false " +
      "else isEven(n - 1) ; isEven(4) } ",BoolVal(true),BoolType())
    test("{ def f(x: Int): Int = 2*x; { def g(x: Int, y: Int): Int = if(x<y) 3*f(x) else 4*f(y); g(5,6) }}",IntVal(30),IntType())
    testFail("{ def f(x: Int): Int = 2*x; { def g(x: Int, y: Int): Int = if(x<y) 3*f(x) else 4*f(y); g(3,true) }}")
    testFail("{ def f(x: Int): Int = 2*x; { def g(x: Int, y: Int): Int = if(x<y) 3*f(x) else 4*f(y); g(3,4,5) }}")
    testVal("{ def f(x) = if(0<g(x)) g(x) else 0 ; def g(x) = 5 ; f(3) }",IntVal(5))
    testType("{ def f(x: Int): Int = g(x) ; def g(x: Int): Int = f(x) ; f(3) }",IntType())
  }

  def test(prg: String, rval: Val, rtype: Type) = {
    testVal(prg, rval)
    testType(prg, rtype)
  }

  def testFail(prg: String) = {
    testValFail(prg)
    testTypeFail(prg)
  }

  def testVal(prg: String, value: Val, venv: VarEnv = Map[Var, Val](), fenv: FunEnv = Map[Var, Closure]()) = {
    assert(eval(parse(prg), venv, fenv) == value)
  }

  def testType(prg: String, out: Type, venv: VarTypeEnv = Map[Var, Type](), fenv: FunTypeEnv = Map[Var, (List[Type], Type)]()) = {
    assert(typeCheck(parse(prg), venv, fenv) == out)
  }

  def testValFail(prg: String) = {
    try {
      eval(parse(prg), Map[Var, Val](), Map[Var, Closure]())
      assert(false)
    } catch {
      case _: InterpreterError => assert(true)
    }
  }

  def testTypeFail(prg: String) = {
    try {
      typeCheck(parse(prg), Map[Var, Type](), Map[Var, (List[Type], Type)]())
      assert(false)
    } catch {
      case _: TypeError => assert(true)
    }
  }
}