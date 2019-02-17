package miniscala

import miniscala.Ast._

/**
  * Unparser for MiniScala.
  */
object Unparser {

  def unparse(n: AstNode): String = n match {
    case IntLit(c) => c.toString()
    case VarExp(x) => s"$x"
    case BinOpExp(leftexp, op, rightexp) =>
      val left = unparse(leftexp)
      val right = unparse(rightexp)
      val op1 = unparse(op)
      "("+left + op1 + right +")"
    case UnOpExp(op, exp) =>
      val op1 = unparse(op)
      val exp1 = unparse(exp)
      op1+"("+exp1+")"
    case PlusBinOp()=>
      "+"
    case MinusBinOp() =>
      "-"
    case DivBinOp() =>
      "/"
    case MultBinOp() =>
      "*"
    case ModuloBinOp() =>
      "%"
    case MaxBinOp() =>
      "max"
    case NegUnOp() =>
      "-"
    /** BlockExp(List[ValDecl],Exp)*/
    case BlockExp(vals,exp) =>
      var valString = ""
      var endTuborg = ""
      for(d <- vals){
          /** ValDecl(Var,Exp) where Var is a string*/
          valString = valString + "{ val "+d.x+" = "+unparse(d.exp)+" ; "
          endTuborg = endTuborg+" }"
      }
      valString+unparse(exp)+endTuborg
  }
}
