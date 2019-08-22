package miniscala

object A7 {

  def main(args: Array[String]): Unit = {

  }

  sealed abstract class List2[T]
  case object Nil extends List2[Any]
  case class Cons[T](x: T, xs: List2[T]) extends List2[T]

  type Set2[A] = List2[A]

  def length[T](xs: List2[T]): Int = xs match {
    case Nil => 0
    case Cons(_, ys) => 1 + length(ys)
  }

  def makeEmpty[A](): Set2[A] = Nil[A]()

  def isEmpty[A](set: Set2[A]): Boolean = set match {
    case Nil => true
    case Cons(_, _) => false
  }

  def size[A](set: Set2[A]): Int = length(set)

  def add[A](set: Set2[A], x: A): Set2[A] = if(contains(set,x)) set else Cons(x,set)

  def contains[A](set: Set2[A], x: A): Boolean = set match {
    case Nil => false
    case Cons(y, ys) => if (x == y) true else contains(ys, x)
  }

  def remove[A](set: Set2[A], x: A): Set2[A] = {
    def r[T](s: Set2[T],x: T,ass: Set2[T]): Set2[T] = s match{
      case Nil => ass
      case Cons(x,ys) => r(ys,x,ass)
      case Cons(y,ys) => r(ys,x,Cons(y,ass))
    }
    r(set,x,Nil[A]())
  }
  def union[A](set1: Set2[A], set2: Set2[A]): Set2[A] = set2 match{
    case Nil => set1
    case Cons(x,xs) => union(add(set1,x),xs)
  }
  def intersection[A](set1: Set2[A], set2: Set2[A]): Set2[A] = {
    def inter[A](s1: Set2[A], s2: Set2[A], ass: Set2[A]): Set2[A] = s1 match{
      case Nil => ass
      case Cons(x,xs) => if(contains(s2,x)) inter(xs,s2,Cons(x,ass)) else inter(xs,s2,ass)
    }
    inter(set1,set2,Nil())
  }
  def difference[A](set1: Set2[A], set2: Set2[A]): Set2[A] = {
    def diff[T](s1: Set2[T], s2: Set2[T], ass: Set2[T]): Set2[T] = s1 match{
      case Nil => ass
      case Cons(x,xs) => if(contains(s2,x)) diff(xs,s2,ass) else diff(xs,s2,Cons(x,ass))
    }
    diff(set1,set2,Nil())
  }

  def listToSet2[T](list: List[T]): Set2[T] = {
    var ass = makeEmpty[T]()
    for(a <- list){
      ass = add(ass,a)
    }
    ass
  }
  def foldRight[A,B](xs: Set2[A], z: B, f: (A, B) => B): B = xs match {
    case Nil => z
    case Cons(y, ys) => f(y, foldRight(ys, z, f))
  }
  def foldLeft[A,B](xs: Set2[A], z: B, f: (B, A) => B): B = xs match {
    case Nil => z
    case Cons(y, ys) => foldLeft(ys, f(z, y), f)
  }
}
