package miniscala
import miniscala.TypeChecker._
import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.io.StdIn

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  type VarEnv = Map[Var, Val]
  sealed abstract class Val
  case class IntVal(v: Int) extends Val
  case class BoolVal(v: Boolean) extends Val
  case class FloatVal(v: Float) extends Val
  case class StringVal(v: String) extends Val
  case class TupleVal(vs: List[Val]) extends Val

  def eval(e: Exp, env: VarEnv): Val = e match {
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
            case _ => throw new InterpreterError("Illegal subtraction",e)
          }
        case MultBinOp() =>
          trace("Multiplying expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a*b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a*b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a*b)
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
            case _ => throw new InterpreterError("Illegal division",e)
          }
        case ModuloBinOp() =>
          if(rightval == IntVal(0) || rightval == FloatVal(0.0f)){throw new InterpreterError("Modulo by zero",op)}
          trace("Calculating modulo")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => IntVal(a%b)
            case (FloatVal(a),IntVal(b)) => FloatVal(a%b)
            case (IntVal(a),FloatVal(b)) => FloatVal(a%b)
            case _ => throw new InterpreterError("Illegal modulation",e)
          }
        case MaxBinOp() =>
          trace("Finding max of expressions")
          (leftval,rightval) match{
            case (IntVal(a),IntVal(b)) => if(a>b){IntVal(a)}else{IntVal(b)}
            case (FloatVal(a),IntVal(b)) => if(a>b){FloatVal(a)}else{IntVal(b)}
            case (IntVal(a),FloatVal(b)) => if(a>b){IntVal(a)}else{FloatVal(b)}
            case _ => throw new InterpreterError("Illegal maksium",e)
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
            eval(thenexp, env)
          }
          else trace("evaluationg else clause")
          eval(elseexp, env)
        case _ => throw new InterpreterError("Condition clause not a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, exp) =>
      var env1 = env
      trace("Calculating variable values and adding to variable environment")
      for (d <- vals) {
        val dexp = eval(d.exp,env1)
        checkValueType(dexp, d.opttype, d)
        env1 += (d.x -> dexp)
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

  import scala.collection.mutable.ListBuffer
  def simplifyDecl(vd: ValDecl): ValDecl = vd match{
    case ValDecl(x,o,exp) =>
      ValDecl(x,o,simplify(exp))
  }
  def simplify(exp: Exp): Exp = {
    var expNew = exp
    while(expNew != simplify1(expNew)) {
      expNew = simplify1(expNew)
    }
    expNew
  }
  def simplify1(exp: Exp): Exp =
    exp match{
    case IntLit(c)=> IntLit(c)
    case VarExp(x)=> VarExp(x)
    case UnOpExp(op,e)=> UnOpExp(op,simplify(e))
    case BlockExp(vals,e)=>{
      var vals1 = new ListBuffer[ValDecl]()
      for (v <- vals){
        vals1 += simplifyDecl(v)
      }
      val vals2 = vals1.toList
      BlockExp(vals2,simplify(e))
    }
     case BinOpExp(IntLit(m),ModuloBinOp(),IntLit(n)) =>
       if ((0 <= m) && (m < n)) IntLit(m)
       else BinOpExp(IntLit(m), ModuloBinOp(), IntLit(n))
     case BinOpExp(IntLit(m),MultBinOp(),IntLit(n)) =>
       if ((m < 0) && (n < 0)) BinOpExp(IntLit(-m), MultBinOp(), IntLit(-n))
       else if (m < 0) UnOpExp(NegUnOp(), BinOpExp(IntLit(-m), MultBinOp(), IntLit(n)))
       else if (n < 0) UnOpExp(NegUnOp(), BinOpExp(IntLit(m), MultBinOp(), IntLit(-n)))
       else if (n == 1) IntLit(m)
       else if (m == 1) IntLit(n)
       else if ((n == 0) || (m == 0)) IntLit(0)
       else BinOpExp(IntLit(m), MultBinOp(), IntLit(n))
    case BinOpExp(IntLit(m),MaxBinOp(),IntLit(n)) =>
      if (m == n) IntLit(m)
      else BinOpExp(IntLit(m), MaxBinOp(), IntLit(n))
    case BinOpExp(le, op, re) => op match {
      case PlusBinOp() =>
        if(le == IntLit(0)) simplify(re)
        else if(re == IntLit(0)) simplify(le)
        else BinOpExp(simplify(le),op,simplify(re))
      case MinusBinOp() =>
        if(le == re) IntLit(0)
        else if (le == IntLit(0)) UnOpExp(NegUnOp(),simplify(re))
        else re match {
          case IntLit(m) =>{
            if (m<0) BinOpExp(simplify(le),PlusBinOp(),IntLit(-m))
            else BinOpExp(simplify(le),op,simplify(re))
          }
          BinOpExp(simplify(le),op,simplify(re))
        }
      case MultBinOp() =>
        if(le == IntLit(1)) simplify(re)
        else if(re == IntLit(1)) simplify(le)
        else if((le == IntLit(0))||(re == IntLit(0))) IntLit(0)
        else BinOpExp(simplify(le),op,simplify(re))
      case DivBinOp() =>
        if(le == IntLit(0)) IntLit(0)
        else if(re == IntLit(0)) throw new IllegalArgumentException("Division by zero")
        else if(le == re) IntLit(1)
        else BinOpExp(simplify(le),op,simplify(re))
      case ModuloBinOp() =>
        if(re == IntLit(0)) throw new IllegalArgumentException("Modulation by zero")
        else BinOpExp(simplify(le),op,simplify(re))
      case MaxBinOp() => BinOpExp(simplify(le),op,simplify(re))
    }
  }

  /**
    * Exception thrown in case of MiniScala runtime errors.
    */
  class InterpreterError(msg: String, node: AstNode) extends MiniScalaError(s"Runtime error: $msg", node.pos)
}