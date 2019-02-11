package miniscala

import miniscala.Ast._

import scala.io.StdIn

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  type VarEnv = Map[Var, Int]

  def eval(e: Exp, venv: VarEnv): Int = e match {
    case IntLit(c) =>
      trace("Integer "+c+ " found")
      c
    case VarExp(x) =>
      trace("Variable found, lookup of variable value in environment")
      venv(x)
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val leftval = eval(leftexp, venv)
      val rightval = eval(rightexp, venv)
      op match {
        case PlusBinOp() =>
          trace("Adding expressions")
          leftval + rightval
        case MinusBinOp() =>
          trace("Subtracting expressions")
          leftval - rightval
        case MultBinOp() =>
          trace("Multiplying expressions")
          leftval*rightval
        case DivBinOp() =>
          if (rightval == 0)
            throw new InterpreterError(s"Division by zero", op)
          trace("Dividing expressions")
          leftval / rightval
        case ModuloBinOp() => if(rightval == 0)
          throw new InterpreterError("Modulo by zero",op)
          trace("Calculating modulo")
          leftval % rightval
        case MaxBinOp() =>
          trace("Finding max of expressions")
          if (leftval>=rightval) leftval else rightval

      }
    case UnOpExp(op, exp) =>
      trace("Unary expression found")
      val expval = eval(exp, venv)
      op match {
        case NegUnOp() =>
          trace("Negation of expression")
          -expval
      }
    case BlockExp(vals, exp) =>
      var venv1 = venv
      for (d <- vals)
        venv1 = venv1 + (d.x -> eval(d.exp, venv1))
        trace("Calculating variable values and adding to variable environment")
      eval(exp, venv1)
  }

  /**
    * Builds an initial environment, with a value for each free variable in the program.
    */
  def makeInitialVarEnv(program: Exp): VarEnv = {
    var venv = Map[Var, Int]()
    for (x <- Vars.freeVars(program)) {
      print(s"Please provide an integer value for the variable $x: ")
      venv = venv + (x -> StdIn.readInt())
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
    case ValDecl(x,exp) => ValDecl(x,simplify(exp))
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