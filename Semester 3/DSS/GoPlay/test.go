package main

import ( "net"
"fmt"
)

func main(){
	addrs, _ := net.LookupHost("www.google.com") //nemmeste måde at finde egen ip
	addr := addrs[0]
	connGoogle, _ := net.Dial("tcp", addr+":80") //tcp call to google at port 80
	currentIP := connGoogle.LocalAddr()
	connGoogle.Close()
	fmt.Println(currentIP)
}