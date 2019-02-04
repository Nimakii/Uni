package miniscala

import miniscala.Ast._

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  def eval(e: Exp): Int = e match {
    case IntLit(c) =>
      trace("Integer "+c+ " found")
      c
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val leftval = eval(leftexp)
      val rightval = eval(rightexp)
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
      val expval = eval(exp)
      op match {
        case NegUnOp() =>
          trace("Negation of expression")
          -expval
      }
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