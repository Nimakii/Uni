package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

/**
  * Type checker for MiniScala.
  */
object TypeChecker {

  type VarTypeEnv = Map[Var, Type]

  def typeCheck(e: Exp, vtenv: VarTypeEnv): Type = e match {
    case IntLit(_) => IntType()
    case BoolLit(_) => BoolType()
    case FloatLit(_) => FloatType()
    case StringLit(_) => StringType()
    case VarExp(x) => vtenv.getOrElse(x, throw new TypeError(s"Unknown identifier '$x'", e))
    case BinOpExp(leftexp, op, rightexp) =>
      val lefttype = typeCheck(leftexp, vtenv)
      val righttype = typeCheck(rightexp, vtenv)
      op match {
        case PlusBinOp() =>
          (lefttype, righttype) match {
            case (IntType(), IntType()) => IntType()
            case (FloatType(), FloatType()) => FloatType()
            case (IntType(), FloatType()) => FloatType()
            case (FloatType(), IntType()) => FloatType()
            case (StringType(), StringType()) => StringType()
            case (StringType(), IntType()) => StringType()
            case (StringType(), FloatType()) => StringType()
            case (IntType(), StringType()) => StringType()
            case (FloatType(), StringType()) => StringType()
            case _ => throw new TypeError(s"Type mismatch at '+', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
        case MinusBinOp() | MultBinOp() | DivBinOp() | ModuloBinOp() | MaxBinOp() => (lefttype, righttype) match {
          case (IntType(), IntType()) => IntType()
          case (FloatType(), FloatType()) => FloatType()
          case (IntType(), FloatType()) => FloatType()
          case (FloatType(), IntType()) => FloatType()
          case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
        }
        case EqualBinOp() => BoolType()
        case LessThanBinOp() | LessThanOrEqualBinOp() =>
          (lefttype, righttype) match{
            case (IntType(),IntType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
        case AndBinOp() | OrBinOp() =>
          (lefttype, righttype) match{
            case (BoolType(),BoolType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
      }
    case UnOpExp(op, exp) => op match{
      case NegUnOp() => typeCheck(exp,vtenv) match{
        case IntType() => IntType()
        case FloatType() => FloatType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,vtenv))}}", op)
      }
      case NotUnOp() => typeCheck(exp,vtenv) match{
        case BoolType() => BoolType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,vtenv))}}", op)
      }
    }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      val ce = typeCheck(condexp,vtenv)
      val te = typeCheck(thenexp,vtenv)
      val ee = typeCheck(elseexp,vtenv)
      (ce,te,ee) match{
        case (BoolType(),IntType(),IntType()) => IntType()
        case (BoolType(),FloatType(),FloatType()) => FloatType()
        case (BoolType(),StringType(),StringType()) => StringType()
        case _ => throw new TypeError(s"Type mismatch at If statement, unexpected type either in the condition ${unparse(ce)} or in the inner expressions that must be of the same type ${unparse(te)} = ${unparse(ee)}", IfThenElseExp())
      }
    case BlockExp(vals, exp) =>
      var vtenv1 = vtenv
      for (d <- vals) {
        val t = typeCheck(d.exp, vtenv1)
        checkTypesEqual(t, d.opttype, d)
        vtenv1 = vtenv1 + (d.x -> d.opttype.getOrElse(t))
      }
      ???
    case TupleExp(exps) => TupleType(???)
    case MatchExp(exp, cases) =>
      val exptype = typeCheck(exp, vtenv)
      exptype match {
        case TupleType(ts) =>
          for (c <- cases) {
            if (ts.length == c.pattern.length) {
              ???
            }
          }
          throw new TypeError(s"No case matches type ${unparse(exptype)}", e)
        case _ => throw new TypeError(s"Tuple expected at match, found ${unparse(exptype)}", e)
      }
  }

  /**
    * Checks that the types `t1` and `ot2` are equal (if present), throws type error exception otherwise.
    */
  def checkTypesEqual(t1: Type, ot2: Option[Type], n: AstNode): Unit = ot2 match {
    case Some(t2) =>
      if (t1 != t2)
        throw new TypeError(s"Type mismatch: expected type ${unparse(t2)}, found type ${unparse(t1)}", n)
    case None => // do nothing
  }

  /**
    * Builds an initial type environment, with a type for each free variable in the program.
    */
  def makeInitialVarTypeEnv(program: Exp): VarTypeEnv = {
    var vtenv: VarTypeEnv = Map()
    for (x <- Vars.freeVars(program))
      vtenv = vtenv + (x -> IntType())
    vtenv
  }

  /**
    * Exception thrown in case of MiniScala type errors.
    */
  class TypeError(msg: String, node: AstNode) extends MiniScalaError(s"Type error: $msg", node.pos)
}
