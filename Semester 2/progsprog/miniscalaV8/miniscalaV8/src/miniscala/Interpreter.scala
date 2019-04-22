package miniscala

import miniscala.Ast._
import miniscala.Unparser.unparse

import scala.io.StdIn
import scala.util.parsing.input.Position

/**
  * Interpreter for MiniScala.
  */
object Interpreter {

  case class Constructor(params: List[FunParam], body: BlockExp, env: Env, cenv: ClassEnv, classes: List[ClassDecl], srcpos: Position)

  case class ClassDeclType(srcpos: Position) extends Type

  type Env = Map[Id, Val]
  type Sto = Map[Loc, Val]
  type ClassEnv = Map[Id, Constructor]

  type Loc = Int

  sealed abstract class Val

  case class IntVal(v: Int) extends Val

  case class BoolVal(v: Boolean) extends Val

  case class FloatVal(v: Float) extends Val

  case class StringVal(v: String) extends Val

  case class TupleVal(vs: List[Val]) extends Val

  //case class ClosureVal(params: List[FunParam], optrestype: Option[Type], body: Exp, env: Env, cenv: ClassEnv) extends Val
  case class ClosureVal(params: List[FunParam], optrestype: Option[Type], body: Exp, env: Env, cenv: ClassEnv, defs: List[DefDecl]) extends Val
  case class RefVal(loc: Loc, opttype: Option[Type]) extends Val
  case class ObjectVal(members: Env) extends Val

  val unitVal = TupleVal(Nil)


  def nextLoc(sto: Sto): Loc = sto.size

  def eval(e: Exp, env: Env, cenv: ClassEnv, sto: Sto): (Val, Sto) = e match {
    case IntLit(c) => (IntVal(c), sto)
    case BoolLit(c) =>
      trace("Boolean " + c + "found")
      (BoolVal(c), sto)
    case FloatLit(c) =>
      trace("Float" + c + "found")
      (FloatVal(c), sto)
    case StringLit(c) =>
      trace("String \"" + c + "\" found")
      (StringVal(c), sto)
    case NullLit() =>
      (RefVal(-1,None),sto)
    case VarExp(x) =>
      trace(s"Variable $x found, lookup of variable value in environment gave " + env(x))
      (getValue(env.getOrElse(x, throw new InterpreterError(s"Unknown identifier '$x'", e)), sto), sto)
    case BinOpExp(left,AndAndBinOp(),right) =>
      val (leftval, sto1) = eval(left, env, cenv, sto)
      if(leftval == BoolVal(false)){
        (leftval,sto1)
      }
      else if(leftval == BoolVal(true)) {
        val res = eval(right, env, cenv, sto1)
        if(res._1 == BoolVal(true)){
          res
        } else if (res._1 == BoolVal(false)){
          res
        } else throw new InterpreterError("Argument not a boolean",e)
      } else throw new InterpreterError("Argument not a boolean",e)
    case BinOpExp(left,OrOrBinOp(),right) =>
      eval(left,env,cenv,sto) match{
        case (BoolVal(a),sto1) => if(!a){
          (BoolVal(a),sto1)
        } else
          eval(right,env,cenv,sto1) match{
            case (BoolVal(b),sto2) => (BoolVal(b||a),sto2)
            case _ => throw new InterpreterError("Argument not a boolean",e)
          }
        case _ => throw new InterpreterError("Argument not a boolean",e)
      }
    case DoWhileExp(body, cond) =>
      val dopeart = eval(body,env,cenv,sto)
      eval(WhileExp(cond,body),env,cenv,dopeart._2)
    case BinOpExp(leftexp, op, rightexp) =>
      trace("BinOpExp found, evaluating left and right expressions")
      val (leftval, sto1) = eval(leftexp, env, cenv, sto)
      val (rightval, sto2) = eval(rightexp, env, cenv, sto1)
      op match {
        case PlusBinOp() => trace("Adding expressions")
          (leftval, rightval) match {
            case (IntVal(v1), IntVal(v2)) => (IntVal(v1 + v2), sto2)
            case (FloatVal(v1), FloatVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (IntVal(v1), FloatVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (FloatVal(v1), IntVal(v2)) => (FloatVal(v1 + v2), sto2)
            case (StringVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case (StringVal(v1), IntVal(v2)) => (StringVal(v1 + v2), sto2)
            case (StringVal(v1), FloatVal(v2)) => (StringVal(v1 + v2), sto2)
            case (IntVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case (FloatVal(v1), StringVal(v2)) => (StringVal(v1 + v2), sto2)
            case _ => throw new InterpreterError(s"Type mismatch at '+', unexpected values ${valueToString(leftval)} and ${valueToString(rightval)}", op)
          }
        case MinusBinOp() =>
          trace("Subtracting expressions")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (IntVal(a - b), sto2)
            case (FloatVal(a), IntVal(b)) => (FloatVal(a - b), sto2)
            case (IntVal(a), FloatVal(b)) => (FloatVal(a - b), sto2)
            case (FloatVal(a), FloatVal(b)) => (FloatVal(a - b), sto2)
            case (ClosureVal(_, _, _, _, _,_), ClosureVal(_, _, _, _, _,_)) => throw new InterpreterError("This is right here it goes wrong", e)
            case _ => throw new InterpreterError("Illegal subtraction", e)
          }
        case MultBinOp() =>
          trace("Multiplying expressions")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (IntVal(a * b), sto2)
            case (FloatVal(a), IntVal(b)) => (FloatVal(a * b), sto2)
            case (IntVal(a), FloatVal(b)) => (FloatVal(a * b), sto2)
            case (FloatVal(a), FloatVal(b)) => (FloatVal(a * b), sto2)
            case _ => throw new InterpreterError("Illegal multiplication", e)
          }
        case DivBinOp() =>
          if (rightval == IntVal(0) || rightval == FloatVal(0.0f))
            throw new InterpreterError(s"Division by zero", op)
          trace("Dividing expressions")
          (leftval, rightval) match {
            case (IntVal(v1), IntVal(v2)) => (IntVal(v1 / v2), sto2)
            case (FloatVal(v1), FloatVal(v2)) => (FloatVal(v1 / v2), sto2)
            case (IntVal(v1), FloatVal(v2)) => (FloatVal(v1 / v2), sto2)
            case (FloatVal(v1), IntVal(v2)) => (FloatVal(v1 / v2), sto2)
            case _ => throw new InterpreterError("Illegal division", e)
          }
        case ModuloBinOp() =>
          if (rightval == IntVal(0) || rightval == FloatVal(0.0f)) {
            throw new InterpreterError("Modulo by zero", op)
          }
          trace("Calculating modulo")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (IntVal(a % b), sto2)
            case (FloatVal(a), IntVal(b)) => (FloatVal(a % b), sto2)
            case (IntVal(a), FloatVal(b)) => (FloatVal(a % b), sto2)
            case (FloatVal(a), FloatVal(b)) => (FloatVal(a % b), sto2)
            case _ => throw new InterpreterError("Illegal modulation", e)
          }
        case MaxBinOp() =>
          trace("Finding max of expressions")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => if (a > b) {
              (IntVal(a), sto2)
            } else {
              (IntVal(b), sto2)
            }
            case (FloatVal(a), IntVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case (IntVal(a), FloatVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case (FloatVal(a), FloatVal(b)) => if (a > b) {
              (FloatVal(a), sto2)
            } else {
              (FloatVal(b), sto2)
            }
            case _ => throw new InterpreterError("Illegal maximum", e)
          }
        case EqualBinOp() =>
          trace("Evaluating equal")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a == b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a == b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a == b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a == b), sto2)
            case (StringVal(a), StringVal(b)) => (BoolVal(a == b), sto2)
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a == b), sto2)
            case (TupleVal(a), TupleVal(b)) => (BoolVal(a == b), sto2)
            case _ => (BoolVal(false), sto2)
          }
        case LessThanBinOp() =>
          trace("Evaluating less than")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a < b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a < b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a < b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a < b), sto2)
            case _ => throw new InterpreterError("Illegal less than operation", op)
          }
        case LessThanOrEqualBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (IntVal(a), IntVal(b)) => (BoolVal(a <= b), sto2)
            case (FloatVal(a), IntVal(b)) => (BoolVal(a <= b), sto2)
            case (IntVal(a), FloatVal(b)) => (BoolVal(a <= b), sto2)
            case (FloatVal(a), FloatVal(b)) => (BoolVal(a <= b), sto2)
            case _ => throw new InterpreterError("Illegal 'less than or equal' operation", op)
          }
        case AndBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a & b), sto2)
            case _ => throw new InterpreterError("Illegal 'and' operation", op)
          }
        case OrBinOp() =>
          trace("Evaluating less than or equal")
          (leftval, rightval) match {
            case (BoolVal(a), BoolVal(b)) => (BoolVal(a | b), sto2)
            case _ => throw new InterpreterError("Illegal 'and' operation", op)
          }
      }
    case UnOpExp(op, exp) =>
      trace("Unary expression found")
      val (expval, sto1) = eval(exp, env, cenv, sto)
      op match {
        case NegUnOp() =>
          trace("Negation of number")
          expval match {
            case IntVal(v) => (IntVal(-v), sto1)
            case FloatVal(v) => (FloatVal(-v), sto1)
            case _ => throw new InterpreterError("Not a number", e)
          }
        case NotUnOp() =>
          trace("Negation of Boolean")
          expval match {
            case BoolVal(a) => (BoolVal(!a), sto1)
            case _ => throw new InterpreterError("Not a Boolean", e)
          }
      }
    case IfThenElseExp(condexp, thenexp, elseexp) =>
      eval(condexp, env, cenv,sto) match {
        case (BoolVal(a), st) =>
          trace("If statement found, evaluating condition")
          if (a) {
            trace("evaluating then clause")
            return eval(thenexp, env,cenv, st)
          }
          else trace("evaluationg else clause")
          eval(elseexp, env,cenv, st)
        case _ => throw new InterpreterError("Condition clause not a boolean", IfThenElseExp(condexp, thenexp, elseexp))
      }
   /** case BlockExp(vals, vars, defs, exps) =>
      var env1 = env
      var sto1 = sto
      trace("Calculating variable values and adding to variable environment")
      for (d <- vals) {
        val (v, sto2) = eval(d.exp, env1, sto1)
        env1 = env1 + (d.x -> v)
        sto1 = sto2
        checkValueType(v,d.opttype,e)
      }
      for (d <- vars) {
        val (v, sto2) = eval(d.exp, env1, sto1)
        val loc = nextLoc(sto2)
        env1 = env1 + (d.x -> RefVal(loc, d.opttype))
        sto1 = sto2 + (loc -> v)
        checkValueType(v,d.opttype,e)
      }

      //Defs
      env1 = defs.foldLeft(env1)((en: Env, d: DefDecl) => {
        en + (d.fun -> ClosureVal(d.params, d.optrestype, d.body, en, defs))
      })
      var res: Val = unitVal
      for (exp <- exps) {
        val (res1, sto2) = eval(exp, env1, sto1)
        res = res1
        sto1 = sto2
      }
      (res, sto1)
     */
    case b: BlockExp =>
        val (res, _, sto1) = evalBlock(b, env, cenv, sto)
      (res, sto1)

    case TupleExp(exps) =>
      var (vals, sto1) = (List[Val](), sto)
      for (exp <- exps) {
        val (v, sto2) = eval(exp, env, cenv, sto1)
        vals = v :: vals
        sto1 = sto2
      }
      (TupleVal(vals.reverse), sto1)
    case MatchExp(exp, cases) =>
      trace("Updating ")
      val (expval, sto1) = eval(exp, env, cenv, sto)
      expval match {
        case TupleVal(vs) =>
          for (c <- cases) {
            if (vs.length == c.pattern.length) {
              val venv_update = c.pattern.zip(vs)
              return eval(c.exp, env ++ venv_update,cenv, sto1)
            }
          }
          throw new InterpreterError(s"No case matches value ${valueToString(expval)}", e)
        case _ => throw new InterpreterError(s"Tuple expected at match, found ${valueToString(expval)}", e)
      }
    case LambdaExp(params, body) =>             //Closureval, the defdecl list, to implement mutual recursion has been removed from closureval, TV
      (ClosureVal(params, None, body, env, cenv,List[DefDecl]()), sto)
    case AssignmentExp(x, exp) =>
      val (v, sto1) = eval(exp, env, cenv, sto)
      env(x) match {
        case RefVal(loc, opttype) =>
          checkValueType(v, opttype, e)
          (unitVal, sto1 + (loc -> v))
        case _ => throw new InterpreterError("Not a var", e)
      }

    case WhileExp(cond, body) =>
      eval(cond, env,cenv, sto) match {
        case (BoolVal(true), st) =>
          val (_, st1) = eval(body, env,cenv, st)
          eval(WhileExp(cond, body), env,cenv, st1)
        case (BoolVal(false), st) => (unitVal, st)
        case _ => throw new InterpreterError("Not a boolean", cond)
      }

    case CallExp(funexp, args) =>
      trace("Attempting a function call")
      eval(funexp, env,cenv, sto) match {
        case (ClosureVal(params, optrestype, body, kenv, cenv1,defs), sto1) =>
          trace("Function found, attempting evaluation")
          if (args.length == params.length) {
            def halp(fp: FunParam): Id = fp.x

            var kenv_updated = kenv
            var sto2 = sto1

            trace("Rebinding functions, for mutual recursion")
            for (d <- defs) {                                                     //Rebinding, mutual recursion
              kenv_updated += (d.fun -> ClosureVal(d.params, d.optrestype, d.body, kenv,cenv1, defs))
            }

            trace("Evaluating parameters")
            for (i <- args.indices) {
              val evaluation = eval(args(i), env,cenv, sto2)
              val argval = evaluation._1
              sto2 = evaluation._2
              checkValueType(argval, params(i).opttype, CallExp(funexp, args))
              kenv_updated += (halp(params(i)) -> argval)                         //Rho2 updating
            }

            trace("Evalutating, function body with parameters values")
            val res = eval(body, kenv_updated,cenv1, sto2)
            checkValueType(res._1, optrestype, CallExp(funexp, args))
            return res
          }
          else
            throw new InterpreterError(s"Wrong number of arguments", CallExp(funexp, args))
        case _ =>
          throw new InterpreterError(s"Not a function", funexp)

      }


    case NewObjExp(klass, args) =>
      trace("Trying to create new object")
      val c = cenv.getOrElse(klass, throw new InterpreterError(s"Unknown class name '$klass'", e))
      trace("Rebinding to achieve mutual recursion")
      val declcenv1 = rebindClasses(c.env, c.cenv, c.classes)

      trace("Evaluating arguments")
      val (declenv1, sto1) = evalArgs(args, c.params, env, sto, cenv, c.env, declcenv1, e) //rho, kappa, sigma evaluering - v, sigma'

      trace("Building methods for the object")
      val (_, env1, sto2) = evalBlock(c.body, declenv1, declcenv1, sto1)
      trace("Getting new unused location")
      val newloc = nextLoc(sto2)
      trace("Making the restricted enviroment") //funktion i klasse med samme navnfeltVARiablefeltVALiable
      val objenv = env1.filterKeys(p => c.body.defs.exists(d => d.fun == p) || c.body.vars.exists(d => d.x == p) || c.body.vals.exists(d => d.x == p))
      trace("Adding object to storage")
      val sto3 = sto2 + (newloc -> ObjectVal(objenv))
      trace("Returning object location and updated store")
      (RefVal(newloc, Some(ClassDeclType(c.srcpos))), sto3)

    case LookupExp(objexp, member) =>
      trace("Evaluating base value")
      val (objval, sto1) = eval(objexp, env, cenv, sto) //rho, kappa, sigma eval e - l, sigma'
      objval match {
        case RefVal(loc, _) =>
          trace("Location found, looking in storage")
          if(loc == -1) {throw new InterpreterError("Null pointer exception",LookupExp(objexp, member))}
          sto1(loc) match {
            case ObjectVal(members) =>
              trace("Looking for matching class method")
              (getValue(members.getOrElse(member, throw new InterpreterError(s"No such member: $member", e)), sto1), sto1)
            case v => throw new InterpreterError(s"Base value of lookup is not a reference to an object: ${valueToString(v)}", e)
          }
        case _ => throw new InterpreterError(s"Base value of lookup is not a location: ${valueToString(objval)}", e)
      }
  }

  /**
    * Evaluates the given block.
    * Returns the resulting value, the updated environment after evaluating all declarations, and the latest store.
    */
  def evalBlock(b: BlockExp, env: Env, cenv: ClassEnv, sto: Sto): (Val, Env, Sto) = {
    var env1 = env
    var sto1 = sto
    trace("Calculating values and adding to environment and storage")
    for (d <- b.vals) {
      val (v, sto2) = eval(d.exp, env1, cenv, sto1)
      val ot = getType(d.opttype, cenv)
      env1 = env1 + (d.x -> v)
      sto1 = sto2
    }
    trace("Calculating variables and adding to environment and storage")
    for (d <- b.vars) {
      val (v1, sto2) = eval(d.exp, env1, cenv, sto1)
      trace(s"Evaluating vars.")
      val loc = nextLoc(sto2)
      val ot =getType(d.opttype, cenv)
      env1 = env1 + (d.x -> RefVal(loc, ot))
      sto1 = sto2 + (loc -> v1)
    }


    trace("Defining functions and updating enviroment")
    env1 = b.defs.foldLeft(env1)((en: Env, d: DefDecl) => {
      en + (d.fun -> ClosureVal(d.params, d.optrestype, d.body, en, cenv,b.defs))
    })

    var cenv1 = cenv
    trace("Defining classes and updating class enviroment")
    for (d <- b.classes)
      cenv1 = cenv1 + (d.klass -> Constructor(d.params, d.body, env1, cenv, b.classes, d.pos))
    var res: Val = unitVal
    trace("Evaluating side effects of expressions")
    for (exp <- b.exps) {
      val (res1, sto2) = eval(exp, env1, cenv1, sto1)
      res = res1
      sto1 = sto2
    }
    trace("Returning result of last expression in this block")
    (res, env1, sto1)
  }



  /**
    * Evaluates the arguments `args` in environment `env` with store `sto`,
    * extends the environment `declenv` with the new bindings, and
    * returns the extended environment and the latest store.
    */
  def evalArgs(args: List[Exp], params: List[FunParam], env: Env, sto: Sto, cenv: ClassEnv, declenv: Env, declcenv: ClassEnv, e: Exp): (Env, Sto) = {
    trace("Evaluating arguments in the enviroment")
    if (args.length != params.length) throw new InterpreterError("Wrong number of arguments at call/new", e)
    trace("Extending the enviroment with the new bindings")
    var (env1, sto1) = (declenv, sto)
    for ((p, arg) <- params.zip(args) ) {
      val (argval, sto2) = eval(arg, env, cenv, sto1)
      checkValueType(argval, getType(p.opttype, declcenv), arg)
      env1 = env1 + (p.x -> argval)
      sto1 = sto2
    }
    trace("Returning the extended enviroment and latest store")
    (env1, sto1)
  }

  /**
    * If `v` is a reference to an object or it is a non-reference value, then return `v` itself;
    * otherwise, it must be a reference to a non-object value, so return that value.
    */
  def getValue(v: Val, sto: Sto): Val = v match {
    case RefVal(loc, _) =>
      trace("Reference value found, looking in storage")
      if(loc == -1) {return v}
      sto(loc) match {
        case _: ObjectVal => //ObjectVal type
          trace("Object found, returning reference")
          v
        case stoval =>
          trace("Value found, returning value")
          stoval
      }
    case _ =>
      trace("Not a reference, no lookup in storage needed, returning value")
      v
  }

  /**
    * Rebinds `classes` in `cenv` to support recursive class declarations.
    */
  def rebindClasses(env: Env, cenv: ClassEnv, classes: List[ClassDecl]): ClassEnv = {
    trace("Rebinding classes in class enviroment")
    var cenv1 = cenv
    for (d <- classes)
      cenv1 = cenv1 + (d.klass -> Constructor(d.params, d.body, env, cenv, classes, d.pos))
    trace("Returning updated class enviroment")
    cenv1

  }

  /**
    * Returns the proper type for the type annotation `ot` (if present).
    * Class names are converted to proper types according to the class environment `cenv`.
    */
  def getType(ot: Option[Type], cenv: ClassEnv): Option[Type] = ot.map(t => {
    def getType(t: Type): Type = t match {
      case ClassType(klass) => ClassDeclType(cenv.getOrElse(klass, throw new InterpreterError(s"Unknown class '$klass'", t)).srcpos)
      case IntType() | BoolType() | FloatType() | StringType() | NullType() => t
      case TupleType(ts) => TupleType(ts.map(getType))
      case FunType(paramtypes, restype) => FunType(paramtypes.map(getType), getType(restype))
      case _ => throw new RuntimeException(s"Unexpected type $t") // this case is unreachable
    }
    getType(t)
  })

  /**
    * Checks whether value `v` has type `ot` (if present), generates runtime type error otherwise.
    */
  def checkValueType(v: Val, ot: Option[Type], n: AstNode): Unit = ot match {
    case Some(t) =>
      (v, t) match {
        case (IntVal(_), IntType()) |
             (BoolVal(_), BoolType()) |
             (FloatVal(_), FloatType()) |
             (IntVal(_), FloatType()) |
             (StringVal(_), StringType()) => // do nothing
        case (TupleVal(vs), TupleType(ts)) if vs.length == ts.length =>
          for ((vi, ti) <- vs.zip(ts))
            checkValueType(vi, Some(ti), n)
        case (ClosureVal(cparams, optcrestype, _, _, cenv,_), FunType(paramtypes, restype)) if cparams.length == paramtypes.length =>
          for ((p, t) <- cparams.zip(paramtypes))
            checkTypesEqual(t, getType(p.opttype, cenv), n)
          checkTypesEqual(restype, getType(optcrestype, cenv), n)
        case (RefVal(loc, Some(vd: ClassDeclType)), td: ClassDeclType) =>
          if(loc == -1){return }
          if(vd != td)
            throw new InterpreterError(s"Type mismatch: object of type ${unparse(vd)} does not match type ${unparse(td)}", n)
        case (RefVal(loc,_),_) =>
          if(loc == -1){return }
          println(loc)
          throw new InterpreterError("How did you end up here?",n)
        case _ =>
          throw new InterpreterError(s"Type mismatch: value ${valueToString(v)} does not match type ${unparse(t)}", n)
      }
    case None => // do nothing
  }

  /**
    * Checks that the types `t1` and `ot2` are equal (if present), throws type error exception otherwise.
    */
  def checkTypesEqual(t1: Type, ot2: Option[Type], n: AstNode): Unit = ot2 match {
    case Some(t2) =>
      if (t1 != t2)
        throw new InterpreterError(s"Type mismatch: type ${unparse(t1)} does not match expected type ${unparse(t2)}", n)
    case None => // do nothing
  }

  /**
    * Converts a value to its string representation (for error messages).
    */
  def valueToString(v: Val): String = v match {
    case IntVal(c) => c.toString
    case FloatVal(c) => c.toString
    case BoolVal(c) => c.toString
    case StringVal(c) => c
    case TupleVal(vs) => vs.map(v => valueToString(v)).mkString("(", ",", ")")
    case ClosureVal(params, _, exp, _, _,_) => // the resulting string ignores the result type annotation, the declaration environment, and the set of classes
      s"<(${params.map(p => unparse(p)).mkString(",")}), ${unparse(exp)}>"
    case RefVal(loc, _) =>
      if(loc== -1){return "null"}
      s"$loc" // the resulting string ignores the type annotation
    case ObjectVal(_) => "object" // (unreachable case)
  }

  /**
    * Builds an initial environment, with a value for each free variable in the program.
    */
  def makeInitialEnv(program: Exp): Env = {
    var env = Map[Id, Val]()
    for (x <- Vars.freeVars(program)) {
      print(s"Please provide an integer value for the variable $x: ")
      env = env + (x -> IntVal(StdIn.readInt()))
    }
    env
  }

  /**
    * Prints message if option -trace is used.
    */
  def trace(msg: => String): Unit =
    if (Options.trace)
      println(msg)


  /**
    * Exception thrown in case of MiniScala runtime errors.
    */
  class InterpreterError(msg: String, node: AstNode) extends MiniScalaError(s"Runtime error: $msg", node.pos)

}