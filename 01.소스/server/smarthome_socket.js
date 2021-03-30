var net = require('net');
var app = require('./app_server.js');
rb = require('./server.js');


var moment = require('moment');

var socket = net.connect({port:8480, host:'192.168.13.17'});
socket.on('connect', function(){
    console.log(rb.time() + 'SmartHome_Server connected');
    exports.send_to_home = function(data){                              
        console.log(rb.time()+"AppServer --> SmartHome : "+ data);           
        socket.write(data);                                                
    }
});

socket.on('end', function(){
    console.log(rb.time() + 'Market_Server disconnected');
});

socket.on('error', function(err){
    console.log(err);
});
