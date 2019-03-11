package miniscala

import miniscala.Ast._
import miniscala.Interpreter._
import miniscala.TypeChecker._
import miniscala.parser.Parser.parse

object Test49 {

  def main(args: Array[String]): Unit = {
    testVal("{ def f(x) = x; f(2)}", IntVal(2))
    testTypeFail("{ def f(x) = x; f(2)}")
    test("{ def f(x: Int): Int = x; f(2) }", IntVal(2), IntType())
    test("{def get(x: Int): Int = x; get(2) }", IntVal(2), IntType())
    test("{def f(x: Int) : Int = x; if(true) f(5) else f(3)}",IntVal(5),IntType())
    test("{def dyt(x: Int): Int = x*2; dyt(21)}",IntVal(42),IntType())
    test("{def fac(n: Int) : Int = if (n == 0) 1 else n * fac(n - 1); fac(2)}",IntVal(2),IntType())
    test("{def f(y: Int): Boolean = (y == y); f(2)}",BoolVal(true),BoolType())
    testFail("{ def f(x: Int): Int = x; f(2, 3) }")
    testFail("{def f(y: Int): Int = (y == y); f(2)}")
    testFail("{def fac(n: Int) : Boolean = if (n == 0) 1 else n * fac(n - 1); fac(2)} ")
    testFail("{ def f(x: Float): Int = x; f(2f) }")
    val tests8a = "{ val x = 3; def use(f, y) = f(x, y); def add(a, b) = a + b; def mult(a, b) = a * b; use(add, 7) - use(mult, 13)}"
    val tests8b = "{ def choose(c) = if (c) add else mult; def add(a, b) = a + b; def mult(a, b) = a * b;{ val foo = choose(true); foo(1, 2) - choose(false)(7, 13)}}"
    testingVal(tests8a)
    testingVal(tests8b)
    val tests29a = "{val inc: Int => Int = (x: Int) => x + 1;inc(3)}"
    val tests29b = "{val inc: Int => Int = (x: Int) => x + 1;def twice(f: Int => Int, x: Int): Int = f(f(x));twice(inc, 3)}"
    val tests29c = "{val add: Int => (Int => Int) = (x: Int) => (y: Int) => x + y;val inc: Int => Int = add(1);add(1)(2) + inc(3)}"
    testingVal(tests29a)
    testingVal(tests29b)
    testingVal(tests29c)
    testingType(tests29a)
    testingType(tests29b)
    val tests35 = "{val x = (f: Int => Int) => (x: Int) => f(f(x));def g(a: Int) = a + 1;x(g)(2)}"
    curryTest("def f(x,y)=x+y;curry(f)(2)(3)")
    curryTest("def hej(x) = 3*x; def med(y) = y+2; def dig(x,y) = hej(x)+med(y);curry(dig)(1)(2)")
  }
  def curryTest(prg: String) = {
    val currytest = "{def curry(f) = (x) => (y) => f(x,y);def uncurry(f) = (x,y) => f(x)(y);"+prg+"}"
    testingVal(currytest)
  }
  def testingVal(prg: String) = {
    testVal(prg,eval(parse(prg),Map[Id, Val]()))
  }
  def testingType(prg: String) = {
    testType(prg,typeCheck(parse(prg),Map[Id, Type]()))
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