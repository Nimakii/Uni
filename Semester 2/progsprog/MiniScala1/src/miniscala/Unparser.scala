package miniscala

import miniscala.Ast._

/**
  * Unparser for MiniScala.
  */
object Unparser {

  def unparse(n: AstNode): String = n match {
    case IntLit(c) => c.toString()
    case BinOpExp(leftexp, op, rightexp) =>
      val leftString = unparse(leftexp)
      val rightString = unparse(rightexp)
      op match {
        case PlusBinOp() => leftString+"+"+rightString
        case MinusBinOp() => "("+leftString+")-("+rightString+")"
        case MultBinOp() => "("+leftString+")*("+rightString+")"
        case DivBinOp() => "("+leftString+")/("+rightString+")"
        case ModuloBinOp() => "("+leftString+")%("+rightString+")"
        case MaxBinOp() => "("+leftString+")max("+rightString+")"
      }
    case UnOpExp(op,exp) =>
      val expString = unparse(exp)
      op match{
        case NegUnOp() => "-("+expString+")"
      }
  }
}
