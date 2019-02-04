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
        case MinusBinOp() => leftString+parenthesize("","-",rightString,1)
        case MultBinOp() => parenthesize(leftString,"*",rightString,2)
        case DivBinOp() => parenthesize(leftString,"/",rightString,2)
        case ModuloBinOp() => parenthesize(leftString,"%",rightString,2)
        case MaxBinOp() => parenthesize(leftString,"max",rightString,2)
      }
    case UnOpExp(op,exp) =>
      val expString = unparse(exp)
      op match{
        case NegUnOp() => parenthesize("","-",expString,1)
      }
  }
  private def parenthesize(leftString: String, op: String, rightString: String, option: Int): String = {
    var parLeftString = ""
    var parRightString = rightString
    if(option == 2) {
      try {
        leftString.toInt
        parLeftString = leftString
      }
      catch {
        case e: Exception => parLeftString = "(" + leftString + ")"
      }
    }
    try {
      rightString.toInt
    }
    catch{
      case e: Exception => parRightString = "("+rightString+")"
    }
    parLeftString + op + parRightString
  }
}
