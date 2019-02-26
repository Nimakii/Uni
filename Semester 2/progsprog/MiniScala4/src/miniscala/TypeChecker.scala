package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

/**
  * Type checker for MiniScala.
  */
object TypeChecker {

  type VarTypeEnv = Map[Var, Type]

  type FunTypeEnv = Map[Fun, (List[Type], Type)]

  def typeCheck(e: Exp, vtenv: VarTypeEnv, ftenv: FunTypeEnv): Type = e match {
    case IntLit(_) => IntType()
    case BoolLit(_) => BoolType()
    case FloatLit(_) => FloatType()
    case StringLit(_) => StringType()
    case VarExp(x) => vtenv.getOrElse(x, throw new TypeError(s"Unknown identifier '$x'", e))
    case BinOpExp(leftexp, op, rightexp) =>
      val lefttype = typeCheck(leftexp, vtenv, ftenv)
      val righttype = typeCheck(rightexp, vtenv, ftenv)
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
            case (FloatType(), FloatType()) => BoolType()
            case (IntType(), FloatType()) => BoolType()
            case (FloatType(), IntType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
        case AndBinOp() | OrBinOp() =>
          (lefttype, righttype) match{
            case (BoolType(),BoolType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
      }
    case UnOpExp(op, exp) => op match{
      case NegUnOp() => typeCheck(exp,vtenv,ftenv) match{
        case IntType() => IntType()
        case FloatType() => FloatType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,vtenv,ftenv))}}", op)
      }
      case NotUnOp() => typeCheck(exp,vtenv,ftenv) match{
        case BoolType() => BoolType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,vtenv,ftenv))}}", op)
      }
    }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      val ce = typeCheck(condexp,vtenv,ftenv)
      val te = typeCheck(thenexp,vtenv,ftenv)
      val ee = typeCheck(elseexp,vtenv,ftenv)
      ce match {
        case BoolType() =>
          if (te == ee) te
          else throw new TypeError("thenexp and elseexp must have the same type", IfThenElseExp(condexp, thenexp, elseexp))
        case _ =>
          throw new TypeError(s"If clause must be a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, defs, exp) =>
      var (vtenv1,ftenv1) = (vtenv, ftenv)
      for (d <- vals) {
        val t = typeCheck(d.exp, vtenv1, ftenv1)
        checkTypesEqual(t, d.opttype, d)
        vtenv1 += (d.x -> d.opttype.getOrElse(t))
      }
      for (d <- defs)
        ftenv1 += (d.fun -> getFunType(d))
      // ftenv1 ++ defs.map(d => d.fun).zip(defs.map(getFunType))
      for (d <- defs) {
        val funType = getFunType(d)
        val vtenv_update = d.params.map(fp => fp.x).zip(funType._1)
        if(d.optrestype.isDefined) {
          checkTypesEqual(typeCheck(d.body, vtenv ++ vtenv_update, ftenv1), d.optrestype, d)
        }
      }
      typeCheck(exp,vtenv1,ftenv1)
    case TupleExp(exps) => TupleType(exps.map(x => typeCheck(x,vtenv,ftenv)))
    case MatchExp(exp, cases) =>
      val exptype = typeCheck(exp, vtenv, ftenv)
      exptype match {
        case TupleType(ts) =>
          for (c <- cases) {
            if (ts.length == c.pattern.length) {
              val venv_update = c.pattern.zip(ts)
              return typeCheck(c.exp,vtenv++venv_update,ftenv)
            }
          }
          throw new TypeError(s"No case matches type ${unparse(exptype)}", e)
        case _ => throw new TypeError(s"Tuple expected at match, found ${unparse(exptype)}", e)
      }
    case CallExp(fun, args) =>
      val paramz = ftenv(fun)
      if(paramz._1.length == args.length){
        for(i <- paramz._1.indices){
          if(paramz._1(i) != typeCheck(args(i),vtenv,ftenv)){
            throw new TypeError(s"Argument nr $i has the wrong type",CallExp(fun, args))
          }
        }
        paramz._2
      } else throw new TypeError("Wrong number of arguments for the function "+fun,CallExp(fun, args))
  }

  /**
    * Returns the parameter types and return type for the function declaration `d`.
    */
  def getFunType(d: DefDecl): (List[Type], Type) =
    (d.params.map(p => p.opttype.getOrElse(throw new TypeError(s"Type annotation missing at parameter ${p.x}", p))),
      d.optrestype.getOrElse(throw new TypeError(s"Type annotation missing at function result ${d.fun}", d)))

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
