package miniscala

import miniscala.Ast._
import miniscala.Interpreter._
import miniscala.TypeChecker._
import miniscala.parser.Parser.parse

object Test95 {

  def main(args: Array[String]): Unit = {
    
    test("{ def f(x: Int): Int = x; f(2) }", IntVal(2), IntType())
    testFail("{ def f(x: Int): Int = x; f(2, 3) }")
    test("2",IntVal(2),IntType())
    testVal("{ var z: Int = 0; { var t: Int = x; while (y <= t) { z = z + 1; t = t - y }; z } }", IntVal(3), Map("x" -> IntVal(17), "y" -> IntVal(5)))
    testType("{ var z: Int = 0; { var t: Int = x; while (y <= t) { z = z + 1; t = t - y }; z } }", IntType(), Map("x" -> IntType(), "y" -> IntType()))
    testVal("{ var x: Int = 0; def inc(): Int = { x = x + 1; x }; inc(); inc() }", IntVal(2))
    testType("{ var x: Int = 0; def inc(): Int = { x = x + 1; x }; inc(); inc() }", IntType())
    testVal("""{ def make(a: Int): Int => Int = {
              |    var c: Int = a;
              |    def add(b: Int): Int = { c = c + b; c };
              |    add
              |  };
              |  { val c1 = make(100);
              |    val c2 = make(1000);
              |    c1(1) + c1(2) + c2(3) } }""".stripMargin, IntVal(101 + 103 + 1003))
    test("{def get(x: Int): Int = x; get(2) }", IntVal(2), IntType())
    test("{def f(x: Int) : Int = x; if(true) f(5) else f(3)}",IntVal(5),IntType())
    test("{def dyt(x: Int): Int = x*2; dyt(21)}",IntVal(42),IntType())
    test(" {def fac(n: Int) : Int = if (n == 0) 1 else n * fac(n - 1); fac(2)} ",IntVal(2),IntType())
    test("{def f(y: Int): Boolean = (y == y); f(2)}",BoolVal(true),BoolType())
    testFail("{ def f(x: Int): Int = x; f(2, 3) }")
    testFail("{def f(y: Int): Int = (y == y); f(2)}")
    testFail(" {def fac(n: Int) : Boolean = if (n == 0) 1 else n * fac(n - 1); fac(2)} ")
    testFail("{ def f(x: Float): Int = x; f(2f) }")
    test("{ def te(u: Int => Int): Int = u(u(4)); te((u: Int) => u % 4) }",IntVal(0),IntType())
    testFail("{ def te(u: Boolean => Int): Int = u(u(4)); te((u: Int) => u % 4) }")
    testVal("{ def isEven(x) = if (x == 0) true else isOdd(x-1);" +
      " def isOdd(x) = if (x == 0) false else isEven(x-1);isEven(2) }",BoolVal(true))
    test("{var x = 10;{ x = x - 10; x } + { x = x * 7; x }}",IntVal(0),IntType())
    test("{var x = 1;var y = 2;{var x = 3;x = 4;y = 5;x + y};x = x + 10;x}",IntVal(11),IntType())
    test("{def fac(n: Int): Int = if (n == 0) 1 else n * fac(n - 1);fac(4)}",IntVal(24),IntType())
    test("{def fac(n: Int): Int = {var r = 1 ; var i = n ; while ( 0 < i){r = r * i;i = i - 1};r};fac(4)}",IntVal(24),IntType())
    test("{def fib(n: Int): Int = if (n <= 1) n else fib(n - 1) + fib(n - 2);fib(4)}",IntVal(3),IntType())
    test("{def fib(n: Int): Int = {if (n <= 1) n else {var pp = 0 ; var p = 1 ; var r = 0 ; var i = 2 ; while (i <= n) {r = p + pp ; pp = p ; p = r ; i = i + 1};r}};fib(4)}",IntVal(3),IntType())  }

  def test(prg: String, rval: Val, rtype: Type) = {
    testVal(prg, rval)
    testType(prg, rtype)
  }

  def testFail(prg: String) = {
    testValFail(prg)
    testTypeFail(prg)
  }

  def testVal(prg: String, value: Val, env: Env = Map[Id, Val](), sto: Sto = Map[Loc, Val]()) = {
    val (res, _) = eval(parse(prg), env,Map[Id, Constructor](), sto)
    assert(res == value)
  }

  def testType(prg: String, out: Type, tenv: TypeEnv = Map[Id, Type]()) = {
    assert(typeCheck(parse(prg), tenv) == out)
  }

  def testValFail(prg: String,env: Env = Map[Id, Val](), sto: Sto = Map[Loc, Val]() ) = {
    try {
      eval(parse(prg), env,Map[Id, Constructor](), sto)
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