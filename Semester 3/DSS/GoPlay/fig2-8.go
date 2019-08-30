//cd E:uni\github\uni\"semester 3"\dss\goplay
package main

import ( "net" ; "fmt" ; "strconv" )

func main() {
	addrs, _ := net.LookupHost("google.com") //ask the inet for an IP
	for indx, addr := range addrs {
		fmt.Println("Address number " + strconv.Itoa(indx) + ": " + addr)
	}
	addrs2, _ := net.LookupHost("youtube.com")
	fmt.Println("Youtube IP: "+addrs2[0])
	addrs3, _ := net.LookupHost("pornhub.com")
	fmt.Println("Pornhub IP: "+addrs3[0])
}