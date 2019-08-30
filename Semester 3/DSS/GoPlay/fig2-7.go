package main

//cd E:uni\github\uni\"semester 3"\dss\goplay

import (
	"bufio"
	"fmt"
	"net"
	"os"
)

var conn net.Conn

func main() {
	conn, _ = net.Dial("tcp", "10.192.45.33:18081")
	defer conn.Close()
	for {
		reader := bufio.NewReader(os.Stdin)
		fmt.Print("> ")
		text, err := reader.ReadString('\n')
		if text == "quit\n" {
			return
		}
		fmt.Fprintf(conn, text)
		msg, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			return
		}
		fmt.Print("From server: " + msg)
	}
}
