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
    case VarExp(x) => venv(x)
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

  /**
    * Exception thrown in case of MiniScala runtime errors.
    */
  class InterpreterError(msg: String, node: AstNode) extends MiniScalaError(s"Runtime error: $msg", node.pos)
}