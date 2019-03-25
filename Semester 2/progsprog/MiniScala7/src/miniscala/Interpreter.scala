package miniscala
import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.io.StdIn

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  type Env = Map[Id, Val]
  sealed abstract class Val
  case class IntVal(v: Int) extends Val
  case class BoolVal(v: Boolean) extends Val
  case class FloatVal(v: Float) extends Val
  case class StringVal(v: String) extends Val
  case class TupleVal(vs: List[Val]) extends Val
  case class ClosureVal(params: List[FunParam], optrestype: Option[Type], body: Exp, env: Env, defs: List[DefDecl]) extends Val


  def eval(e: Exp, env: Env): Val = e match {
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
      trace(s"Variable $x found, lookup of variable value in environment gave "+env(x))
      env.getOrElse(x, throw new InterpreterError(s"Unknown identifier '$x'", e))
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val leftval = eval(leftexp, env)
      val rightval = eval(rightexp, env)
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
            case (ClosureVal(_,_,_,_,_),ClosureVal(_,_,_,_,_))=> throw new InterpreterError("This is right here it goes wrong",e)
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
      val expval = eval(exp, env)
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
      eval(condexp,env) match {
        case BoolVal(a) =>
          trace("If statement found, evaluating condition")
          if (a) {
            trace("evaluating then clause")
            return eval(thenexp,env)
          }
          else trace("evaluationg else clause")
          eval(elseexp, env)
        case _ => throw new InterpreterError("Condition clause not a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, defs, exp) =>
      var env1 = env
      trace("Calculating variable values and adding to variable environment")
      for (d <- vals) {
        val dexp = eval(d.exp,env1)
        checkValueType(dexp, d.opttype, d)
        env1 += (d.x -> dexp)
      }
      for (d <- defs){
         env1 += (d.fun -> ClosureVal(d.params,d.optrestype,d.body,env1,defs) )
      }
      eval(exp, env1)
    case TupleExp(exps) =>
      trace("Evaluation tuple of expressions")
      var vals = List[Val]()
      for (ex <- exps)
        vals = eval(ex, env) :: vals
      TupleVal(vals.reverse)
    case MatchExp(exp, cases) =>
      trace("Updating ")
      val expval = eval(exp, env)
      expval match {
        case TupleVal(vs) =>
          for (c <- cases) {
            if (vs.length == c.pattern.length) {
              val venv_update = c.pattern.zip(vs)
              return eval(c.exp,env++venv_update)
            }
          }
          throw new InterpreterError(s"No case matches value ${valueToString(expval)}", e)
        case _ => throw new InterpreterError(s"Tuple expected at match, found ${valueToString(expval)}", e)
      }
    case LambdaExp(params, body) =>
      ClosureVal(params,None,body,env,List[DefDecl]())



    case CallExp(funexp, args) =>
      eval(funexp,env) match{
        case ClosureVal(params,optrestype,body,cenv,defs) =>
          if(args.length == params.length){
            def halp (fp:FunParam) : Id = fp.x
            var cenv_updated = cenv
            for(i <- args.indices) {
              val argval = eval(args(i), env)
              checkValueType(argval, params(i).opttype, CallExp(funexp, args))
              cenv_updated +=  (halp(params(i)) -> argval)
              }
            for(d <- defs){
              cenv_updated += (d.fun -> ClosureVal(d.params,d.optrestype,d.body,cenv,defs))

            }
            val res = eval(body,cenv_updated)
            checkValueType(res,optrestype,CallExp(funexp,args))
             return res
          }
          else
            println("hello")
            throw new InterpreterError(s"Wrong number of arguments",CallExp(funexp,args))
        case _ =>
          throw new InterpreterError(s"Not a function",funexp)

      }


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
        case (ClosureVal(cparams, optcrestype, _, _,_), FunType(paramtypes, restype)) if cparams.length == paramtypes.length =>
          for ((p, t) <- cparams.zip(paramtypes))
            checkTypesEqual(t, p.opttype, n)
          checkTypesEqual(restype, optcrestype, n)
        case _ =>
          throw new InterpreterError(s"Type mismatch: value ${valueToString(v)} does not match type ${unparse(t)}", n)
      }
    case None => // do nothing
  }

  /**
    * Checks that the types `t1` and `ot2` are equal (if present), throws type error exception otherwise.
    */
  def checkTypesEqual(t1: Type, ot2: Option[Type], n: AstNode): Unit = ot2 match {
    case Some(t2) =>
      if (t1 != t2)
        throw new InterpreterError(s"Type mismatch: type ${unparse(t1)} does not match expected type ${unparse(t2)}", n)
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
    case ClosureVal(params, _, exp,_,_) => // the resulting string ignores the result type annotation and the declaration environment
      s"<(${params.map(p => unparse(p)).mkString(",")}), ${unparse(exp)}>"
  }

  /**
    * Builds an initial environment, with a value for each free variable in the program.
    */
  def makeInitialEnv(program: Exp): Env = {
    var env = Map[Id, Val]()
    for (x <- Vars.freeVars(program)) {
      print(s"Please provide an integer value for the variable $x: ")
      env = env + (x -> IntVal(StdIn.readInt()))
    }
    env
  }

  /**
    * Prints message if option -trace is used.
    */
  def trace(msg: => String): Unit =
    if (Options.trace)
      println(msg)



  /**
    * Exception thrown in case of MiniScala runtime errors.
    */
  class InterpreterError(msg: String, node: AstNode) extends MiniScalaError(s"Runtime error: $msg", node.pos)
}