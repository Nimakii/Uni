package miniscala
import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.io.StdIn

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  case class Closure(params: List[FunParam], optrestype: Option[Type], body: Exp, venv: VarEnv, fenv: FunEnv, defs: List[DefDecl])

  type VarEnv = Map[Var, Val]
  type FunEnv = Map[Fun, Closure]
  sealed abstract class Val
  case class IntVal(v: Int) extends Val
  case class BoolVal(v: Boolean) extends Val
  case class FloatVal(v: Float) extends Val
  case class StringVal(v: String) extends Val
  case class TupleVal(vs: List[Val]) extends Val

  def eval(e: Exp, venv: VarEnv, fenv: FunEnv): Val = e match {
    case IntLit(c) =>
      trace("Integer "+c+ " found")
      IntVal(c)
    case BoolLit(c) =>
      trace("Boolean "+c+ "found")
      BoolVal(c)
    case FloatLit(c) =>
      trace("Float"+c+ "found")
      FloatVal(c)
    case StringLit(c) =>
      trace("String \""+c+ "\" found")
      StringVal(c)
    case VarExp(x) =>
      trace(s"Variable $x found, lookup of variable value in environment gave "+venv(x))
      venv.getOrElse(x, throw new InterpreterError(s"Unknown identifier '$x'", e))
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val leftval = eval(leftexp, venv, fenv)
      val rightval = eval(rightexp, venv, fenv)
      op match {
        case PlusBinOp() => trace("Adding expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a+b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a+b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a+b)
            case (FloatVal(a),FloatVal(b)) => FloatVal(a+b)
            case (StringVal(a),StringVal(b)) => StringVal(a+b)
            case (StringVal(a),IntVal(b)) => StringVal(a+b)
            case (StringVal(a),FloatVal(b)) => StringVal(a+b)
            case (IntVal(a),StringVal(b)) => StringVal(a+b)
            case (FloatVal(a),StringVal(b)) => StringVal(a+b)
            case _ => throw new InterpreterError("Illegal addition",e)
        }
        case MinusBinOp() =>
          trace("Subtracting expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a-b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a-b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a-b)
            case (FloatVal(a),FloatVal(b)) => FloatVal(a-b)
            case _ => throw new InterpreterError("Illegal subtraction",e)
          }
        case MultBinOp() =>
          trace("Multiplying expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a*b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a*b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a*b)
            case (FloatVal(a),FloatVal(b)) => FloatVal(a*b)
            case _ => throw new InterpreterError("Illegal multiplication",e)
          }
        case DivBinOp() =>
          if (rightval == IntVal(0) || rightval == FloatVal(0.0f))
            throw new InterpreterError(s"Division by zero", op)
          trace("Dividing expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a/b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a/b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a/b)
            case (FloatVal(a),FloatVal(b)) => FloatVal(a/b)
            case _ => throw new InterpreterError("Illegal division",e)
          }
        case ModuloBinOp() =>
          if(rightval == IntVal(0) || rightval == FloatVal(0.0f)){throw new InterpreterError("Modulo by zero",op)}
          trace("Calculating modulo")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a%b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a%b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a%b)
            case (FloatVal(a),FloatVal(b)) => FloatVal(a%b)
            case _ => throw new InterpreterError("Illegal modulation",e)
          }
        case MaxBinOp() =>
          trace("Finding max of expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => if(a>b){IntVal(a)}else{IntVal(b)}
            case (FloatVal(a),IntVal(b)) => if(a>b){FloatVal(a)}else{FloatVal(b)}
            case (IntVal(a),FloatVal(b)) => if(a>b){FloatVal(a)}else{FloatVal(b)}
            case (FloatVal(a),FloatVal(b)) => if(a>b){FloatVal(a)}else{FloatVal(b)}
            case _ => throw new InterpreterError("Illegal maksium",e)
          }
        case EqualBinOp()=>
          trace("Evaluating equal")
          (leftval,rightval) match {
            case(IntVal(a),IntVal(b)) => BoolVal(a==b)
            case(FloatVal(a),IntVal(b))=> BoolVal(a==b)
            case(IntVal(a),FloatVal(b))=> BoolVal(a==b)
            case(FloatVal(a),FloatVal(b))=> BoolVal(a==b)
            case(StringVal(a),StringVal(b))=> BoolVal(a==b)
            case(BoolVal(a),BoolVal(b)) => BoolVal(a==b)
            case(TupleVal(a),TupleVal(b)) => BoolVal(a==b)
            case _=> BoolVal(false)
          }
        case LessThanBinOp()=>
          trace("Evaluating less than")
          (leftval,rightval) match {
            case (IntVal(a),IntVal(b)) => BoolVal(a<b)
            case(FloatVal(a),IntVal(b))=> BoolVal(a<b)
            case(IntVal(a),FloatVal(b))=> BoolVal(a<b)
            case (FloatVal(a),FloatVal(b))=> BoolVal(a<b)
            case _=> throw new InterpreterError("Illegal less than operation",op)
          }
        case LessThanOrEqualBinOp()=>
          trace("Evaluating less than or equal")
          (leftval,rightval) match {
            case(IntVal(a),IntVal(b)) => BoolVal(a<=b)
            case(FloatVal(a),IntVal(b))=> BoolVal(a<=b)
            case(IntVal(a),FloatVal(b))=> BoolVal(a<=b)
            case(FloatVal(a),FloatVal(b))=> BoolVal(a<=b)
            case _=> throw new InterpreterError("Illegal 'less than or equal' operation",op)
          }
        case AndBinOp()=>
          trace("Evaluating less than or equal")
          (leftval,rightval) match {
            case (BoolVal(a),BoolVal(b)) => BoolVal(a&b)
            case _=> throw new InterpreterError("Illegal 'and' operation",op)
          }
        case OrBinOp()=>
          trace("Evaluating less than or equal")
          (leftval,rightval) match {
            case (BoolVal(a),BoolVal(b)) => BoolVal(a|b)
            case _=> throw new InterpreterError("Illegal 'and' operation",op)
          }
      }
    case UnOpExp(op, exp) =>
      trace("Unary expression found")
      val expval = eval(exp, venv, fenv)
      op match {
        case NegUnOp() =>
          trace("Negation of number")
          expval match{
            case IntVal(a) => IntVal(-a)
            case FloatVal(a) => FloatVal(-a)
            case _ => throw new InterpreterError("Not a number",e)
          }
        case NotUnOp() =>
          trace("Negation of Boolean")
          expval match{
            case BoolVal(a) => BoolVal(!a)
            case _ => throw new InterpreterError("Not a Boolean",e)
          }
      }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      eval(condexp,venv,fenv) match {
        case BoolVal(a) =>
          trace("If statement found, evaluating condition")
          if (a) {
            trace("evaluating then clause")
            return eval(thenexp, venv, fenv)
          }
          else trace("evaluationg else clause")
          eval(elseexp, venv, fenv)
        case _ => throw new InterpreterError("Condition clause not a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, defs, exp) =>
      var venv1 = venv
      var fenv1 = fenv
      trace("Calculating variable values and adding to variable environment")
      for (d <- vals) {
        val dexp = eval(d.exp,venv1,fenv)
        checkValueType(dexp, d.opttype, d)
        venv1 += (d.x -> dexp)
      }
      for (d <- defs){
        /**val dexp = eval(d.body,venv1,fenv)
          *checkValueType(dexp, d.optrestype, d)
          */
        fenv1 += (d.fun -> Closure(d.params,d.optrestype,d.body,venv1,fenv1,defs))
      }
      /** BlockExp(vals: List[ValDecl], defs: List[DefDecl], exp: Exp)
        * DefDecl(fun: Fun, params: List[FunParam], optrestype: Option[Type], body: Exp)
        * case class Closure(params: List[FunParam], optrestype (t2): Option[Type], body: Exp, venv: VarEnv, fenv: FunEnv, defs: List[DefDecl])
        * case class CallExp(fun: Fun, args: List[Exp]) extends Exp
        * type FunEnv = Map[Fun, Closure]
        * type VarEnv = Map[Var, Val]
        * case class FunParam(x: Var, opttype (t1): Option[Type]) extends AstNode
        */
      eval(exp, venv1, fenv1)
    case TupleExp(exps) =>
      trace("Evaluation tuple of expressions")
      var vals = List[Val]()
      for (ex <- exps)
        vals = eval(ex, venv, fenv) :: vals
      TupleVal(vals.reverse)
    case MatchExp(exp, cases) =>
      trace("Updating ")
      val expval = eval(exp, venv, fenv )
      expval match {
        case TupleVal(vs) =>
          for (c <- cases) {
            if (vs.length == c.pattern.length) {
              val venv_update = c.pattern.zip(vs)
              return eval(c.exp,venv++venv_update,fenv)
            }
          }
          throw new InterpreterError(s"No case matches value ${valueToString(expval)}", e)
        case _ => throw new InterpreterError(s"Tuple expected at match, found ${valueToString(expval)}", e)
      }
    case CallExp(fun, args) =>
      val close = fenv(fun)
      if(args.length == close.params.length){
        for(i <- args.indices){
          checkValueType(eval(args(i),venv,fenv),close.params(i).opttype,CallExp(fun,args))
        }
        def halp(fp: FunParam): Var = fp.x
        val venv_update = close.params.map(halp).zip(args.map(exp => eval(exp,venv,fenv)))
        var fenv_updated = close.fenv
        for(d <- close.defs){
          fenv_updated += (d.fun -> Closure(d.params,d.optrestype,d.body,close.venv,close.fenv,close.defs))
        }
        val res = eval(close.body,close.venv++venv_update,fenv_updated)
        checkValueType(res,close.optrestype,CallExp(fun, args))
        return res
        } else throw new InterpreterError(fun+" failed due to mismatch of number of arguments",CallExp(fun, args))

      /** case class Closure(params: List[FunParam], optrestype: Option[Type], body: Exp, venv: VarEnv, fenv: FunEnv, defs: List[DefDecl])
        * DefDecl(fun: Fun, params: List[FunParam], optrestype: Option[Type], body: Exp) extends Decl
        * case class CallExp(fun: Fun, args: List[Exp]) extends Exp
        * type FunEnv = Map[Fun, Closure]
        * type VarEnv = Map[Var, Val]
        * case class FunParam(x: Var, opttype: Option[Type]) extends AstNode
        */
  }

  /**
    * Checks whether value `v` has type `ot` (if present), generates runtime type error otherwise.
    */
  def checkValueType(v: Val, ot: Option[Type], n: AstNode): Unit = ot match {
    case Some(t) =>
      (v, t) match {
        case (IntVal(_), IntType()) |
             (BoolVal(_), BoolType()) |
             (FloatVal(_), FloatType()) |
             (IntVal(_), FloatType()) |
             (StringVal(_), StringType()) => // do nothing
        case (TupleVal(vs), TupleType(ts)) if vs.length == ts.length =>
          for ((vi, ti) <- vs.zip(ts))
            checkValueType(vi, Some(ti), n)
        case _ =>
          throw new InterpreterError(s"Type mismatch: value ${valueToString(v)} does not match type ${unparse(t)}", n)
      }
    case None => // do nothing
  }

  /**
    * Converts a value to its string representation (for error messages).
    */
  def valueToString(v: Val): String = v match {
    case IntVal(c) => c.toString
    case FloatVal(c) => c.toString
    case BoolVal(c) => c.toString
    case StringVal(c) => c
    case TupleVal(vs) => vs.map(v => valueToString(v)).mkString("(", ",", ")")
  }

  /**
    * Builds an initial environment, with a value for each free variable in the program.
    */
  def makeInitialVarEnv(program: Exp): VarEnv = {
    var venv = Map[Var, Val]()
    for (x <- Vars.freeVars(program)) {
      print(s"Please provide an integer value for the variable $x: ")
      venv = venv + (x -> IntVal(StdIn.readInt()))
    }
    venv
  }

  /**
    * Prints message if option -trace is used.
    */
  def trace(msg: String): Unit =
    if (Options.trace)
      println(msg)


  /**
    * Exception thrown in case of MiniScala runtime errors.
    */
  class InterpreterError(msg: String, node: AstNode) extends MiniScalaError(s"Runtime error: $msg", node.pos)
}