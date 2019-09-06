package main
/*
import (
	"fmt"
	"io/ioutil"
	"net/http"
)
func main() {
	url := "https://api.ipify.org?format=text"	// we are using a pulib IP API, we're using ipify here, below are some others
                                              // https://www.ipify.org
                                              // http://myexternalip.com
                                              // http://api.ident.me
                                              // http://whatismyipaddress.com/api
	fmt.Printf("Getting IP address from ipify ...\n")
	resp, err := http.Get(url)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()
	ip, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}
	fmt.Printf("My IP is:%s\n", ip)
}
*/

import(
	"fmt"
	"net"
)

func main(){
	addrs, _ := net.LookupHost("www.google.com") //nemmeste måde at finde egen ip
	addr := addrs[0]
	connGoogle, _ := net.Dial("tcp", addr+":80") //tcp call to google at port 80
	currentIP := connGoogle.LocalAddr()
	connGoogle.Close()
	fmt.Print(currentIP)

	fmt.Println("Setting up port listner.")
	newListener, _ := net.Listen("tcp", ":18081")//Listen to port 18081
	defer newListener.Close()
	fmt.Print(newListener.Addr())
}