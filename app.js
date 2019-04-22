const app = require('http').createServer(response);   //include libraries: http, fs, socket.io and create server to respond to client requests
const fs = require('fs');                   //file system library to work with files
const io = require('socket.io')(app);       //socket.io library to manage socket s

var uMap = new Map(); //Map to tie usernames to socket id
var uRoom = new Map(); //Map to tie socket id to chatroom
var rooms = []; //Array to hold rooms

app.listen(3000);  //Listen on port 3000 localhost  
console.log('Launching Application...');

//request listener for server
function response(req, res) {     // used to create http server on local machine and handle request/response
    let file = "";          //create correct file path for request
    if (req.url == "/") {
        file = __dirname + "/index.html";
    } else {
        file = __dirname + req.url;
    }

    //async read html file
    fs.readFile(file, function (err, page) {
        if (err) {            //404 error
            res.writeHead(404);
            return res.end("Page not found!");
        }
        res.writeHead(200); //write html header 
        res.end(page);      //send data response to webpage
    });
}


io.on("connection", function (socket) {               //Listen for incoming connections  

    console.log(socket.id + " connected")

    socket.on("send uName", function (uName, call) {       //listener to receive and store username
        uMap.set(socket.id, uName);
        console.log(socket.id + " set name to " + uName);
        console.log(uName + " has joined the chatroom!");
        io.sockets.emit("update messages", uName + " has joined the chatroom!");
        call();             //function callback to let new user into chatroom
    });

    socket.on("get rooms", function (call) {
        call(rooms);
    });

    socket.on("create room", function (sent_roomname, call) { //listener to receive and create chatroom
        let roomNumber = rooms.indexOf(sent_roomname);
        if (roomNumber != 1) {
            rooms.push(sent_roomname);
            uRoom.set(socket.id, rooms.indexOf(sent_roomname));
            console.log("created room " + sent_roomname + " with id " + rooms.indexOf(sent_roomname))
            call(0);
        }
        else call(-1);
    });

    socket.on("enter room", function (roomid, call) {
        uRoom.set(socket.id, roomid);
        console.log(socket.id + " joined room id " + roomid);

    });

    socket.on("leave room", function (call) {
        uRoom.delete(socket.id);
        call();
    });

    socket.on("send message", function (sent_msg, call) {              //listen for send message event from client
        let send_msg = "[" + currentTime() + "] " + uMap.get(socket.id) + ": " + sent_msg;
        console.log("[" + uRoom.get(socket.id) + "]" + send_msg);
        io.sockets.emit("update messages", send_msg);                   //emit the message to all sockets currently connected to server
        call();                 //call back to client and clear text box
    });

    socket.on("disconnect", function (reason) {      //socket.io disconnect listener 
        console.log(socket.id + ": " + reason);
        if (uRoom.get(socket.id) == null) {
                console.log(socket.id + " disconnected from server");
        }
        else{
            console.log(uMap.get(socket.id) + " (" + socket.id + ") has left the chatroom!");
            io.sockets.emit("update messages", uMap.get(socket.id) + " has left the chatroom!");  //alert users the a client has left the chatroom
            uMap.delete(socket.id); //deletes the disconnected user
            uRoom.delete(socket.id);
        }

    });
});



function currentTime() {            //helper function to calculate the time a message was sent
    let time = new Date();
    let hour = (time.getHours() % 12 < 10 ? '0' : '') + time.getHours() % 12;
    let minute = (time.getMinutes() < 10 ? '0' : '') + time.getMinutes();
    let second = (time.getSeconds() < 10 ? '0' : '') + time.getSeconds();
    return hour + ":" + minute + ":" + second;
}
