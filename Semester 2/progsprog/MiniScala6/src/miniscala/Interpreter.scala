package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.io.StdIn

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  sealed abstract class Val

  case class IntVal(v: Int) extends Val

  case class BoolVal(v: Boolean) extends Val

  case class FloatVal(v: Float) extends Val

  case class StringVal(v: String) extends Val

  case class TupleVal(vs: List[Val]) extends Val

  case class ClosureVal(params: List[FunParam], optrestype: Option[Type],
                        body: Exp, env: Env, defs: List[DefDecl]) extends Val

  case class RefVal(loc: Loc, opttype: Option[Type]) extends Val

  val unitVal = TupleVal(List[Val]())

  type Env = Map[Id, Val]

  type Sto = Map[Loc, Val]

  type Loc = Int

  def nextLoc(sto: Sto): Loc = sto.size

  def evaltest(e: Exp): (Val,Sto) = eval(e,Map[Id,Val](),Map[Loc,Val]())
  def eval(e: Exp, env: Env, sto: Sto): (Val, Sto) = e match {
    case IntLit(c) =>
      trace("Integer " + c + " found")
      (IntVal(c), sto)
    case BoolLit(c) =>
      trace("Boolean " + c + "found")
      (BoolVal(c), sto)
    case FloatLit(c) =>
      trace("Float" + c + "found")
      (FloatVal(c), sto)
    case StringLit(c) =>
      trace("String \"" + c + "\" found")
      (StringVal(c), sto)
    case VarExp(x) =>
      trace(s"Variable $x found, lookup of variable value in environment gave " + env(x))
      env.getOrElse(x, throw new InterpreterError(s"Unknown identifier '$x'", e)) match {
        case RefVal(loc, _) => (sto(loc), sto)
        case v: Val => (v, sto)
      }
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val (leftval, sto1) = eval(leftexp, env, sto)
      val (rightval, sto2) = eval(rightexp, env, sto1)
      op match {
        case PlusBinOp() => trace("Adding expressions")
          (leftval, rightval) match {
            case (IntVal(v1), IntVal(v2)) => (IntVal(v1 + v2), sto2)
            case (FloatVal(v1), FloatVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (IntVal(v1), FloatVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (FloatVal(v1), IntVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (StringVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case (StringVal(v1), IntVal(v2)) => (StringVal(v1 + v2), sto2)
            case (StringVal(v1), FloatVal(v2)) => (StringVal(v1 + v2), sto2)
            case (IntVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case (FloatVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case _ => throw new InterpreterError("Illegal addition", e)
          }
        case MinusBinOp() =>
          trace("Subtracting expressions")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (IntVal(a - b), sto2)
            case (FloatVal(a), IntVal(b)) => (FloatVal(a - b), sto2)
            case (IntVal(a), FloatVal(b)) => (FloatVal(a - b), sto2)
            case (FloatVal(a), FloatVal(b)) => (FloatVal(a - b), sto2)
            case _ => throw new InterpreterError("Illegal subtraction", e)
          }
        case MultBinOp() =>
          trace("Multiplying expressions")
          (leftval, rightval) match {
            case (IntVal(v1), IntVal(v2)) => (IntVal(v1 * v2), sto2)
            case (FloatVal(v1), FloatVal(v2)) => (FloatVal(v1 * v2), sto2)
            case (IntVal(v1), FloatVal(v2)) => (FloatVal(v1 * v2), sto2)
            case (FloatVal(v1), IntVal(v2)) => (FloatVal(v1 * v2), sto2)
            case _ => throw new InterpreterError("Illegal multiplication", e)
          }
        case DivBinOp() =>
          if (rightval == IntVal(0) || rightval == FloatVal(0.0f))
            throw new InterpreterError(s"Division by zero", op)
          trace("Dividing expressions")
          (leftval, rightval) match {
            case (IntVal(v1), IntVal(v2)) => (IntVal(v1 / v2), sto2)
            case (FloatVal(v1), FloatVal(v2)) => (FloatVal(v1 / v2), sto2)
            case (IntVal(v1), FloatVal(v2)) => (FloatVal(v1 / v2), sto2)
            case (FloatVal(v1), IntVal(v2)) => (FloatVal(v1 / v2), sto2)
            case _ => throw new InterpreterError("Illegal division", e)
          }
        case ModuloBinOp() =>
          if (rightval == IntVal(0) || rightval == FloatVal(0.0f)) {
            throw new InterpreterError("Modulo by zero", op)
          }
          trace("Calculating modulo")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (IntVal(a % b), sto2)
            case (FloatVal(a), IntVal(b)) => (FloatVal(a % b), sto2)
            case (IntVal(a), FloatVal(b)) => (FloatVal(a % b), sto2)
            case (FloatVal(a), FloatVal(b)) => (FloatVal(a % b), sto2)
            case _ => throw new InterpreterError("Illegal modulation", e)
          }
        case MaxBinOp() =>
          trace("Finding max of expressions")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => if (a > b) {
              (IntVal(a), sto2)
            } else {
              (IntVal(b), sto2)
            }
            case (FloatVal(a), IntVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case (IntVal(a), FloatVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case (FloatVal(a), FloatVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case _ => throw new InterpreterError("Illegal maksium", e)
          }
        case EqualBinOp() =>
          trace("Evaluating equal")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a == b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a == b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a == b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a == b), sto2)
            case (StringVal(a), StringVal(b)) => (BoolVal(a == b), sto2)
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a == b), sto2)
            case (TupleVal(a), TupleVal(b)) => (BoolVal(a == b), sto2)
            case _ => (BoolVal(false), sto2)
          }
        case LessThanBinOp() =>
          trace("Evaluating less than")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a < b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a < b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a < b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a < b), sto2)
            case _ => throw new InterpreterError("Illegal less than operation", op)
          }
        case LessThanOrEqualBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a <= b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a <= b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a <= b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a <= b), sto2)
            case _ => throw new InterpreterError("Illegal 'less than or equal' operation", op)
          }
        case AndBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a & b), sto2)
            case _ => throw new InterpreterError("Illegal 'and' operation", op)
          }
        case OrBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a | b), sto2)
            case _ => throw new InterpreterError("Illegal 'and' operation", op)
          }
      }
    case UnOpExp(op, exp) =>
      trace("Unary expression found")
      val (expval, sto1) = eval(exp, env, sto)
      op match {
        case NegUnOp() =>
          trace("Negation of number")
          expval match {
            case IntVal(v) => (IntVal(-v), sto1)
            case FloatVal(v) => (FloatVal(-v), sto1)
            case _ => throw new InterpreterError("Not a number", e)
          }
        case NotUnOp() =>
          trace("Negation of Boolean")
          expval match {
            case BoolVal(a) => (BoolVal(!a), sto1)
            case _ => throw new InterpreterError("Not a Boolean", e)
          }
      }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      eval(condexp, env, sto) match {
        case (BoolVal(a), st) =>
          trace("If statement found, evaluating condition")
          if (a) {
            trace("evaluating then clause")
            return eval(thenexp, env, st)
          }
          else trace("evaluationg else clause")
          eval(elseexp, env, st)
        case _ => throw new InterpreterError("Condition clause not a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, vars, defs, exps) =>
      var env1 = env
      var sto1 = sto
      for (d <- vals) {
        val (v, sto2) = eval(d.exp, env1, sto1)
        env1 = env1 + (d.x -> v)
        sto1 = sto2
      }
      for (d <- vars) {
        val (v, sto2) = eval(d.exp, env1, sto1)
        val loc = nextLoc(sto2)
        env1 = env1 + (d.x -> RefVal(loc, d.opttype))
        sto1 = sto2 + (loc -> v)
      }
      env1 = defs.foldLeft(env1)((en: Env, d: DefDecl) => {
        en + (d.fun -> ClosureVal(d.params, d.optrestype, d.body, en, defs))
      })
      var res: Val = unitVal
      //var env2 = env1
      for (exp <- exps) {
        val (res1, sto2) = eval(exp, env1, sto1) //env2 was used here ?
        res = res1
        sto1 = sto2
      }
      (res, sto1)
    case TupleExp(exps) =>
      var (vals, sto1) = (List[Val](), sto)
      for (exp <- exps) {
        val (v, sto2) = eval(exp, env, sto1)
        vals = v :: vals
        sto1 = sto2
      }
      (TupleVal(vals.reverse), sto1)
    case MatchExp(exp, cases) =>
      trace("Updating ")
      val (expval, sto1) = eval(exp, env, sto)
      expval match {
        case TupleVal(vs) =>
          for (c <- cases) {
            if (vs.length == c.pattern.length) {
              val venv_update = c.pattern.zip(vs)
              return eval(c.exp, env ++ venv_update,sto1)
            }
          }
          throw new InterpreterError(s"No case matches value ${valueToString(expval)}", e)
        case _ => throw new InterpreterError(s"Tuple expected at match, found ${valueToString(expval)}", e)
      }
    case LambdaExp(params, body) =>
      (ClosureVal(params, None, body, env, List[DefDecl]()),sto)
    case AssignmentExp(x, exp) =>
      val (v, sto1) = eval(exp, env, sto)
      env(x) match {
        case RefVal(loc,opttype) =>
          checkValueType(v,opttype,e)
          (unitVal,sto1 + (loc -> v))
        case _ => throw new InterpreterError("Not a var",e)
      }
    case WhileExp(cond, body) =>
      eval(cond,env,sto) match {
        case (BoolVal(true),st) => eval(WhileExp(cond,body),env,eval(body,env,st)._2)
        case (BoolVal(false),st) => (unitVal,st)
        case _ => throw new InterpreterError("Not a boolean",cond)
      }

    case CallExp(funexp, args) =>
      eval(funexp, env, sto) match {
        case (ClosureVal(params, optrestype, body, cenv, defs), sto1) =>
          if (args.length == params.length) {
            def halp(fp: FunParam): Id = fp.x
            var cenv_updated = cenv
            var sto2 = sto1
            for (i <- args.indices) {
              val evaluation = eval(args(i), env, sto2)
              val argval = evaluation._1
              sto2 = evaluation._2
              checkValueType(argval, params(i).opttype, CallExp(funexp, args))
              cenv_updated += (halp(params(i)) -> argval)
            }
            for (d <- defs) { //rebind function defs, to achieve mutual recursion
              cenv_updated += (d.fun -> ClosureVal(d.params, d.optrestype, d.body, cenv, defs))
            }
            val res = eval(body, cenv_updated,sto2)
            checkValueType(res._1, optrestype, CallExp(funexp, args))
            res
          } else throw new InterpreterError("Wrong number of arguments", CallExp(funexp, args))
        case _ =>
          throw new InterpreterError("Not a function", funexp)
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
        case (ClosureVal(cparams, optcrestype, _, _, _), FunType(paramtypes, restype)) if cparams.length == paramtypes.length =>
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
    case ClosureVal(params, _, exp, _, _) => // the resulting string ignores the result type annotation and the declaration environment
      s"<(${params.map(p => unparse(p)).mkString(",")}), ${unparse(exp)}>"
    case RefVal(loc, _) => s"#$loc" // the resulting string ignores the type annotation
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