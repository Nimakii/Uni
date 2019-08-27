object DB6b {
  def main(args: Array[String]): Unit = {
    println(mean(List((3,4),(3,6),(3,8),(4,7))))
    println(mean(List((4,5),(5,1),(5,5),(8,4),(9,1),(9,5))))
    println(manD((1,2),(1,3)))
    println(loopy(List((3,4),(3,6),(3,8),(4,7)),List((4,5),(5,1),(5,5),(8,4),(9,1),(9,5))))
  }

  def mean(prg: List[(Int,Int)]): (Double, Double) = {
    val c : Double =1.0/ prg.length
    val res = prg.foldLeft(0,0)((a:(Int,Int),b:(Int, Int)) =>
      (a._1+b._1,a._2+b._2))
    (res._1.toDouble*c,res._2.toDouble*c)
  }

  def td(prg: List[(Int,Int)]): Double = {
    val centroid = mean(prg)
    val res = prg.foldLeft(0.0)((a:Double,b:(Int, Int)) =>
      a+math.pow(manD(b,centroid),2))
    math.pow(res,0.5)
  }

  def manD(point : (Int,Int), centroid: (Double,Double)):Double ={
    math.abs(point._1-centroid._1)+math.abs(point._2-centroid._2)
  }

  def update(prg : List[(Int,Int)],c1 : (Double,Double), c2: (Double,Double) ): (List[(Int,Int)],List[(Int,Int)])={
    var res1= List[(Int,Int)]()
    var res2 = List[(Int,Int)]()
    for(a <- prg){
      import java.text.DecimalFormat
      val df = new DecimalFormat(".####")
      println(a+" &"+df.format(manD(a,c1))+" &"+df.format(manD(a,c2)))
      if(manD(a,c1)<=manD(a,c2)){
        res1 =  a :: res1
      }
      else{
        res2 = a :: res2
      }
    }
    (res1,res2)
  }

  def loopy(prg : (List[(Int,Int)],List[(Int,Int)])): (List[(Int,Int)],List[(Int,Int)]) ={
    val fullList = prg._1 ++ prg._2
    var c1new = mean(prg._1)
    println("First Centroid:"+c1new)
    var c1old = (0.0,0.0)
    var c2new = mean(prg._2)
    println("Second Centroid:"+c2new)
    var c2old = (0.0,0.0)
    var res = prg
    while(!compare(c1new._1, c1old._1,0.0001) && !compare(c1new._2, c1old._2,0.0001) && !compare(c2new._1, c2old._1,0.0001)
      && !compare(c2new._2, c2old._2,0.0001)){
      res = update(fullList,c1new,c2new)
      println("Print res: "+res)
      c1old = c1new
      c1new = mean(res._1)
      println("c1new: "+c1new)
      c2old = c2new
      c2new = mean(res._2)
      println("c2new: "+c2new)
    }
    res
  }

  def compare(x: Double, y: Double, precision: Double)={
    if(math.abs(x-y)<precision){ true}
    else{ false}
  }
}