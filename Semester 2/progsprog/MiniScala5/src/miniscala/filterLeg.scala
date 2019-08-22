
package miniscala


object filterLeg {

  sealed abstract class List[T]
  case class Nil[T]() extends List[T]
  case class Cons[T](x: T, xs: List[T]) extends List[T]

  sealed abstract class Option[T]
  case class None[T]() extends Option[T]
  case class Some[T](t: T) extends Option[T]

  def foldLeft[A,B](xs: List[A], z: B, f: (B, A) => B): B = xs match {
    case Nil() => z
    case Cons(y, ys) => foldLeft(ys, f(z, y), f)
  }

  def filter[T](xs: List[T], p: T => Boolean): List[T] = xs match {
    case Nil() => Nil[T]()
    case Cons(y, ys) =>
      val r = filter(ys, p)
      if (p(y)) Cons(y, r) else r
  }

  def filter2[T](xs: List[T], p: T => Boolean): List[T] = {
    def fun(a:T):Option[T] = {
      if(p(a))
        Some(a)
      else
        None()
    }
    def unpack[H](xs: List[Option[H]], res: List[H]): List[H] = xs match{
      case Nil() => res
      case Cons(y,ys) => y match{
        case None() => unpack(ys,res)
        case Some(a) => unpack(ys,Cons(a,res))
      }
    }
    //foldLeft[Option[T],List[T]((foldLeft[T,List[Option[T]]](xs,Nil(),(a:List[Option[T]],b) => Cons(fun(b),a)),Nil()),Nil(), (ys:List[T],op:Option[T]) => if()  )
    foldLeft[T,List[T]](xs,Nil(),(ys:List[T],y: T) => if(p(y)) Cons(y,ys) else ys )
  }
  def filter3[T](xs: List[T], p: T => Boolean): List[T] = {
    foldLeft[T,List[T]](xs,Nil(),(ys:List[T],y: T) => if(p(y)) Cons(y,ys) else ys )
  }


  def main(args: Array[String]): Unit = {
    val t = Cons(3, Cons(4, Nil()))
    println(filter3(t, (a: Int) => a < 4))
    val p = Cons(3, Cons(4, Cons(5, Nil())))
    println(filter3(p, (a: Int) => a >= 4))
  }
}
