var net = require('net');                                       // 소켓 서버를 위한 모듈
var que = require('./query.js');
rb = require('./server.js');

// 서버 생성
var app_server = net.createServer(function(socket){                   
    console.log('App connected');                                   //어플 연결

    socket.setEncoding("utf8");                                     //데이터 UTF-8로 인코딩
    socket.on('data', function(data){                               //데이터를 받았을때
        console.log("App sent : " + data);                          //콘솔에 app으로 부터 받은 데이터 출력
        if(data == "s"){                                            //사료 데이터(테이블)를 요청하는 명령어가 오면
            que.select('feed', function(db){                        //select문 실행
                console.log(db);                    
                socket.write(db);                                   //app에 데이터값 전송
            })
        }else if(data == "g"){                                      //간식 데이터(테이블)를 요청하는 명령어가 오면
            que.select('snack', function(db){                       //select문 실행
                console.log(db);                    
                socket.write(db);                                   //app에 데이터값 전송
            })
        }else{                                                      //그 외 명령어가 오면
            try{
                rb.appsend(data);                                   //server.js의 send 함수로 data 전송
                socket.write("RB recieved: "+ data);                //전송이 되었음을 app에 알림
            }catch(err){                                            //에러가 나면, 연결이 안되는 것이다
                console.error(err)                                   
                socket.write("RB POWER OFF")                        //클라이언트가 Off되어있음으로 판단하고 넘긴다
            }
        }
    });

    socket.on('end', function(data){                                // 소켓 연결 종료 이벤트가 왔을 때 콜백함수 실행
       console.log("app disconnected.");                            // 콘솔창에 연결이 종료되었다고 출력
    });

    socket.on('error', function(err){                               // 소켓 연결에 에러 이벤트가 왔을 때 콜백함수 실행
       console.log(err);                                            // 에러를 콘솔창에 출력
    });
})

app_server.listen(8402, function(){
    console.log('listening on 8402...');
})