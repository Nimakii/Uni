package miniscala

import miniscala.Ast._
import miniscala.Interpreter._
import miniscala.TypeChecker._
import miniscala.parser.Parser.parse

object Test95 {

  def main(args: Array[String]): Unit = {
    val w14 = "{def fac(n: Int): Int = {def f(n: Int, acc: Int): Int = if (n == 0) acc else f(n - 1, n * acc); f(n, 1) }; fac(5)}"
    testVal(w14,IntVal(120))
  }

  def test(prg: String, rval: Val, rtype: Type) = {
    testVal(prg, rval)
    testType(prg, rtype)
  }

  def testFail(prg: String) = {
    testValFail(prg)
    testTypeFail(prg)
  }

  def testVal(prg: String, value: Val, env: Env = Map[Id, Val](), sto: Sto = Map[Loc, Val]()) = {
    val (res, _) = eval(parse(prg), env, sto)
    assert(res == value)
  }

  def testType(prg: String, out: Type, tenv: TypeEnv = Map[Id, Type]()) = {
    assert(typeCheck(parse(prg), tenv) == out)
  }

  def testValFail(prg: String,env: Env = Map[Id, Val](), sto: Sto = Map[Loc, Val]() ) = {
    try {
      eval(parse(prg), env, sto)
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