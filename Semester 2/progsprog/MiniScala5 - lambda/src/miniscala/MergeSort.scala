package miniscala

import java.util.Random

import miniscala.Ast.MiniScalaError

sealed abstract class IntList
case object Nil extends IntList
case class Cons(x: Int, xs: IntList) extends IntList

object MergeSort {
  def main(args: Array[String]): Unit ={
    val list1 = Cons(2, Cons(5, Cons(7, Nil)))
    val list2 = Cons(3, Cons(5, Cons(8, Nil)))
    val list3 = Cons(3, Cons(5, Nil))
    println(merge(list1,list2))
    println(split(Cons(2, Cons(3, Cons(5, Cons(5, Cons(7, Nil))))), 2))
    println(randomIntList())
    println(permuted(list1,list3))
    println("done")
  }

  def append(xs: IntList, x: Int): IntList = xs match {
    case Nil => Cons(x, Nil)
    case Cons(y, ys) => Cons(y, append(ys, x))
  }
  def reverse(xs: IntList): IntList = xs match {
    case Nil => Nil
    case Cons(x, ys) => append(reverse(ys), x)
  }


  def merge(xs: IntList, ys: IntList): IntList = mergee(xs,ys,Nil)
  def mergee(xs: IntList, ys: IntList, ass: IntList):
  IntList = (xs,ys) match{
    case (Nil,Nil) => reverse(ass)
    case (Nil,Cons(z,zs)) => mergee(xs,zs,Cons(z,ass))
    case (Cons(z,zs),Nil) => mergee(zs,ys,Cons(z,ass))
    case (Cons(z,zs),Cons(w,ws)) =>
      if(z<w) mergee(zs,ys,Cons(z,ass))
      else mergee(xs,ws,Cons(w,ass))
  }

  def split(xs: IntList, n: Int): (IntList, IntList) =
    if(length(xs) <= n) (xs,Nil)
    else if(n<0) throw new RuntimeException("Illegal index")
    else splitt(xs,n,Nil)
  def splitt(xs: IntList, n: Int, ass: IntList): (IntList,IntList) = xs match{
    case Cons(z,zs) => if(n>0) splitt(zs,n-1,Cons(z,ass)) else (reverse(ass),xs)
  }

  def ordered(xs: IntList): Boolean = xs match{
    case Nil => true
    case Cons(x,Nil) => true
    case Cons(x,Cons(y,ys)) => if(x<=y) ordered(ys) else false
  }

  def randomIntList(): IntList = randomIntListt(Nil,r.nextInt(101),r)
  val r = new Random()
    def randomIntListt(ass: IntList,n: Int, r: Random): IntList =
      if(n>0) randomIntListt((Cons(r.nextInt(),ass)),n-1,r)
      else ass

  def permuted(xs: IntList, ys: IntList): Boolean =
    if (length(xs) == length(ys)) listChecker(xs, ys) //a necessary condition
    else false
  def boringMerge(xs: IntList,ys: IntList): IntList = xs match{
    case Nil => ys //we are done
    case Cons(z,zs) => boringMerge(zs,Cons(z,ys)) //merges xs and ys with no regard for sequence
  }
  def elementChecker(x: Int, ys: IntList, ass: IntList):
  (Boolean,IntList,IntList) = ys match{
    case Nil => (false,ys,ass) //x was not found in ys
    case Cons(z,zs) =>
      if (x==z) (true,zs,ass) //x was found in ys, return elements after and before x
      else elementChecker(x,zs,Cons(z,ass)) //looks at next value in ys, with z added to accumulator
  }
  def listChecker(xs: IntList,ys: IntList): Boolean = xs match{
    case Cons(x,zs) =>
      val eC = elementChecker(x,ys,Nil) //checks if x is in y
      if(eC._1) listChecker(zs,boringMerge(eC._2,eC._3)) //continues without x in xs and x in ys
      else false
    case Nil => true //xs is empty, and since the length of xs and ys are the same and we remove 1 element from each, ys is empty too
  }

  def testMergeSort(): Unit = ???

  def length(xs: IntList): Int = xs match {
    case Nil => 0
    case Cons(_, ys) => 1 + length(ys)
  }
  def mergeSort(xs: IntList): IntList = {
    val n = length(xs) / 2
    if (n == 0) xs
    else {
      val (left, right) = split(xs, n)
      merge(mergeSort(left), mergeSort(right))
    }
  }
}