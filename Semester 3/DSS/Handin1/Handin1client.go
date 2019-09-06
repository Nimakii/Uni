package main

import ( "net" 
"fmt" 
"bufio" 
"os" 
"strings" 
"encoding/gob" )

var connectionList []net.Conn		//Listen over forbindelser
var MessagesSent map[string]bool	//Tjekliste over sendte beskeder

type ToSend struct{
	Msg string
}

func handleConnection(conn net.Conn) { //Modtager og flood'er beskeder
	defer conn.Close()
	msg := &ToSend{}
	for {
		dec := gob.NewDecoder(conn)
		err := dec.Decode(msg)
		if err != nil {return}
		flood(msg.Msg)
		/*msg, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			fmt.Println("Connection lost!")
			return
		} else {
			dec := gob.NewDecoder(conn)
			dec.Decode(msg)
			flood(msg)
		}*/
	}
}

func flood(msg string) { //Sender nye input besked til alle forbindelser i "connectionList"
	//uniqueness check
	if(MessagesSent[msg]==true){
		return
		//Do nothing. Maybe....
	} else {
		fmt.Println(msg + " flood reached")
		ts := &ToSend{}
		ts.Msg = msg
		for _, element := range connectionList {
			//fmt.Println("connectionClient")
			//element.Write([]byte(msg))
			enc := gob.NewEncoder(element)
			//MessagesSent[msg] = true
			enc.Encode(ts)
		}
		MessagesSent[msg] = true
	}
	
}

func main() {
	MessagesSent = make(map[string]bool)
	SetupConnection()
}

func SetupMessenger(){ //Lader client skrive beskeder, som flood'es
	for {
		reader := bufio.NewReader(os.Stdin)
		fmt.Print("> ")
		text, _ := reader.ReadString('\n')
		flood(strings.TrimSpace(text))
	}
}

func SetupConnection() { //Lader nye clienter forbinde til eksisterende netværk. Ved fejl startes eget netværk
	/*reader := bufio.NewReader(os.Stdin)
	fmt.Print("Please input target IP: ")
	targetIP, _ := reader.ReadString('\n')
	fmt.Print("Please input target port: ")
	targetPORT, _ := reader.ReadString('\n')
	targetIP = strings.TrimSpace(targetIP) //trimspace fjerner mellemrum og linjeskift
	targetPORT = strings.TrimSpace(targetPORT)
	fmt.Println("Your target is: " + targetIP + ":" + targetPORT)
	conn, connErr := net.Dial("tcp", targetIP + ":" + targetPORT)*/
	conn, connErr := net.Dial("tcp", "192.168.1.121:18081")
	if connErr == nil {
		fmt.Println("Connection established!")
		connectionList = append(connectionList, conn)
		go handleConnection(conn)
	} else {
		fmt.Println("Connection failed!")
	}
	go SetupListener()
	SetupMessenger()
}
// cd d:\projekter\go
//Steen: 10.192.45.33
//cd E:\Uni\GitHub\Uni\"Semester 3"\DSS\Handin1
//JK: 10.192.72.226


func SetupListener() { //Opsæt af portaflytning.
	addrs, _ := net.LookupHost("www.google.com") //nemmeste måde at finde egen ip
	addr := addrs[0]
	connGoogle, _ := net.Dial("tcp", addr+":80") //tcp call to google at port 80
	currentIP := connGoogle.LocalAddr()
	connGoogle.Close()

	fmt.Println("Setting up port listner.")
	newListener, _ := net.Listen("tcp", ":18082")							//Listen to port 18081
	defer newListener.Close()

	fmt.Print("My connection info: ")
	fmt.Println(currentIP)
	
	for { //Holder port åben og venter på nye forbindelser
		fmt.Println("Awaiting connection...")
		conn, _ := newListener.Accept()
		fmt.Println("Someone connected to me!")
		connectionList = append(connectionList, conn)
		go handleConnection(conn)
	}
}