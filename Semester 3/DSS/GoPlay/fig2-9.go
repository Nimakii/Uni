//cd E:uni\github\uni\"semester 3"\dss\goplay
// cd C:\Users\tove\Desktop\Uni2\"Semester 3"\DSS\GoPlay
package main

import (
	"bufio"
	"fmt"
	"net"
)

func main() {
	addrs, _ := net.LookupHost("www.google.com")
	addr := addrs[0]
	fmt.Println(addr)
	conn, err := net.Dial("tcp", addr+":80") //tcp call to google at port 80
	fmt.Print("here")
	fmt.Println(conn.LocalAddr())
	fmt.Print("no longer here")
	if conn != nil {
		defer conn.Close()
	}
	if err != nil {
		panic(0)
	}
	fmt.Fprintf(conn, "GET /search?q=Secure+Distributed+Systems HTTP/1.1\n")
	fmt.Fprintf(conn, "HOST: www.google.com\n")
	fmt.Fprintf(conn, "\n")
	for {
		msg, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			panic(1)
		}
		fmt.Println(msg)
	}
}
