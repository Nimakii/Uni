package main
import ( "net" ; "os" ; "fmt" ; "strconv" )
func main() {
name, _ := os.Hostname()
addrs, _ := net.LookupHost(name)
fmt.Println("Name: " + name)
for indx, addr := range addrs { // for loop indx iterates through the indices and addr the elements, pretty smart
fmt.Println("Address number " + strconv.Itoa(indx) + ": " + addr) //strconv.Itoa(indx) can be replaced by indx, probably implicit call to the same?
}
}

/* output
Name: DESKTOP-V9M34NU
Address number 0: fe80::4cd0:3ecb:8d56:c7e%Ethernet 2
Address number 1: 192.168.1.121
*/

// cd C:\Users\tove\Desktop\Uni2\"Semester 3"\DSS\GoPlay