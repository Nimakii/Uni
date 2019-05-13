package miniscala
import miniscala.AbstractMachine.{Add, Alloc, And, Branch, Const, Div, Dup, Enter, EnterDefs, Eq, Exit, Instruction, Lambda, Leq, Load, Loop, Lt, Mul, Neg, Not, Or, Pop, Read, Store, Sub, Unit}
object Validator {
  def Validate(prg: List[Instruction],opstack: Int, envstack: Int):(Int,Int)={
    class ValidateError(msg:String) extends Exception
    def ValidateList(prg: List[Instruction]):(Int,Int) =
      prg.foldLeft(0,0)((a:(Int,Int),b:Instruction) =>
        ((c:(Int,Int),d:(Int,Int)) =>
          (c._1+d._1,c._2+d._2))(a,ValidateInst(b,a._1,a._2)))
    def ValidateInst(ins:Instruction,op:Int,en:Int):(Int,Int) ={
      if(op <0 || en < 0 ){throw new ValidateError("Stack underflow")}
      ins match{
        case Const(_) => (op+1,en)
        case Add | Sub | Mul | Div | Eq | Lt | Leq | And | Or => if(op<2){throw new ValidateError("BinOpExp invalid")}
          (op-1,en)
        case Neg | Not => if(op <1){throw new ValidateError("UnOp invalid")}
          (op,en)
        case Dup => if(op<1){throw new ValidateError("Duplicate invalid")}
          (op+1,en)
        case Pop =>(op-1,en)
        case Unit => (op+1,en)
        case Branch(thenlist,elselist) =>
          if(ValidateList(thenlist) != (1,0) || ValidateList(elselist) != (1,0)){
            throw new ValidateError("Branch invalid")}
          (op-1,en)
        case Loop(condlist,bodylist) =>
          if(ValidateList(condlist) != (1,0) || ValidateList(bodylist) != (1,0)){
            throw new ValidateError("Loop invalid")}
          (op,en)
        case Enter => (op-1,en+1)
        case Exit(n) => (op,en-n)
        case Read(i) => if(en<i){throw new ValidateError("Read invalid")}
          (op+1,en)
        case Alloc => (op+1,en)
        case Load => if(op<1){throw new ValidateError("Load invalid")}
          (op,en)
        case Store => (op-2,en)
        case EnterDefs(n) => (op-n,en+n)
        case Lambda(_,body) => if(ValidateList(body) != (1,0)){throw new ValidateError("Lambda invalid")}
          (op+1,en)
        case _ => throw new ValidateError("Uncovered instruction or some other error")
      }
    }
    val validation = ValidateList(prg)
    if(validation._1 <0 || validation._2 <0){throw new ValidateError("Stack underflow")} //final check, ValidateInst doesnt check if there is underflow after the final instance
    validation
  }
}
