package miniscala



object travian {
  val merchantSpeed: Double = 5 // min pr distance

  def main(args: Array[String]): Unit = {
    val wonder = (50,0,"wonder")
    val aarhus = (44,12,"aarhus")
    val oslo = (47,7,"oslo")
    val kalmar = (64,16,"kalmar")
    val potential = (53,4,"potential")
    val stockholm = (76,14,"Stockholm")
    val newplace = (69,15,"mby")
    val kalmarToPotential1 = (56,9,"kalmar road 1")
    val kalmarToPotential2 = (59,13,"kalmar road 2")
    dPrint(potential,kalmar)
    dPrint(potential,kalmarToPotential1)
    dPrint(kalmarToPotential1,kalmarToPotential2)
    dPrint(kalmarToPotential2,kalmar)
    dPrint(kalmar,newplace)
    dPrint(newplace,stockholm)
    dPrint(kalmar,stockholm)
    dPrint(wonder,stockholm)
    dPrint(wonder,oslo)
    dPrint(aarhus,oslo)
    dPrint(oslo,kalmar)
    dPrint(aarhus,kalmar)
    dPrint(oslo,potential)
    dPrint(potential,wonder)
    dPrint(potential,kalmar)
  }

  def dPrint(a:(Int,Int,String),b:(Int,Int,String)): Unit =
    println("The distance from "+a._3+" to "+b._3+" is >>"+d(a,b).toInt+"<<, and you can send teams of "+merchant(a,b)+" merchants pr hour ")

  def d(a:(Int,Int,String),b:(Int,Int,String)): Double =
    math.sqrt((a._1 - b._1)*(a._1 - b._1) + (a._2 - b._2)*(a._2 - b._2))

  def merchant(a:(Int,Int,String),b:(Int,Int,String)): Int = {
    val speed = d(a, b) * merchantSpeed / 60
    if (0 <= speed && speed < 0.5) 20
    else if (0.5 <= speed && speed < 1) 10
    else if (1 <= speed && speed < 1.5) 6
    else 5
  }
}
