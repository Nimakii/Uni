package miniscala

import miniscala.Ast.NullType
import miniscala.Interpreter._
import miniscala.parser.Parser.parse

object Test112 {

  def main(args: Array[String]): Unit = {
    testValFail("""{ var z = null; z.f }""")
    testVal("""{ class C() { }; { var x: C = null; x = new C() } }""".stripMargin, TupleVal(List[Val]()))
    testVal("""{ class C() { }; { var x: C = new C(); x = null } }""".stripMargin, TupleVal(List[Val]()))
    println(TypeChecker.typeCheck(parse("{ val x: Int = 42; x }"),Map(),Map()))
    println(TypeChecker.typeCheck(parse("{ val x: Int = 42;  val y: Float = x;  class C() {};   { var z: C = null;    z = null ; z} }"),Map(),Map()))
    println(NullType().equals(TypeChecker.typeCheck(parse("{ val x: Int = 42;  val y: Float = x;  class C() {};   { var z: C = null;    z = null; z} }"),Map(),Map())))
    // <-- add more test cases here
  }

  def testVal(prg: String, value: Val, env: Env = Map(), cenv: ClassEnv = Map(), sto: Sto = Map()) = {
    val (res, _) = eval(parse(prg), env, cenv, sto)
    assert(res == value)
  }

  def testValFail(prg: String, env: Env = Map(), cenv: ClassEnv = Map(), sto: Sto = Map()) = {
    try {
      eval(parse(prg), env, cenv, sto)
      assert(false)
    } catch {
      case _: InterpreterError => assert(true)
    }
  }

}