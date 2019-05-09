package miniscala

import scala.util.Random

object Week12 {
  /**sealed abstract class IntList
  case object Nil extends IntList
  case class Cons(x: Int, xs: IntList) extends IntList
*/
  sealed abstract class List[+T]
  case object Nil extends List[Nothing]
  case class Cons[T](x: T, xs: List[T]) extends List[T]

  class Student(val id: Int) extends Comparable[Student] {
    def compareTo(that: Student) = this.id - that.id

    override def toString: String = s"$id"
  }

  class Teacher(val id: Int) extends Comparable[Teacher] {
    def compareTo(that: Teacher) = this.id - that.id

  }


  def main(args: Array[String]): Unit = {
    println(merge(Cons( new Student(2), Cons(new Student(5), Cons(new Student(7), Nil))), Cons(new Student(3), Cons(new Student(5), Nil))))
    println(split(Cons(new Student(2), Cons(new Student(3), Cons(new Student(5), Cons( new Student(5), Cons(new Student(7), Nil))))), 2))
    println(ordered(Cons(new Student(2), Cons(new Student(3), Cons(new Student(5), Cons(new Student(5), Cons(new Student(7), Nil)))))))
    println(ordered(Cons(new Student(2), Cons(new Student(3), Cons(new Student(500), Cons(new Student(5), Cons(new Student(7), Nil)))))))
    //println (randomIntList())

    println(testMergeSort())
    println("finished")
    println(mergeSort(Cons(new Student(200), Cons(new Student(30), Cons(new Student(500), Cons(new Student(400), Cons(new Student(7), Nil)))))))

  }



  def merge[T <: Comparable[T]](xs: List[T], ys: List[T]): List[T] = mergee(xs,ys,Nil)

  /**
    * Helping function for merge method. Easier to return a list this way.
    *
    */
  def mergee[T<:Comparable[T]](xs: List[T], ys: List[T], ass: List[T]):
  List[T] = (xs,ys) match{
    case (Nil,Nil) => reverse(ass)
    case (Nil,Cons(z,zs)) => mergee(xs,zs,Cons(z,ass))
    case (Cons(z,zs),Nil) => mergee(zs,ys,Cons(z,ass))
    case (Cons(z,zs),Cons(w,ws)) =>
      if(z.compareTo(w)<=0) mergee(zs,ys,Cons(z,ass))
      else mergee(xs,ws,Cons(w,ass))
  }

  def split[T](xs: List[T], n: Int): (List[T], List[T]) =
    if( length(xs)<= n)
      (xs,Nil)
    else if (n<0)
      throw new RuntimeException("Illegal index")
    else
      splitt(xs,n,Nil)

  def splitt[T](xs: List[T],n: Int, ass: List[T]) : (List[T],List[T]) = xs match{

    case Cons(z,zs) => if (n>0) splitt(zs,n-1,Cons(z,ass)) else (reverse(ass),xs)

  }


  def ordered[T <: Comparable[T]](xs: List[T]): Boolean = xs match {
    case Nil => true
    case Cons(x,Nil) => true
    case Cons(x,Cons(y,ys)) => if (x.compareTo(y)<=0) ordered(ys) else false
  }






  def permuted[T<:Comparable[T]](xs: List[T], ys: List[T]): Boolean =
    if (length(xs) == length(ys)) listChecker(xs, ys) //a necessary condition
    else false
  def boringMerge[T](xs: List[T],ys: List[T]): List[T] = xs match{
    case Nil => ys //we are done
    case Cons(z,zs) => boringMerge(zs,Cons(z,ys)) //merges xs and ys with no regard for sequence
  }
  def elementChecker[T <: Comparable[T]](x: T, ys: List[T], ass: List[T]):
  (Boolean,List[T],List[T]) = ys match{
    case Nil => (false,ys,ass) //x was not found in ys
    case Cons(z,zs) =>
      if (x.compareTo(z)==0) (true,zs,ass) //x was found in ys, return elements after and before x
      else elementChecker(x,zs,Cons(z,ass)) //looks at next value in ys, with z added to accumulator
  }
  def listChecker[T<:Comparable[T]](xs: List[T],ys: List[T]): Boolean = xs match{
    case Cons(x,zs) =>
      val eC = elementChecker(x,ys,Nil) //checks if x is in y
      if(eC._1) listChecker(zs,boringMerge(eC._2,eC._3)) //continues without x in xs and x in ys
      else false
    case Nil => true //xs is empty, and since the length of xs and ys are the same and we remove 1 element from each, ys is empty too
  }

  def testMergeSort(): Unit =  testMergeSortHelp(100)
  def testMergeSortHelp(n: Int): Unit ={
    if (n > 0){
      val x = randomTeacherList()
      val y = mergeSort(x)
      assert(ordered(y))
      assert(permuted(x,y))
      testMergeSortHelp(n - 1)
    }
  }




  def mergeSort[T <: Comparable [T]](xs: List[T]): List[T] = {
    val n = length(xs) / 2
    if (n == 0) xs
    else {
      val (left, right) = split(xs, n)
      merge(mergeSort(left), mergeSort(right))
    }
  }


  /**
    * Helping functions
    */
  def reverse[T](xs: List[T]): List[T] = xs match {
    case Nil => Nil
    case Cons(x, ys) => append(reverse(ys), x)
  }
  def append[T](xs: List[T], x: T): List[T] = xs match {
    case Nil => Cons(x, Nil)
    case Cons(y, ys) => Cons(y, append(ys, x))
  }
  def length[T](xs: List[T]): Int = xs match {
    case Nil => 0
    case Cons(_, ys) => 1 + length(ys)
  }

  def randomStudentList(): List[Student] = randomStudentListt(Nil,new Random().nextInt(101))
  def randomStudentListt(ass : List[Student],n:Int): List[Student] = {
    if(n>0)
      randomStudentListt(Cons(new Student( new Random().nextInt()),ass),n-1)
    else
      ass
  }
  def randomTeacherList(): List[Teacher] = randomTeacherListt(Nil,new Random().nextInt(101))
  def randomTeacherListt(ass : List[Teacher],n:Int): List[Teacher] = {
    if(n>0)
      randomTeacherListt(Cons(new Teacher( new Random().nextInt()),ass),n-1)
    else
      ass
  }

}