package main
//cd E:uni\github\uni\"semester 3"\dss\goplay
/*A UDP client sending packages to port 10001 on the local
machine. If the server is running on another machine, the 127.0.0.1 would
have to be replaced by the IP address of that machine.*/

//import ( "net" ; "time" ; "strconv" )

import ( "net" ; "time" )
func main() {
	ServerAddr, _ := net.ResolveUDPAddr("udp","127.0.0.1:10001")
	LocalAddr, _ := net.ResolveUDPAddr("udp", "127.0.0.1:0")
	conn, _ := net.DialUDP("udp", LocalAddr, ServerAddr)
	defer conn.Close()
	i := 0
	for {
		i++
		//msg := strconv.Itoa(i)
		msg := "hej"
		conn.Write([]byte(msg)) //marshalling?
		time.Sleep(time.Second * 1)
	}
}