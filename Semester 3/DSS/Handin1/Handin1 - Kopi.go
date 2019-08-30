package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
)

func handleConnection(conn net.Conn) {
	defer conn.Close()

}

func main() {
	SetupConnection()
}

func SetupConnection() {
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("Please input target IP: ")
	targetIP, _ := reader.ReadString('\n')
	fmt.Print("Please input target port: ")
	targetPORT, _ := reader.ReadString('\n')
	fmt.Println("You have chosen this IP: " + targetIP)
	fmt.Println("You have chosen this Port: " + targetPORT)
	conn, connErr := net.Dial("tcp", targetIP+":"+targetPORT)
	if connErr == nil {
		fmt.Println("Connection established!")
		go handleConnection(conn)
	} else {
		fmt.Println("Connection failed!")
	}
	SetupListner()
}

// cd d:\projekter\go
// cd C:\Users\tove\Desktop\Uni2\"Semester 3"\DSS

func SetupListner() {
	fmt.Println("Setting up port listner.")
	newListner, _ := net.Listen("tcp", ":")
	defer newListner.Close()
	_, myPort, _ := net.SplitHostPort(newListner.Addr().String()) //From figure 2.5

	addrs, _ := net.InterfaceAddrs()
	var currentIP string

	for _, address := range addrs {
		if ipnet, ok := address.(*net.IPNet); ok && !ipnet.IP.IsLoopback() { //check 1, "indeholder denne addr en gyldig ip", check 2
			if ipnet.IP.To4() != nil {
				currentIP = ipnet.IP.String()
			}
		}
	}

	for {
		fmt.Println("My IP: " + currentIP)
		fmt.Println("Listening on port: " + myPort)
		conn, _ := newListner.Accept()
		fmt.Println("Someone connected to me!")
		go handleConnection(conn)
	}
}
