package main
//cd E:uni\github\uni\"semester 3"\dss\goplay
/*Figure 2.6 A TCP Server. We learn some more Go. The function
strings.Title simply capitalises each word in the string. The defer
keyword defers the function call until the enclosing function terminates. It is
a good way to ensure that for instance connections are closed no matter what
happens later: The deferred work is executed no matter how the function is
exited, even if there is a panic. If you put the closing of connections at the
end of the function, they would not be closed if you exit the function earlier
in the code or the function terminated because of a panic.*/
import ( "net" ; "fmt" ; "bufio" ; "strings" )

func handleConnection(conn net.Conn) {
	defer conn.Close() //defer ensures this closes even if something goes wrong
	for {
		msg, err := bufio.NewReader(conn).ReadString('\n')
		if (err != nil) {
			fmt.Println("Error: " + err.Error())
			return
		} else {
		fmt.Print("From Client:", string(msg)) //unmarshalling?
		titlemsg := strings.Title(msg) //capitalize every beginning letter
		conn.Write([]byte(titlemsg)) //sends the capitalized string back to the client
		}
	}
}

func main() {
	fmt.Println("Listening for connection...")
	ln, _ := net.Listen("tcp", ":18081")
	defer ln.Close()
	conn, _ := ln.Accept()
	fmt.Println("Got a connection...")
	handleConnection(conn)
}