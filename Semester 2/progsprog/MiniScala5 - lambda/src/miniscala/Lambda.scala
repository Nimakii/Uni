package miniscala

import miniscala.Ast._
import miniscala.Interpreter.{Val, _}
import miniscala.parser.Parser

import scala.io.StdIn

/**
  * Compiler from (a large part of) MiniScala v5 to lambda calculus.
  */


object Lambda {

  def main(args: Array[String]): Unit = {
    println("Dette er 2: " + Unparser.unparse(encode(IntLit(2))))
    println("Dette er 4: " + Unparser.unparse(encode(IntLit(4))))
    println("Dette er 0: " + Unparser.unparse(encode(IntLit(0))))
    println("Test 3=0: "+ Unparser.unparse(encode(BinOpExp(encode(IntLit(3)),EqualBinOp(),IntLit(0)))))
    println("Test 0=0: "+ Unparser.unparse(encode(BinOpExp(encode(IntLit(0)),EqualBinOp(),IntLit(0)))))
    println("Test !true: "+ Unparser.unparse(encode(UnOpExp(NotUnOp(),BoolLit(true)))))
    println("Test !false: "+ Unparser.unparse(encode(UnOpExp(NotUnOp(),BoolLit(false)))))
    //println(eval(BlockExp(List(ValDecl("a",None,IntLit(3)),ValDecl("b",None,IntLit(4))),List(),BinOpExp(VarExp("a"),PlusBinOp(),VarExp("b"))),Map[Id,Val]()))
    //println("Test af val a =3; val b=4; a+b" +encode(BlockExp(List(ValDecl("a",None,IntLit(3)),ValDecl("b",None,IntLit(4))),List(),BinOpExp(VarExp("a"),PlusBinOp(),VarExp("b")))))
    //println(Parser.parse("{def f(a) =a+2; def g(b)=3*b; f(4)+g(5)}"))
    //println(encode(BlockExp(List(),List(DefDecl("f",List(FunParam("a",None)),None,BinOpExp(VarExp("a"),PlusBinOp(),IntLit(2))), DefDecl("g",List(FunParam("b",None)),None,BinOpExp(IntLit(3),MultBinOp(),VarExp("b")))),BinOpExp(CallExp(VarExp("f"),List(IntLit(4))),PlusBinOp(),CallExp(VarExp("g"),List(IntLit(5)))))))
    println(Unparser.unparse(encode(Parser.parse("{val x = 3; x}"))))
    println(evaltest(Parser.parse("{def  f(x) = 3; def y(z) = 4; f(1)}")))
    println(Unparser.unparse(encode(Parser.parse("{def f(x) = 3; f(2)}"))))
    println("her"+Unparser.unparse(encode(Parser.parse("{val a =3; val b=4; a+b}"))))
    //println(Unparser.unparse(encode(Parser.parse("{def f(x) = 3; def y(z) = 4; f(1)}"))))
    println(decodeBoolean(evaltest(encode(BoolLit(true)))))
    println(decodeBoolean(evaltest(encode(BoolLit(false)))))
    println("Her omkring")
    opg89("0 + 1 == 0")
    println("")
    println("")
    println(encode(BinOpExp(IntLit(0),PlusBinOp(),IntLit(1))))
    println(Unparser.unparse(encode(BinOpExp(IntLit(0),PlusBinOp(),IntLit(1)))))
    //println(Unparser.unparse(encode(BinOpExp(IntLit(3),EqualBinOp(),IntLit(0)))))
    //println(decodeBoolean(ClosureVal(List(FunParam("t",None),FunParam("e",None)),None,encode(BinOpExp(IntLit(3),EqualBinOp(),IntLit(0))),Map[Id,Val](),List())))
    //println(decodeBoolean(evaltest(encode(BinOpExp(IntLit(3),EqualBinOp(),IntLit(0))))))
  }
  def opg89(s: String): Unit = {
    val expr = Parser.parse(s)
    println(encode(expr))
    println(Unparser.unparse(encode(expr)))
    println(evaltest(expr))
  }
  val FIX: Exp = Parser.parse("((x)=>(y)=>(y((z)=>x(x)(y)(z))))((x)=>(y)=>(y((z)=>x(x)(y)(z))))")

  def encode(e: Exp): Exp =
    e match {
      case IntLit(c) if c >= 0 => // non-negative integer literals are encoded as on slide 18
        if (c == 0) return LambdaExp(List(FunParam("s",None)), LambdaExp(List(FunParam("z",None)),VarExp("z")))
        var value2 = CallExp(VarExp("s"),List(VarExp("z")))
        for(i<-0 until c-1){
          value2 = CallExp(VarExp("s"),List(value2))
        }
        LambdaExp(List(FunParam("s",None)), LambdaExp(List(FunParam("z",None)),value2))
        // 1 = LambdaExp(List(FunParam("s",None)), LambdaExp(List(FunParam("z",None)),CallExp(VarExp("s"),List(VarExp("z")))))
      case BoolLit(c) => // boolean literals are encoded as on slide 15
        if (c) LambdaExp(List(FunParam("t",None)), LambdaExp(List(FunParam("e", None)), VarExp("t")))
        else LambdaExp(List(FunParam("t", None)), LambdaExp(List(FunParam("e", None)), VarExp("e")))
      case VarExp(id) => // variables need no encoding
        e
      case BinOpExp(leftexp, EqualBinOp(), IntLit(0)) => // e == 0, slide 18 e(n=>false,true)
        CallExp(encode(leftexp),List(LambdaExp(List(FunParam("n",None)),
          LambdaExp(List(FunParam("t", None)), LambdaExp(List(FunParam("e", None)), VarExp("e")))), //false
          LambdaExp(List(FunParam("t",None)), LambdaExp(List(FunParam("e", None)), VarExp("t"))))) //true
      case BinOpExp(leftexp, MinusBinOp(), IntLit(1)) => // e - 1, slide 20
        LambdaExp(List(FunParam("s", None)), LambdaExp(List(FunParam("z", None)),
          CallExp(CallExp(CallExp(encode(leftexp), List(
            LambdaExp(List(FunParam("g", None)), LambdaExp(List(FunParam("h", None)),
              CallExp(VarExp("h"), List(CallExp(VarExp("g"), List(VarExp("s"))))))))),
            List(LambdaExp(List(FunParam("u", None)), VarExp("z")))),
            List(LambdaExp(List(FunParam("u", None)), VarExp("u"))))))
      case BinOpExp(leftexp, op, rightexp) =>
        op match {
          case PlusBinOp() => // e1 + e2, slide 20 (we assume there are no floats or strings)
            LambdaExp(List(FunParam("s", None)), LambdaExp(List(FunParam("z", None)),
              CallExp(CallExp(encode(leftexp), List(VarExp("s"))),
                List(CallExp(CallExp(encode(rightexp), List(VarExp("s"))), List(VarExp("z")))))))
          case MultBinOp() => // e1 * e2, slide 20
            LambdaExp(List(FunParam("s", None)), CallExp(encode(rightexp), List(CallExp(encode(leftexp), List(VarExp("s"))))))
          case AndBinOp() => // e1 & e2, slide 15
            encode(IfThenElseExp(leftexp, rightexp, BoolLit(false)))
          case OrBinOp() => // e1 | e2, slide 15
            encode(IfThenElseExp(leftexp, BoolLit(true), rightexp))
          case MinusBinOp() => // e1 - e2 (not in slides, see https://en.wikipedia.org/wiki/Church_encoding)
            CallExp(CallExp(encode(rightexp),
              List(LambdaExp(List(FunParam("n", None)),
                LambdaExp(List(FunParam("s", None)), LambdaExp(List(FunParam("z", None)),
                  CallExp(CallExp(CallExp(VarExp("n"), List(
                    LambdaExp(List(FunParam("g", None)), LambdaExp(List(FunParam("h", None)),
                      CallExp(VarExp("h"), List(CallExp(VarExp("g"), List(VarExp("s"))))))))),
                    List(LambdaExp(List(FunParam("u", None)), VarExp("z")))),
                    List(LambdaExp(List(FunParam("u", None)), VarExp("u"))))))))),
              List(encode(leftexp)))
          case LessThanOrEqualBinOp() => // e1 <= e2 (not in slides, see https://en.wikipedia.org/wiki/Church_encoding)
            encode(BinOpExp(BinOpExp(leftexp, MinusBinOp(), rightexp), EqualBinOp(), IntLit(0)))
          case LessThanBinOp() => // e1 < e2 (not in slides)
            encode(BinOpExp(BinOpExp(BinOpExp(leftexp, PlusBinOp(), IntLit(1)), MinusBinOp(), rightexp), EqualBinOp(), IntLit(0)))
          case _ => // remaining cases are not (yet) implemented
            throw new EncoderError(e)
        }
      case UnOpExp(op, subexp) =>
        op match {
          case NotUnOp() => // !e, slide 15
            encode(IfThenElseExp(subexp, BoolLit(false), BoolLit(true)))
          case _ => // remaining cases are not (yet) implemented
            throw new EncoderError(e)
        }
      case IfThenElseExp(condexp, thenexp, elseexp) => // if (e1) e2 else e3, slide 15
        // CallExp(CallExp(encode(condexp), List(encode(thenexp))), List(encode(elseexp))) // no good, evaluates both branches if using call-by-value
        CallExp(CallExp(encode(condexp),
          List(LambdaExp(List(FunParam("a", None)), CallExp(encode(thenexp), List(VarExp("a")))))),
          List(LambdaExp(List(FunParam("b", None)), CallExp(encode(elseexp), List(VarExp("b")))))) // mimics call-by-name
      case BlockExp(params,List(), e2: Exp) => // { val x = e1; e2 }, slide 23
        //CallExp(LambdaExp(List(FunParam(id,None)),encode(e2)),encode(e1))
        //CallExp(LambdaExp(List(FunParam(id, None)), encode(e2)),
            //List(encode(e1)))
        var startparam = e2
        for (p<- params.reverse){
          startparam = LambdaExp(List(FunParam(p.x,None)),startparam)
        }
        CallExp(startparam,params.map(p=>p.exp))
      case BlockExp(List(), List(DefDecl(f, List(FunParam(x, _)), _, e1)), e2: Exp) => // { def f(x) = e1; e2 }, slide 23
        CallExp(LambdaExp(List(FunParam(f, None)), encode(e2)),
          List(CallExp(FIX,
            List(LambdaExp(List(FunParam(f, None)), LambdaExp(List(FunParam(x, None)), encode(e1)))))))
      case TupleExp(List(e1, e2)) => // (e1, e2), slide 21
        LambdaExp(List(FunParam("p", None)),
          CallExp(CallExp(VarExp("p"), List(encode(e1))), List(encode(e2))))
      case MatchExp(mexp, List(MatchCase(List(x, y), caseexp))) => // e1 match { case (x,y) => e2 }, slide 21
        encode(BlockExp(List(ValDecl("p", None, mexp)), List(),
          BlockExp(List(ValDecl(x, None, CallExp(VarExp("p"), List(LambdaExp(List(FunParam("x", None)), LambdaExp(List(FunParam("y", None)), VarExp("x"))))))), List(),
            BlockExp(List(ValDecl(y, None, CallExp(VarExp("p"), List(LambdaExp(List(FunParam("x", None)), LambdaExp(List(FunParam("y", None)), VarExp("y"))))))), List(),
              caseexp))))
      case CallExp(target, args) => // call expressions are trivial, just encode the arguments recursively
        CallExp(encode(target), args.foldLeft(List[Exp]())((es, a) => encode(a) :: es))
      case LambdaExp(params, body) => // lambdas are trivial, just encode the body recursively
        val ps = params.map(p => FunParam(p.x, None)) // remove the type annotations, to avoid annoying the dynamic type checker
        LambdaExp(ps, encode(body))
      case _ => // remaining cases are not (yet) implemented
        throw new EncoderError(e)
    }

  def decodeNumber(v: Val): Int = v match {
    case ClosureVal(params, _, exp, env,_) =>
      val unchurch = // see slide 22
        CallExp(CallExp(LambdaExp(params, exp),
          List(LambdaExp(List(FunParam("n", None)), BinOpExp(VarExp("n"), PlusBinOp(), IntLit(1))))),
          List(IntLit(0)))
      Interpreter.eval(unchurch, env) match {
        case IntVal(c) => c
        case _ => throw new RuntimeException(s"Unexpected decoded value $v")
      }
    case _ => throw new RuntimeException(s"Unexpected encoded value $v")
  }

  def decodeBoolean(v: Val): Boolean = v match {
    case ClosureVal(params, _, exp, env,_) =>
      val satan = // see slide 22
      CallExp(CallExp(LambdaExp(params,exp),
        List(BoolLit(true))),
        List(BoolLit(false)))
      Interpreter.eval(satan, env) match {
        case BoolVal(c) => c
        case _ => throw new RuntimeException(s"Unexpected decoded value $v")
      }
    case _ => throw new RuntimeException(s"Unexpected encoded value $v")
  }

  /**
    * Builds an initial environment, with a lambda-encoded value for each free variable in the program.
    */
  def makeInitialEnv(program: Exp): Env = {
    var env = Map[Id, Val]()
    for (x <- Vars.freeVars(program)) {
      print(s"Please provide an integer value for the variable $x: ")
      env = env + (x -> Interpreter.eval(encode(IntLit(StdIn.readInt())), Map[Id, Val]()))
    }
    env
  }

  class EncoderError(node: AstNode) extends MiniScalaError(s"Don't know how to encode $node", node.pos)
}
