package miniscala

object Test {
  def main(args: Array[String]): Unit ={
    /**positive*/
    assert(1+1==2)
    assert(1-1==0)
    assert(1*1==1)
    assert(2*3==6)
    assert(1*0==0)
    assert(10%8 == 2)
    /**negative*/
    assert(1+1!=3)
    assert(1-1!=1)
    assert(1*1!=2)
    assert(2*3!=8)
    assert(1*0!=3)
    assert(10%8 != 10)

  }
}
