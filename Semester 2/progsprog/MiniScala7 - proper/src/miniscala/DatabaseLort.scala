package miniscala
object DatabaseLort {

  def main(args: Array[String]): Unit = {
    /*'println("outlook and class")
    println(gainz(List(5,9),List((3,2),(4,0),(2,3))))
    println("temp and class")
    println(gainz(List(5,9),List((2,2),(4,2),(3,1))))
    println("hum and class")
    println(gainz(List(5,9),List((3,4),(6,1))))
    println("windy and class")
    println(gainz(List(5,9),List((6,2),(3,3))))
*/
    println("temp and rain")
    println(gainz(List(3,2),List((0,0),(2,1),(1,1))))
    println("hum and rain")
    println(gainz(List(5,9),List((1,1),(2,1))))
    println("wind and rain")
    println(gainz(List(5,9),List((3,0),(0,2))))
  }
  def entropy(prg : List[Int]): Double = {
    var sum :Double = 0
    val prgSum = prg.reduce((x,y) => x+y)
    var strg = "-("
    for (i <- prg){
      if(i==0){
        strg += "0+"
      }
      else
        sum = sum + (i.toDouble/prgSum)*math.log(i.toDouble/prgSum)/math.log(2)
      strg += " ("+i.toDouble +" / " +prgSum +""") \text{ log}_2( """+ i.toDouble+"/"+ prgSum+")+"
    }
    println(strg+"😞"+ -sum)
    -sum
  }

  def gainz(T : List[Int],A : List[(Int,Int)]):Double ={
    var res : Double = entropy(T)
    var strg : String = res.toString+ "-("
    var strg2 = strg
    val sum:Double = A.reduce((x,y)=> (x._1+x._2+y._1+y._2,0))._1
    for(i <- A){
      res += -(i._1+i._2).toDouble/sum * entropy(List(i._1,i._2))
      strg = strg +s"("+i._1+"+"+i._2+")/"+sum+""" \cdot"""+" E("+i._1+","+i._2+")+"
      strg2 = strg2 +s"("+i._1+"+"+i._2+")/"+sum+""" \cdot """+entropy(List(i._1,i._2))+")+"
    }
    strg +=")"
    println(strg)
    println(strg2+")")
    res
  }

}