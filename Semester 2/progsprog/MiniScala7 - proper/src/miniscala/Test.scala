package miniscala

import miniscala.AbstractMachine._
import miniscala.Compiler._
import miniscala.parser.Parser.parse

object Test132 {

  def main(args: Array[String]): Unit = {
    List(Add) :: List[Instruction]()
    test("2 + 3", 5)
    test("3==4",0)
    test("(3<4) == 3",0)
    testFail("!3")
    testFail("1&3")
    testFail("3|-4")
    test("{ val x = 1; x * 2 } + 3",5)
    println(execute(compile(parse("{ val x = 2; { val y = 3; x *{ val x = 4;x + 1}}}")), List()))
    println(compile(parse("{ val y = 3; x *{ val x = 4;x + 1}}")))
    println(execute(compile(parse("(1 +{ val x = 2; { val y = 3; x *{ val x = 4;x + 1}}}) + 4")), List()))
    println(execute(compile(parse("{ val y = 3; x *{ val x = 4;x + 1}}")), List()))
    println(execute(compile(parse("{ val x = 2; { val y = 3; x *{ val x = 4;x + 1}}}")), List()))
    test("{ val x = 2; { val y = 3; x *{ val x = 4;x + 1}}}",11)
    test("(1 +{ val x = 2; { val y = 3; x *{ val x = 4;x + 1}}}) + 4",15)


    // <-- add more test cases here
  }

  def test(prg: String, result: Int) = {
    assert(execute(compile(parse(prg)), List()) == result)
  }
  def testFail(prg: String) = {
    try{
      execute(compile(parse(prg)),List())
      assert(false)
    } catch {
      case _ => assert(true)
    }
  }

}