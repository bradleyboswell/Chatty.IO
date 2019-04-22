const app = require('http').createServer(response);   //include libraries: http, fs, socket.io and create server to respond to client requests
const fs = require('fs');                   //file system library to work with files
const io = require('socket.io')(app);       //socket.io library to manage socket s

let myMap = new Map();

app.listen(3000);  //Listen on port 3000 localhost  
console.log('Launching Application...');

//request listener for server
function response(req,res){     // used to create http server on local machine and handle request/response
    let file = "";          //create correct file path for request
    if(req.url == "/"){                           
        file = __dirname + "/index.html";
    }else{
        file = __dirname + req.url;
    }

    //async read html file
    fs.readFile(file, function(err,page){
        if(err){            //404 error
            res.writeHead(404);
            return res.end("Page not found!");
        }
        res.writeHead(200); //write html header 
        res.end(page);      //send data response to webpage
    });
}


io.on("connection", function(socket){               //Listen for incoming connections  
    
    socket.on("send uName", function(uName,call){       //listener to receive and store username
        myMap.set(socket.id, uName);
        console.log(socket.id + " set name to " + uName);
        console.log(uName + " has joined the chatroom!");
        io.sockets.emit("update messages", uName + " has joined the chatroom!");    
        call();             //function callback to let new user into chatroom
    })
    
    socket.on("send message",function(sent_msg, call){              //listen for send message event from client
        let send_msg = "[" + currentTime() + "] "+ myMap.get(socket.id) +": "+ sent_msg;
        console.log(send_msg);
        io.sockets.emit("update messages", send_msg);                   //emit the message to all sockets currently connected to server
        call();                 //call back to client and clear text box
    });
   
    socket.on('disconnect', function(reason) {      //socket.io disconnect listener 
        console.log(reason);            
        console.log(myMap.get(socket.id) + " has left the chatroom!");      
        io.sockets.emit("update messages", myMap.get(socket.id)+ " has left the chatroom!");  //alert users the a client has left the chatroom
        myMap.delete(socket.id); //deletes the disconnected user
    });
});



function currentTime() {            //helper function to calculate the time a message was sent
    let time = new Date();
    let hour = (time.getHours()%12 < 10 ? '0' : '') + time.getHours()%12;
    let minute = (time.getMinutes() < 10 ? '0' : '') + time.getMinutes();
    let second = (time.getSeconds() < 10 ? '0' : '') + time.getSeconds();
    return hour + ":" + minute + ":" + second;
}
