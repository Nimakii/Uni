package miniscala

import miniscala.A7._
import miniscala.Ast._

/**
  * Computation of free variables (or rather, identifiers).
  */
object Vars {

  def freeVars(e: Exp): Set2[Id] = e match {
    case _: Literal => makeEmpty[Id]()
    case VarExp(x) => add(makeEmpty[Id](),x)
    case BinOpExp(leftexp, _, rightexp) => union(freeVars(leftexp),freeVars(rightexp))
    case UnOpExp(_, exp) => freeVars(exp)
    case IfThenElseExp(condexp, thenexp, elseexp) => union(union( freeVars(condexp), freeVars(thenexp) ), freeVars(elseexp))
    case BlockExp(vals, defs, exp) =>
      var fv = freeVars(exp)
      for (d <- defs)
        fv = union(fv,freeVars(d))
      for (d <- defs)
        fv = difference(fv,declaredVars(d))
      for (d <- vals.reverse)
        fv = union(difference(fv,declaredVars(d)),freeVars(d))
      fv
    case TupleExp(exps) =>
      var fv = makeEmpty[Id]()
      for (exp <- exps)
        fv = union(fv,freeVars(exp))
      fv
    case MatchExp(exp, cases) =>
      var fv = freeVars(exp)
      for (c <- cases){
        fv = union(fv,difference(freeVars(c.exp),listToSet2[Id](c.pattern)))
      }
      fv
    case CallExp(funexp, args) =>
      var fv = makeEmpty[Id]()
      for (a <- args)
        fv = union(fv,freeVars(a))
      fv
    case LambdaExp(params, body) =>
      difference(freeVars(body),listToSet2(params.map(p => p.x)))
  }

  def freeVars(decl: Decl): Set2[Id] = decl match {
    case ValDecl(_, _, exp) => freeVars(exp)
    case DefDecl(_, params, _, body) => difference(freeVars(body),listToSet2(params.map(p => p.x)))
  }

  def declaredVars(decl: Decl): Set2[Id] = decl match {
    case ValDecl(x, _, _) => add(makeEmpty[Id](),x)
    case DefDecl(x, _, _, _) => add(makeEmpty[Id](),x)
  }


}