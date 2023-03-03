import socket,threading

#listen to receive message
def listen_to_resp(socket):
    while True:
        data=socket.recv(1024).decode()
        print("Msg Received : "+data)




if __name__ == '__main__':
    client=socket.socket()
    client.connect(("127.0.0.1",2001))

    #Thread for read message
    thread=threading.Thread(target=listen_to_resp(client))
    thread.start()

    req=""
    while req.strip()!="exit":
        req=input("->")
        client.send(req.encode())
    client.close()