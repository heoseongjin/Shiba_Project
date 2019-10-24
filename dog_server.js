var net = require('net');           
rb = require('./server.js');

var moment = require('moment');

// 서버 생성
var dog_server = net.createServer(function(socket){                   
    console.log(rb.time()+'Cloud connected');                                //DOG 인식 플랫폼 연결

    exports.send = function(data){
        console.log(rb.time()+"Server --> Cloud : " + data);
        rb_dog_data = data
        socket.write(rb_dog_data);
    }
    
    socket.setEncoding("utf8");                                     //데이터 UTF-8로 인코딩
    socket.on('data', function(data){                               //데이터를 받았을때
        console.log(rb.time()+"Cloud --> DogServer : " + data);                          //콘솔에 DOG로 부터 받은 데이터 출력
        rb.dogsend(data);
    });

    socket.on('end', function(data){                                // 소켓 연결 종료 이벤트가 왔을 때 콜백함수 실행
       console.log(rb.time()+'Cloud disconnected.');                            
	// 콘솔창에 연결이 종료되었다고 출력
    });

    socket.on('error', function(err){                               // 소켓 연결에 에러 이벤트가 왔을 때 콜백함수 실행
       console.log(rb.time()+err);                                            // 에러를 콘솔창에 출력
    });
})

dog_server.listen(8401, function(){
    console.log('listening on Cloud(8401)...');
})
