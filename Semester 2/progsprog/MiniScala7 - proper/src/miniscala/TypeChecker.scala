package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

/**
  * Type checker for MiniScala.
  */
object TypeChecker {
  type TypeEnv = Map[Id, Type]

  case class RefType(theType: Type) extends Type

  val unitType = TupleType(List[Type]())

  def typeCheck(e: Exp, tenv: TypeEnv): Type = e match {
    case IntLit(_) => IntType()
    case BoolLit(_) => BoolType()
    case FloatLit(_) => FloatType()
    case StringLit(_) => StringType()
    case VarExp(x) => tenv.getOrElse(x, throw new TypeError(s"Unknown identifier '$x'", e)) match {
      case RefType(thetype) => thetype
      case t: Type => t
    }
    case BinOpExp(leftexp, op, rightexp) =>
      val lefttype = typeCheck(leftexp, tenv)
      val righttype = typeCheck(rightexp, tenv)
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
      case NegUnOp() => typeCheck(exp,tenv) match{
        case IntType() => IntType()
        case FloatType() => FloatType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,tenv))}}", op)
      }
      case NotUnOp() => typeCheck(exp,tenv) match{
        case BoolType() => BoolType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp,tenv))}}", op)
      }
    }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      val ce = typeCheck(condexp,tenv)
      val te = typeCheck(thenexp,tenv)
      val ee = typeCheck(elseexp,tenv)
      ce match {
        case BoolType() =>
          if (te == ee) te
          else throw new TypeError("thenexp and elseexp must have the same type", IfThenElseExp(condexp, thenexp, elseexp))
        case _ =>
          throw new TypeError(s"If clause must be a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
    case BlockExp(vals, vars, defs, exps) =>
      var tenv_updated = tenv
      for (d <- vals) { //valDecl
        val t = typeCheck(d.exp, tenv_updated)
        tenv_updated += (d.x -> d.opttype.getOrElse(throw new TypeError("No type annotation",BlockExp(vals, vars, defs, exps))))
        checkTypesEqual(t, d.opttype, d)
      }
      //VarDecl
      for (vaR <- vars){
        val vaRType = typeCheck(vaR.exp,tenv_updated) //theta|-e:tau
        checkTypesEqual(vaRType,vaR.opttype,BlockExp(vals, vars, defs, exps)) //tau = type(t)
        tenv_updated += (vaR.x -> RefType(vaRType)) //theta' = theta[x ->Ref(tau)
        //theta' is returned because tenv_updated is a var.
      }
      //defDecl with mutual recursion via lecture 6 slide 36
      for (d <- defs){ //theta'
        tenv_updated += (d.fun -> getFunType(d))
      }
      for (d <- defs){
        var tenvy = tenv_updated
        for (p <- d.params){
          tenvy += (p.x -> p.opttype.getOrElse(throw new TypeError("",p))) //tau_1 = type(t_1) paramtype
        }
        //theta'[x->tau_1]|-e:tau_2
        checkTypesEqual(typeCheck(d.body,tenvy),d.optrestype,BlockExp(vals, vars, defs, exps)) //tau_2 = type(t_2) restype
      }
      //T-Block2 & T-BlockEmpty
      var res: Type = unitType
      for (exp <- exps){
        res = typeCheck(exp,tenv_updated)
      }
      res
    case TupleExp(exps) => TupleType(exps.map(x => typeCheck(x,tenv)))
    case MatchExp(exp, cases) =>
      val exptype = typeCheck(exp, tenv)
      exptype match {
        case TupleType(ts) =>
          for (c <- cases) {
            if (ts.length == c.pattern.length) {
              val venv_update = c.pattern.zip(ts)
              return typeCheck(c.exp,tenv++venv_update)
            }
          }
          throw new TypeError(s"No case matches type ${unparse(exptype)}", e)
        case _ => throw new TypeError(s"Tuple expected at match, found ${unparse(exptype)}", e)
      }
    case LambdaExp(params, body) =>
      val Jeppe = params.map(p => p.x -> p.opttype.getOrElse(
        throw new TypeError("Missing type annotation",LambdaExp(params, body))))
      FunType(Jeppe.unzip._2,typeCheck(body,tenv ++ Jeppe))
    case AssignmentExp(x, exp) =>
      tenv(x) match{
        case RefType(a) => if(typeCheck(exp,tenv)==a){ //husk at Some(a) kalder constructor for Option
          return unitType
        } else throw new TypeError("Incompatible types, expected "+a+" but got "+typeCheck(exp,tenv), e)
        case _ => throw new TypeError("Not a var", e)
      }
    case WhileExp(cond, body) =>
      typeCheck(cond,tenv) match{
        case BoolType() => typeCheck(body,tenv); return unitType
        case _ => throw new TypeError("Condition is not a Boolean",cond)
      }

    /**
      * LambdaExp(params: List[FunParam], body: Exp)
      * TypeEnv = Map[Id, Type]
      * FunParam(x: Id, opttype: Option[Type])
      * FunType(paramtypes: List[Type], restype: Type)
      */
    case CallExp(funexp, args) => typeCheck(funexp,tenv) match{
      case FunType(params,restype) =>
        if(args.length == params.length){
          for(i<- args.indices){
            if(typeCheck(args(i),tenv) != params(i)){
              throw new TypeError("Fool of a Took",CallExp(funexp, args))
            }
          }
          return restype
        } else throw new TypeError("Wrong number of arguments",CallExp(funexp, args))
      case _ => throw new TypeError("Not a function",funexp)
    }
  }

  /**
    * Returns the function type for the function declaration `d`.
    */
  def getFunType(d: DefDecl): FunType =
    FunType(d.params.map(p => p.opttype.getOrElse(throw new TypeError(s"Type annotation missing at parameter ${p.x}", p))),
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
  def makeInitialTypeEnv(program: Exp): TypeEnv = {
    var tenv: TypeEnv = Map()
    for (x <- Vars.freeVars(program))
      tenv = tenv + (x -> IntType())
    tenv
  }

  /**
    * Exception thrown in case of MiniScala type errors.
    */
  class TypeError(msg: String, node: AstNode) extends MiniScalaError(s"Type error: $msg", node.pos)
}
