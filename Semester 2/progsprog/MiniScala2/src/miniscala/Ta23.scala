package miniscala

object Ta23 {
  def main(args: Array[String]): Unit ={
    val a = Cons(1,Cons(2,Cons(3,Nil)))
    val b = Cons(3,Cons(2,Cons(1,Nil)))
    val long = Cons(0,Cons(1,Cons(2,Cons(3,Cons(4,Cons(5,Cons(6,Cons(7,Cons(8,Cons(9,Nil))))))))))
    println(a)
    println(square(a))
    println(ordered(a))
    println(ordered(b))
    println(odd(long))
  }

  sealed abstract class IntList
  case object Nil extends IntList{
    override def toString: String = ""
  }
  case class Cons(x:Int, xs:IntList) extends IntList{
    override def toString: String = xs match{
      case Nil => s"$x"
      case Cons(y,ys) => s"$x, $xs"
    }
  }

  def square(xs:IntList): IntList = xs match {
    case Nil => Nil
    case Cons(y,ys) => Cons(y*y,square(ys))
  }
  def order(zs:IntList,z:Int): Boolean = zs match {
    case Nil => true
    case Cons(y, ys) => if(z<=y){order(ys,y)}else{false}
  }
  def odd(xs:IntList): IntList = xs match {
    case Nil => Nil
    case Cons(y,ys) => ys match{
      case Nil => Nil
      case Cons(z,zs) => Cons(z,odd(zs))
    }
  }
  def ordered(xs:IntList): Boolean = xs match {
    case Nil => true
    case Cons(y,ys) => order(ys,y)
  }
}
