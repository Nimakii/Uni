package miniscala

import miniscala.AbstractMachine._
import miniscala.Ast._

object Compiler {

  def compile(e: Exp): Executable = {

    case class IdDesc(x: Id, mutable: Boolean)

    def lookup(x: Id, idstack: List[IdDesc]): (IdIndex, Boolean) = {
      // find the position of identifier x in idstack
      val index = idstack.indexWhere(p => p.x == x)
      if (index == -1) throw new Exception(s"$x not found")
      // return the position and a boolean flag that indicates whether the identifier was declared with 'var'
      (index, idstack(index).mutable)
    }

    def compileFun(params: List[FunParam], body: Exp, freeids: List[Id], defs: List[DefDecl], idstack: List[IdDesc]) = {
      // prepare the new idstack for the function body, with an entry for each free non-def identifier, each def, and each parameter
      val defids = defs.map(d => d.fun).toSet
      val freenondefs = freeids.filterNot(defids.contains)
      val freeidsstack = freenondefs.map(x => IdDesc(x, lookup(x, idstack)._2))
      val defsstack = defs.map(d => IdDesc(d.fun, mutable = false))
      val paramsstack = params.map(p => IdDesc(p.x, mutable = false))
      // compile the function body
      val bodycode = compile(body, freeidsstack ++ defsstack ++ paramsstack , false) ++ List(Return)
      // find idstack index for each free identifier (excluding defs in same block)
      val indices = freenondefs.map(x => lookup(x, idstack)._1)
      // produce a Lambda instruction
      List(Lambda(indices, bodycode))
    }

    def compile(e: Exp, idstack: List[IdDesc], tailpos: Boolean): List[Instruction] =
      e match {
        case IntLit(c) =>
          List(Const(c))
        case BoolLit(c) =>
          if(c){List(Const(1))}
          else List(Const(0))
        case BinOpExp(leftexp, op, rightexp) =>
          compile(leftexp, idstack, false) ++ compile(rightexp, idstack, false) ++ List(op match {
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
          compile(exp, idstack, false) ++ List(op match {
            case NegUnOp() => Neg
            case NotUnOp() => Not
          })
        case IfThenElseExp(condexp, thenexp, elseexp) =>
          compile(condexp, idstack,false) ++
            List(Branch(compile(thenexp, idstack,tailpos), compile(elseexp, idstack,tailpos)))
        /**case BlockExp(vals, _, _,_, expz) =>
          var res = List[Instruction]()
          var idztack: List[IdDesc] = idstack
          for(v<- vals) {
            res = res ++ compile(v.exp, idztack,false) ++ List(Enter)
            idztack = v.x :: idztack
          }
          for(e<- expz){
            res = res++compile(e,idztack)
          }
          res ++ List(Exit(vals.size)) */
          //ValDecl(id,opttype,exp)
          //BlockExp(List(valdecl),list(vardecl),list(defs),list(exps))
        case WhileExp(cond, body) =>
          List(Loop(compile(cond, idstack, false), compile(body, idstack, false) ++ List(Pop)), Unit)
        case BlockExp(vals, vars, defs,_, exps) =>
          var res = List[Instruction]()
          var idztack: List[IdDesc] = idstack
          for(v<- vals) {
            res = res ++ compile(v.exp, idztack,false) ++ List(Enter)
            idztack = IdDesc(v.x,false) :: idztack
          }
          for(v<- vars){
            res = res ++List(Alloc,Dup)++compile(v.exp,idztack,false) ++ List(Store,Enter)
            idztack = IdDesc(v.x,true) :: idztack
          }
          for(d<- defs){
            //Lambda(freeids: List[IdIndex], body: List[Instruction])
            //def compileFun(params: List[FunParam], body: Exp,
            // freeids: List[Id], defs: List[DefDecl], idstack: List[IdDesc])
            res = res ++ compileFun(d.params,d.body,Vars.freeVars(d.body).toList.sorted,defs,idztack)
            idztack = IdDesc(d.fun,false) :: idztack
          }
          res = res ++ List(EnterDefs(defs.size))
          if(exps.nonEmpty) {
            for (i <- 0 to exps.size - 2) {
              res = res ++ compile(exps(i), idztack, false) ++ List(Pop)
            }
            res = res ++ compile(exps.last, idztack, tailpos)
          }
          else res ++ List(Unit)
          res ++ List(Exit(vals.size+vars.size+defs.size))
        case VarExp(x) => //Lookup of a variable expression
          val look = lookup(x,idstack)
          if(look._2){
            return List(Read(look._1),Load)
          }
          List(Read(look._1))
        case AssignmentExp(x, exp) => //reassignment of a mutable variable
          val look = lookup(x,idstack)
          //if (!look._2){
            //throw new CompilerError(e)
          //}
          List(Read(look._1))++compile(exp,idstack,look._2)++List(Store,Unit)
        case LambdaExp(params, body) =>
          compileFun(params, body, Vars.freeVars(e).toList.sorted, List(), idstack)
        case CallExp(funexp, args) =>
          // compile funexp and args, and then add a Call instruction
          compile(funexp, idstack, false) ++ args.flatMap(arg => compile(arg, idstack, false)) ++ List(Call(args.length, tailpos))
        case _ => throw new CompilerError(e)
      }

    val freeids = Vars.freeVars(e).toList.sorted
    Executable(freeids, compile(e, freeids.map(x => IdDesc(x, mutable = false)), true))
  }

  class CompilerError(node: AstNode) extends MiniScalaError(s"Sorry, I don't know how to compile $node", node.pos)
}
