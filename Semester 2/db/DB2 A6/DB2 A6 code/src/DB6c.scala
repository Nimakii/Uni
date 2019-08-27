object DB6c{
def main(args: Array[String]): Unit = {
  val print = TFIDFweights(List(1,1,1,2,2,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                           List(0,1,2,0,2,2,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                           List(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1))
  println(print._1)
  println(print._2)
  println(print._3)
  println(CosSim(print._1,print._2))
  println(CosSim(print._1,print._3))
  println(CosSim(print._2,print._3))
  println(math.log(math.exp(1)))
}
  def TFIDFweights(list1 : List[Int],list2 : List[Int], list3: List[Int]): (List[Double],List[Double],List[Double]) = {
    var res1: List[Double] = List()
    var res2: List[Double] = List()
    var res3: List[Double] = List()
    val max1: Double = list1.foldLeft(0)((a, b) => math.max(a, b))
    val max2: Double = list2.foldLeft(0)((a, b) => math.max(a, b))
    val max3: Double = list3.foldLeft(0)((a, b) => math.max(a, b))
    for (i <- list1.indices) {
      var b = 0
      if (list1(i) > 0) {
        b += 1
      }
      if (list2(i) > 0) {
        b += 1
      }
      if (list3(i) > 0) {
        b += 1
      }
      res1 = list1(i).toDouble / max1 * math.log(3.0 / b) :: res1
      res2 = list2(i).toDouble / max2 * math.log(3.0 / b) :: res2
      res3 = list3(i).toDouble / max3 * math.log(3.0 / b) :: res3
    }
    (res1.reverse, res2.reverse, res3.reverse)
  }
  def CosSim(list1: List[Double],list2: List[Double]): Double = {
    val length1:Double = math.pow(list1.foldLeft(0.0)((a,b)=>a+math.pow(b,2)),0.5)
    val length2:Double = math.pow(list2.foldLeft(0.0)((a,b)=>a+math.pow(b,2)),0.5)
    val dotproduct:Double = list1.zip(list2).foldLeft(0.0)((a,b)=> a+b._1*b._2)
    println(length1+", "+length2+", "+dotproduct)
    dotproduct/(length1*length2)
  }

}
