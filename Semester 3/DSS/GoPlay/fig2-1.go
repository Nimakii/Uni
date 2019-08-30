package main
//cd E:uni\github\uni\"semester 3"\dss\goplay


import "fmt"

func multi(x int, y int) (string,int){
    return "the answer is", x+y
}

//A class
type Named struct {
    name string //Member of a class
}

func (nm *Named) printName(n int){ //(nm *named) makes this function a method of the class Named, hence refered to as nm
    if n<0 {panic(-1)} //panic is an exception
    for i:=0; i<n;i++{
        fmt.Print(nm.name+"\n") //\n is a new line command so this is the same as .Println
    }
}

func main(){
    var i int   //decl
    i = 21      //assignment
    j := 21     //both

    decr := func() int {
        j=j-7
        return j
    }

    str, m := multi(i,j)
    fmt.Println(str,m)
    defer fmt.Println(str,m) //adding defer makes the print come last, "run after return or a panic"

    fmt.Println(decr())
    fmt.Println(decr())

    nm1 := Named{name:"din mor"}
    nm2 := &Named{}

    nm1.printName(2)
    nm2.printName(2)
    nm2.name = "testEnHest" ; nm2.printName(2)
    nm2.name = "TESTeNhEST"
    var nm3 *Named = nm2; //explicitely declared pointer
    nm3.printName(2)
    nm3.printName(-5) //cause 'panic'
    fmt.Println("Will we make it here?")
}

