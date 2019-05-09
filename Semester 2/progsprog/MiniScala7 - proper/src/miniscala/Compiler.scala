package miniscala

import miniscala.AbstractMachine._
import miniscala.Ast._

object Compiler {

  def compile(e: Exp): Executable = {

    def lookup(x: Id, idstack: List[Id]): IdIndex = {
      // find the position of identifier x in idstack
      val index = idstack.indexOf(x)
      if (index == -1) throw new Exception(s"$x not found")
      index
    }

    def compile(e: Exp, idstack: List[Id]): List[Instruction] =
      e match {
        case IntLit(c) =>
          List(Const(c))
        case BoolLit(c) =>
          if(c){List(Const(1))}
          else List(Const(0))
        case BinOpExp(leftexp, op, rightexp) =>
          compile(leftexp, idstack) ++ compile(rightexp, idstack) ++ List(op match {
            case PlusBinOp() => Add
            case MinusBinOp() => Sub
            case MultBinOp() => Mul
            case DivBinOp() => Div
            case EqualBinOp() => Eq
            case LessThanBinOp() => Lt
            case LessThanOrEqualBinOp() => Leq
            case AndBinOp() => And
            case OrBinOp() => Or
            case _ => throw new CompilerError(e)
          })
        case UnOpExp(op, exp) =>
          compile(exp, idstack) ++ List(op match {
            case NegUnOp() => Neg
            case NotUnOp() => Not
          })
        case IfThenElseExp(condexp, thenexp, elseexp) =>
          compile(condexp, idstack) ++ List(Branch(compile(thenexp, idstack), compile(elseexp, idstack)))
        case BlockExp(vals, _, _, expz) =>
          var res = List[Instruction]()
          var idztack: List[Ast.Id] = idstack
          for(v<- vals) {
            res = res ++ compile(v.exp, idztack) ++ List(Enter)
            idztack = v.x :: idztack
          }
          for(e<- expz){
            res = res++compile(e,idztack)
          }
          res ++ List(Exit(vals.size))
          //ValDecl(id,opttype,exp)
          //BlockExp(List(valdecl),list(vardecl),list(defs),list(exps))
        case VarExp(x) =>
          List(Read(lookup(x,idstack)))
        case _ => throw new CompilerError(e)
      }

    val freeids = Vars.freeVars(e).toList.sorted
    Executable(freeids, compile(e, freeids))
  }

  class CompilerError(node: AstNode) extends MiniScalaError(s"Sorry, I don't know how to compile $node", node.pos)
}
