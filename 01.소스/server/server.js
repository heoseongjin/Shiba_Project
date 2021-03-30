var net = require('net');
var app = require('./app_server.js');
var que = require('./query.js');
var dog = require('./dog_server.js');
var home = require('./smarthome_socket.js');

var moment = require('moment');

function time(){
    return '['+moment().format("YYYY-MM-DD, HH:mm:ss")+']: ';
}
exports.time = time;

// Raspberry 서버 생성
var rb_server = net.createServer(function(socket){                 
    console.log(time()+"Pi connected");                             //Pi 연결
    exports.appsend = function(data){                               //send 함수가 호출되면
        console.log(time()+"AppServer --> Pi : "+ data);            //콘솔에 app에서 Pi로 보낸는 데이터 출력
        app_rb_data = data;                                         //데이터 옮겨담기
        socket.write(app_rb_data);                                  //rb에 데이터 전송
    }
    exports.dogsend = function(data){
        console.log(time()+"DogServer --> Pi : "+data);
        dog_rb_data = data;
        socket.write(dog_rb_data);
    }
    socket.on('data', function(data){                           //Pi가 데이터를 보내면
        console.log(time()+"Pi --> Server : " + data);                       //콘솔에 Pi에서 보낸 데이터 출력
        if(data == "d"){
            dog.send(data);
        }
        else if(data == "c"){
            que.insert_count();
        }
    })
    socket.on('end', function(data){                                // 소켓 연결 종료 이벤트가 왔을 때 콜백함수 실행
        console.log(time()+"Pi disconnected.");                            // 콘솔창에 연결이 종료되었다고 출력
     });
 
     socket.on('error', function(err){                               // 소켓 연결에 에러 이벤트가 왔을 때 콜백함수 실행
        console.log(time()+err);                                            // 에러를 콘솔창에 출력
     });
})

rb_server.listen(8403, function(){
    console.log('listening on Pi(8403)...');
})
