package miniscala

object Ta26 {
  sealed abstract class Nat
  case object Zero extends Nat
  case class Succ(n: Nat) extends Nat

  def main(args: Array[String]): Unit ={
    val a4 = Succ(Succ(Succ(Succ(Zero))))
    val a3 = Succ(Succ(Succ(Zero)))
    println(a4)
    println(decode(a4))
    println(encode(5))
    println(encode(0))
    println(decode(add(a4,a3)))
    println(decode(add(a3,a4)))
    println(decode(multi(a3,a4)))
    println(multi(a4,a3))
    println(decode(multi(a3,Zero)))
    println(multi(Zero,a3))
    println(power(a3,a4))
    println(decode(power(a3,a4)))
    println(decrement(a4))
    println(decrement(Zero))
  }
  def decode(n:Nat): Int = {
    def decod(n:Nat,i:Int): Int = n match {
      case Zero => i
      case Succ(m:Nat) => decod(m,i+1)
    }
    decod(n,0)
  }
  def encode(i:Int): Nat = {
    def encod(i:Int,m:Nat): Nat = {
      if(i<0){throw new RuntimeException("Fuck you")}
      else if (i==0){m}
      else {encod((i-1),Succ(m))}
    }
    encod(i,Zero)
  }
  def add(n:Nat,m:Nat): Nat ={
    def ad(n:Nat,m:Nat): Nat = m match{
      case Zero => n
      case Succ(k) => ad(Succ(n),k)
    }
    ad(n,m)
  }
  def multi(n:Nat,m:Nat): Nat = {
    def mult(n:Nat,m:Nat,res:Nat): Nat = m match{
      case Zero => res
      case Succ(k) => mult(n,k,add(n,res))
    }
    mult(n,m,Zero)
  }
  def power(n:Nat,m:Nat): Nat = {
    def pwr(n:Nat,m:Nat,res:Nat): Nat = m match{
      case Zero => res
      case Succ(k) => pwr(n,k,multi(n,res))
    }
    pwr(n,m,Succ(Zero))
  }
  def decrement(n:Nat): Nat = n match{
    case Zero => Zero
    case Succ(m) => m
  }
}
