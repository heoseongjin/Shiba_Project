var net = require('net');
var app = require('./app_server.js');
var que = require('./query.js');


var rb_server = net.createServer(function(socket){
    console.log("rb connected");                    //라즈베리파이 연결
    module.exports.send = function(data){           
        socket.write(data);                         //라즈베리파이로 데이터(명령어) 전송
    }
    socket.on('data', function(data){               
        console.log("rb sent : " + data);           //라즈베리파이에서 받은 데이터 콘솔에 출력
        }
    )

})

rb_server.listen(8403, function(){
    console.log('listening on 8403...');
})