var net = require('net');
var app = require('./app_server.js');
var que = require('./query.js');


var rb_server = net.createServer(function(socket){
    console.log("rb connected");                    
    exports.send = function(data){
        console.log("App --> RB : "+ data);
        rb_data = data;
        socket.write(rb_data);
    }
    socket.on('data', function(data){
        console.log("rb sent : " + data);
        })
})

rb_server.listen(8403, function(){
    console.log('listening on 8403...');
})