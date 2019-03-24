package miniscala

import miniscala.Ast._

/**
  * Unparser for MiniScala.
  */
object Unparser {

  def unparse(n: AstNode): String = n match {
    /** simple expression cases */
    case IntLit(c) => c.toString
    case BoolLit(c) => c.toString
    case VarExp(x) => s"$x"
    case StringLit(c) => c.toString
    case FloatLit(c) => c.toString

    /** combined expression cases*/
    case LambdaExp(params,expr) =>
      params.map(unparse).mkString("("," ",")=>")+unparse(expr)
    case BinOpExp(leftexp, op, rightexp) =>
      val left = unparse(leftexp)
      val right = unparse(rightexp)
      val op1 = unparse(op)
      "("+left + op1 + right +")"
    case UnOpExp(op, exp) =>
      val op1 = unparse(op)
      val exp1 = unparse(exp)
      op1+"("+exp1+")"
    case BlockExp(vals,defs,exp) => /** BlockExp(List[ValDecl],List[DefDecl],Exp)*/
      var valString = ""
      var endTuborg = ""
      for(d <- vals){
        valString += "{ " + unparse(d) + ";"
        endTuborg = endTuborg+" }"
      }
      valString+unparse(exp)+endTuborg
    case IfThenElseExp(conditionexp,thenexp,elseexp) =>
      val condi = unparse(conditionexp)
      val thene = unparse(thenexp)
      val elsee = unparse(elseexp)
      "if( "+condi+" ) "+thene+" else "+elsee
    case MatchExp(expr,caseList) =>
      unparse(expr)+" match "+caseList.map(unparse).mkString("{",";","}")

    case TupleExp(exps) =>
      exps.map(unparse).mkString("(",",",")")
    case CallExp(fun,args) =>
      /**val funPrint = fun.toString*/
      unparse(fun)+args.map(unparse).mkString("(",")(",")") //curry


    /** operator cases */
    case PlusBinOp()=> "+"
    case MinusBinOp() => "-"
    case DivBinOp() => "/"
    case MultBinOp() => "*"
    case ModuloBinOp() => "%"
    case MaxBinOp() => "max"
    case AndBinOp() => "&"
    case OrBinOp() => "|"
    case EqualBinOp() => "=="
    case LessThanBinOp() => "<"
    case LessThanOrEqualBinOp() => "<="
    case NegUnOp() => "-"
    case NotUnOp() => "!"

    /** declarations */
    case ValDecl(x,opttype,expr) =>
      if(opttype.isDefined){
        s"val $x ; "+unparse(opttype.get)+" = "+unparse(expr)}
      else s"val $x = "+unparse(expr)

    case DefDecl(fun,params,optrestype,body) =>
      "def"+fun+"("+params.map(unparse).mkString(" "," ",")")


    /** types */
    case IntType() => "Int"
    case BoolType() => "Boolean"
    case FloatType() => "Float"
    case StringType() => "String"
    case TupleType(list) =>
      list.map(unparse).mkString("(",",",")")
    case MatchCase(vars,e) =>
      vars.mkString("(",",",") => ")+unparse(e)

    /** Function paramters*/
    case FunParam(x,opttype) =>
      if(opttype.isDefined){
        x.toString+":"+opttype.get}
      else {
        x.toString
      }

  }
}