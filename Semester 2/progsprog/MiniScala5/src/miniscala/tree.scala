package miniscala

import miniscala.Ast._
import miniscala.parser._

object tree {
  def main(args: Array[String]): Unit = {
    println(Parser.parse("3*(10/12/(3+1)-1)+7"))
    println(treePrintOuter(Parser.parse("3*(10/12/(3+1)-1)+7")))

  }
  def treePrintOuter(input: Exp): String = {
    val lizt = treePrint2(input,0)
    val max = lizt.foldLeft[Int](0)((a,b) => scala.math.max(a,b._2))
    var res = ""
    for( j <- 0 to max){
      for( i <- lizt.indices){
        if(lizt(i)._2 == j){
          res += lizt(i)._1 +"   "
        }
      }
      res = res + "\n"
    }
    res
  }

  def treePrint2[A >: Exp](input: (A,Int)): List[(String,Int)] = input._1 match{
    case BinOpExp(a,op,b) => List[(String,Int)](("BinOpExp",input._2)) ++ treePrint2(a,input._2+1) ++ treePrint2(op,input._2+1) ++ treePrint2(b,input._2+1)
    case IntLit(c) => List[(String,Int)](("IntLit("+c.toString+")",input._2))
    case UnOpExp(op,c) => List[(String,Int)](("UnOpExp",input._2)) ++ treePrint2(op,input._2+1) ++ treePrint2(c,input._2+1)
    case PlusBinOp() => List[(String,Int)](("PlusBinOp",input._2))
    case MultBinOp() => List[(String,Int)](("MultBinOp",input._2))
    case DivBinOp() => List[(String,Int)](("DivBinOp",input._2))
    case MinusBinOp() => List[(String,Int)](("MinusBinOp",input._2))
    case NegUnOp() => List[(String,Int)](("NegUnOp",input._2))
    case _ => List[(String,Int)](("fejl",0))
  }

}
