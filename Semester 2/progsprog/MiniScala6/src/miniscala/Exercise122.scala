package miniscala

object Exercise122 {

  def main(args: Array[String]) = {

    /**
      * Lists.
      */
    sealed abstract class List[+T] {

      def filter(p: T => Boolean): List[T] = this match {
        case Nil => Nil
        case Cons(y, ys) =>
          val r = ys.filter(p)
          if (p(y)) Cons(y, r) else r
      }

      def map[U](f: T => U): List[U] = this match {
        case Nil => Nil
        case Cons(y, ys) => Cons(f(y), ys.map(f))
      }

      def foldRight[B](z: B, f: (T, B) => B): B = this match {
        case Nil => z
        case Cons(h, t) => f(h, t.foldRight(z, f))
      }
    }

    case object Nil extends List[Nothing]

    case class Cons[T](x: T, xs: List[T]) extends List[T]


    /**
      * Streams.
      */
    sealed abstract class Stream[+T] {

      override def toString(): String = this match {
        case SCons(x, xs) => x().toString + ", " + xs().toString()
        case SNil => ""
      }

      def head(): T = this match {
        case SNil => throw new RuntimeException("stream is empty")
        case SCons(x, _) => x()
      }

      def tail(): Stream[T] = this match {
        case SNil => throw new RuntimeException("stream is empty")
        case SCons(_, xs) => xs()
      }

      def map[U](f: T => U): Stream[U] = this match {
        case SNil => SNil
        case SCons(x, xs) => SCons(() => f(x()), () => xs().map(f))
      }
      def mapFold[U](f: T => U): Stream[U] = {
        foldRight(()=>SNil,(h,s)=> SCons(() => f(h),s))
      }
      def foldRight[B](z: () => B, f: (T, () => B) => B): B = this match {
        case SNil => z()
        case SCons(h, t) => f(h(), () => t().foldRight(z, f))
      }
      def foreach(f: T => Unit): Unit = this match {
        case SNil =>
        case SCons(x, xs) =>
          f(x())
          xs().foreach(f)
      }

      def filter(p: T => Boolean): Stream[T] = this match {
        case SNil => SNil
        case SCons(y, ys) =>
          if (p(y())) SCons(y, () => ys().filter(p)) else ys().filter(p)
      }

      def zip[U](ys: Stream[U]): Stream[(T, U)] = (this, ys) match {
        case (SNil, SNil) => SNil
        case (SNil, SCons(_, _)) => SNil
        case (SCons(_, _), SNil) => SNil
        case (SCons(x, xs), SCons(z, zs)) => SCons(() => (x(), z()), () => xs().zip(zs()))
      }

      def take(n: Int): Stream[T] =
        if (n == 0) SNil else SCons(() => head(), () => tail().take(n - 1))



      def toList(): List[T] = this match {
        case SNil => Nil
        case SCons(x, xs) => Cons(x(), xs().toList())
      }

    }

    case object SNil extends Stream[Nothing]

    case class SCons[T](x: () => T, xs: () => Stream[T]) extends Stream[T]


    val s1: Stream[Int] =
      SCons(() => 1, () =>
        SCons(() => 2, () =>
          SCons(() => 3, () =>
            SNil)))

    //println(s"first element of s1: ${s1.head()}")
    //println(s"second element of s1: ${s1.tail().head()}")

    def listToStream[T](xs: List[T]): Stream[T] = xs match {
      case Nil => SNil
      case Cons(y, ys) => SCons(() => y, () => listToStream(ys))
    }

    val ones: Stream[Int] = {
      def gen(): Stream[Int] = SCons(() => 1, () => gen())

      gen()
    }

    val nats: Stream[Int] = {
      def gen(n: Int): Stream[Int] = SCons(() => n, () => gen(n + 1))

      gen(0)
    }

    val fibs: Stream[Int] = {
      def gen(a: Int, b: Int): Stream[Int] = SCons(() => a, () => gen(a + b, a))

      gen(0, 1)
    }

    //println("Fibonacci:")
    //fibs.take(25).foreach(println)

    def sieve(xs: Stream[Int]): Stream[Int] =
      SCons(() => xs.head(), () => sieve(xs.tail().filter(x => x % xs.head() != 0)))

    val primes = sieve(nats.tail().tail())

    //println("Primes:")
    //primes.take(100).foreach(println)

    def fibs2(): Stream[Int] =
      SCons(() => 0, () =>
        SCons(() => 1, () =>
          fibs2().zip(fibs2().tail()).map(n => n._1 + n._2)))

    //println("Fibonacci again:")
    //fibs2().take(25).foreach(println)

    //println(s"Fibonacci as list: ${fibs2().take(25).toList()}")

    class Sighting(
                    val animal: String, // Which animal
                    val spotter: Int, // Who saw it
                    val count: Int, // How many
                    val area: Int, // Where
                    val period: Int // When
                  )

    val sightings: List[Sighting] =
      Cons(new Sighting("Elephant", 17, 5, 3, 1),
        Cons(new Sighting("Lion", 2, 5, 3, 2),
          Cons(new Sighting("Elephant", 2, 3, 3, 2),
            Nil)))

    val elephants1: Int =
      sightings.filter(s => s.animal == "Elephant")
        .map(s => s.count)
        .foldRight(0, (x: Int, res: Int) => x + res)
    //println(s"Elephant sightings: $elephants1")

    val elephants2: Int =
      listToStream(sightings).filter(s => s.animal == "Elephant")
        .map(s => s.count)
        .foldRight(() => 0, (x: Int, res: () => Int) => x + res())
    //println(s"Elephant sightings: $elephants2")

    def f(x: Int): Int = x * x * x

    val s: Stream[Int] = SCons(() => f(1), () => SCons(() => f(2), () => SCons(() => f(3), () => SNil)))
    println(s.tail())
    println(s.head())
    println(s.tail().tail())
    println(s.tail().head())


    def unfoldRight[A, S](z: S, f: S => Option[(A, S)]): Stream[A] =
      f(z) match {
        case Some((h, s)) => SCons(() => h, () => unfoldRight(s, f))
        case None => SNil
      }
    val onez = unfoldRight(1,(x: Int)=>Option(1,1))
    println(onez.take(25))
    val natz = unfoldRight(0,(x:Int)=> Option(x,x+1))
    println(natz.take(25))
    val g: (Int,Int) => Option[(Int,(Int,Int))] = (x,y) => Option((x,(y,x+y)))
    val fibz = unfoldRight[Int,(Int,Int)]((0,1),x => Option((x._1,(x._2,x._1+x._2))))
    println(fibz.take(25))

    val cyclic = unfoldRight(0,(x: Int) => if(x==0)Option(0,1) else Option(1,0))
    println(cyclic.take(25))

    val elephants3: Int =
      sightings.filter(s => { println("filtering"); s.animal == "Elephant" })
        .map(s => { println("counting"); s.count })
        .foldRight(0, (x: Int, res: Int) => { println("adding"); x + res})
    println(s"Elephant sightings: $elephants3")

    val elephants4: Int =
      listToStream(sightings).filter(s => { println("filtering"); s.animal == "Elephant" })
        .map(s => { println("counting"); s.count})
        .foldRight(() => 0, (x: Int, res: () => Int) => {println("adding?"); val t = res(); println("adding"); x + t})
    println(s"Elephant sightings: $elephants4")
  }
}