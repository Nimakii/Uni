package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.util.parsing.input.Position

/**
  * Type checker for MiniScala.
  */
object TypeChecker {

  type TypeEnv = Map[Id, Type]
  type ClassTypeEnv = Map[Id, ConstructorType]

  case class RefType(theType: Type) extends Type

  case class ConstructorType(srcpos: Position, params: List[FunParam], membertypes: TypeEnv) extends Type

  val unitType = TupleType(Nil)

  def typeCheck(e: Exp, tenv: TypeEnv, ctenv: ClassTypeEnv): Type = e match {
    case IntLit(_) => IntType()
    case FloatLit(_) => FloatType()
    case BoolLit(_) => BoolType()
    case StringLit(_) => StringType()
    case NullLit() => NullType()
    case VarExp(x) => tenv.getOrElse(x, throw new TypeError(s"Unknown identifier '$x'", e)) match {
      case RefType(thetype) => thetype
      case t: Type => t
    }
    case BinOpExp(leftexp, op, rightexp) =>
      val lefttype = typeCheck(leftexp, tenv, ctenv)
      val righttype = typeCheck(rightexp, tenv, ctenv)
      op match {
        case AndAndBinOp() =>
          (lefttype,righttype) match{
            case (BoolType(),BoolType())=> BoolType()
            case _=> throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
        case OrOrBinOp() =>
          (lefttype,righttype) match{
            case (BoolType(),BoolType())=> BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
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
          (lefttype, righttype) match {
            case (IntType(), IntType()) => BoolType()
            case (FloatType(), FloatType()) => BoolType()
            case (IntType(), FloatType()) => BoolType()
            case (FloatType(), IntType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
        case AndBinOp() | OrBinOp() =>
          (lefttype, righttype) match {
            case (BoolType(), BoolType()) => BoolType()
            case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected types ${unparse(lefttype)} and ${unparse(righttype)}", op)
          }
      }
    case UnOpExp(op, exp) => op match {
      case NegUnOp() => typeCheck(exp, tenv,ctenv) match {
        case IntType() => IntType()
        case FloatType() => FloatType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp, tenv,ctenv))}}", op)
      }
      case NotUnOp() => typeCheck(exp, tenv,ctenv) match {
        case BoolType() => BoolType()
        case _ => throw new TypeError(s"Type mismatch at '${unparse(op)}', unexpected type ${unparse(typeCheck(exp, tenv,ctenv))}}", op)
      }
    }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      val ce = typeCheck(condexp, tenv,ctenv)
      val te = typeCheck(thenexp, tenv,ctenv)
      val ee = typeCheck(elseexp, tenv,ctenv)
      ce match {
        case BoolType() =>
          if (subtype(te,ee)) return ee
          if (subtype(ee,te)) return te
          else
            throw new TypeError(s"Then expression ${unparse(te)} and the else expression ${unparse(ce)} should be of same type", IfThenElseExp(condexp, thenexp, elseexp))
        case _ => throw new TypeError(s"Type mismatch at If statement, unexpected type either in the condition ${unparse(ce)} or in the inner expressions that must be of the same type ${unparse(te)} = ${unparse(ee)}", IfThenElseExp(condexp, thenexp, elseexp))
      }

    case BlockExp(vals, vars, defs, classes, exps) =>
      var tenv_updated = tenv //ValDecl
      for (d <- vals) {
        val t = typeCheck(d.exp, tenv_updated,ctenv)
        checkSubtype(t, getType(d.opttype,ctenv,d), d)
        tenv_updated += (d.x -> d.opttype.getOrElse(t))
      }
      //VarsDecl
      for(d <- vars){
        val dType = typeCheck(d.exp,tenv_updated,ctenv)     //theta e : tau
        val ot = getType(d.opttype,ctenv,d)
        checkSubtype(dType,ot,d)
        tenv_updated += (d.x->RefType(dType))         //theta' x -> ref(tau)
      }
      //DefDecl with mututal recursion, via lecture 6, slide 36
      for (d <- defs) {
        tenv_updated += (d.fun -> getFunType(d))
      }
      for (d <- defs) {
        var tenvy = tenv_updated
        for (p <- d.params) {
          tenvy += (p.x -> p.opttype.getOrElse(throw new TypeError("Some error", BlockExp(vals,vars, defs,classes, exps)))) //tau_1 = type(t_1) paramtype
        }
                        //Theta' [x ->tau-1] |-e:tau_2
        checkSubtype(typeCheck(d.body, tenvy,ctenv), d.optrestype, BlockExp(vals,vars, defs,classes, exps))  //tau_2 = type(t_2) resttype
      }
      //ClassDecl
      var ctenvUpdated = ctenv
      for(cd <- classes) {
        val tau = cd.params.map(a => getType(a.opttype,ctenvUpdated,BlockExp(vals, vars, defs, classes, exps)))
        ctenvUpdated += (cd.klass -> getConstructorType(cd,ctenvUpdated,classes))
        var typeenvUpdated = tenv
        for (x <- cd.params) {
          typeenvUpdated += (x.x -> getType(x.opttype.getOrElse(throw new TypeError("",BlockExp(vals, vars, defs, classes, exps))), ctenvUpdated))
        }
            //x.opttype.getOrElse(throw new TypeError("Missing paramater type annotation",BlockExp(vals, vars, defs, classes, exps))))
        typeCheck(cd.body,typeenvUpdated,ctenvUpdated)
      }

      //Block2 and block empty
      var res : Type = unitType
      for (exp <- exps){
        res = typeCheck(exp,tenv_updated,ctenvUpdated)
      }
      res
    case TupleExp(exps) => TupleType(exps.map(x => typeCheck(x, tenv,ctenv)))
    case MatchExp(exp, cases) =>
      val exptype = typeCheck(exp, tenv, ctenv)
      exptype match {
        case TupleType(ts) =>
          for (c <- cases) {
            if (ts.length == c.pattern.length) {
              val venv_update = c.pattern.zip(ts)
              return typeCheck(c.exp, tenv ++ venv_update,ctenv)
            }
          }
          throw new TypeError(s"No case matches type ${unparse(exptype)}", e)
        case _ => throw new TypeError(s"Tuple expected at match, found ${unparse(exptype)}", e)
      }

    case LambdaExp(params, body) =>
      val pal = params.map(p => p.x -> p.opttype.getOrElse(throw new TypeError("Missing type annotation", LambdaExp(params, body))))
      FunType(pal.unzip._2, typeCheck(body, tenv ++ pal,ctenv))
    case AssignmentExp(x, exp) =>
      tenv(x) match{
        case RefType(a) => checkSubtype(typeCheck(exp,tenv,ctenv),Some(a),e)
          return unitType
        case _ => throw new TypeError("Not a var",e)
      }
      return unitType
    case WhileExp(cond, body) =>
      typeCheck(cond,tenv,ctenv) match{
        case BoolType() => typeCheck(body,tenv,ctenv)
          return unitType
        case _ => throw new TypeError("Not a boolean expression",e)
      }
    case DoWhileExp(body,cond) =>
      val firstCheck = typeCheck(body,tenv,ctenv)
      typeCheck(WhileExp(cond,body),tenv,ctenv)
    case NewObjExp(klass, args) =>
      val constructor = ctenv.getOrElse(klass,throw new TypeError("Class not foiund",NewObjExp(klass, args)))
      val paramtypes = constructor.params.map(b => b.opttype)
      if(!(args.length == constructor.params.length)){throw new TypeError("Wrong number of arguments",NewObjExp(klass,args))}
      for (i <- args.indices){
        checkSubtype(typeCheck(args(i),tenv,ctenv),paramtypes(i),NewObjExp(klass, args))
      }
      constructor
    case LookupExp(objexp, member) =>
      typeCheck(objexp,tenv,ctenv) match {
        case ConstructorType(k,tau1,m) => getType(m.getOrElse(member,throw new TypeError("Not a function on this object",LookupExp(objexp, member))),ctenv)
        case _ => throw new TypeError("Not an object",objexp)
      }

    case CallExp(funexp, args) => typeCheck(funexp, tenv,ctenv) match {
      case FunType(params, restype) =>
        if (args.length == params.length) {
          for (i <- args.indices) {
            if (typeCheck(args(i), tenv,ctenv) != params(i))
              throw new TypeError("Fool of a took", CallExp(funexp, args))
          }
          return restype
        }
        else throw new TypeError("Wrong number of arguments", CallExp(funexp, args))
      case _ => throw new TypeError("Not a function", funexp)
    }
  }

  /**
    * Returns the proper type for `t`.
    * Class names are converted to proper types according to the class-type environment `ctenv`.
    */
  def getType(t: Type, ctenv: ClassTypeEnv): Type = t match {
    case ClassType(klass) => ctenv.getOrElse(klass, throw new TypeError(s"Unknown class '$klass'", t))
    case IntType() | BoolType() | FloatType() | StringType() | NullType() => t
    case TupleType(ts) => TupleType(ts.map(tt => getType(tt, ctenv)))
    case FunType(paramtypes, restype) => FunType(paramtypes.map(tt => getType(tt, ctenv)), getType(restype, ctenv))
    case _ => throw new RuntimeException(s"Unexpected type $t") // this case is unreachable...
  }

  /**
    * Returns the proper type for `t` (if present).
    */
  def getType(ot: Option[Type], ctenv: ClassTypeEnv, n: AstNode): Option[Type] = ot.map(t => getType(t, ctenv))

  /**
    * Returns the function type for the function declaration `d`.
    */
  def getFunType(d: DefDecl): FunType =
    FunType(d.params.map(p => p.opttype.getOrElse(throw new TypeError(s"Type annotation missing at parameter ${p.x}", p))),
      d.optrestype.getOrElse(throw new TypeError(s"Type annotation missing at function result ${d.fun}", d)))

  /**
    * Returns the constructor type for the class declaration `d`.
    */
  def getConstructorType(d: ClassDecl, ctenv: ClassTypeEnv, classes: List[ClassDecl]): ConstructorType = {
    var membertypes: TypeEnv = Map()
    for (m <- d.body.vals)
      membertypes = membertypes + (m.x -> m.opttype.getOrElse(throw new TypeError(s"Type annotation missing at field ${m.x}", m)))
    for (m <- d.body.vars)
      membertypes = membertypes + (m.x -> m.opttype.getOrElse(throw new TypeError(s"Type annotation missing at field ${m.x}", m)))
    for (m <- d.body.defs)
      membertypes = membertypes + (m.fun -> getFunType(m))
    ConstructorType(d.pos, d.params, membertypes)
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





  /**
    * Checks whether t1 is a subtype of t2.
    */
  def subtype(t1: Type, t2: Type): Boolean =
    if(t1.equals(t2)) true //reflexivity
    else (t1,t2) match  {
    case (IntType(),FloatType()) => true
    case (NullType(),ConstructorType(_,_,_)) => true
    case (FunType(left1,right1),FunType(left2,right2)) =>
      if (left1.length == left2.length ){
        left1.zip(left2).foldRight[Boolean](true)((a,b) => subtype(a._2,a._1)&&b) &&
        subtype(right1,right2)
      } else false
    case (TupleType(left1),TupleType(left2)) =>
      if (left1.length == left2.length ){
        left1.zip(left2).foldRight[Boolean](true)((a,b) => subtype(a._1,a._2)&&b)
      } else false
    case _ => false
  }


  /**
    * Checks whether t1 is a subtype of t2, generates type error otherwise.
    */
  def checkSubtype(t1: Type, t2: Type, n: AstNode): Unit =
    if (!subtype(t1, t2)) throw new TypeError(s"Type mismatch: type ${unparse(t1)} is not subtype of ${unparse(t2)}", n)

  /**
    * Checks whether t1 is a subtype of ot2 (if present), generates type error otherwise.
    */
  def checkSubtype(t: Type, ot2: Option[Type], n: AstNode): Unit = ot2 match {
    case Some(t2) => checkSubtype(t, t2, n)
    case None => // do nothing
  }

}


