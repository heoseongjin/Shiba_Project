var net = require('net');
var rb = require('./server.js');
var app = require('./app_server.js');
var mysql = require("mysql");


var dog = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "1234",
    port: "3306",
    database: "mydog"
})

module.exports.select = function(name, callback){
    console.log('SELECT * FROM ' + name);
    var selectsql = "SELECT * FROM " + name;
    dog.query(selectsql, function(err, result){             //쿼리문 실행
        if(!err){                                           // 쿼리문이 에러가 없다면
            var db = JSON.stringify(result);                // 결과값을 JSON 형태의 문자열로 변환
            callback(db);                               // 연결된 소켓으로 데이터 전송
            } else{                                               // 에러가 있다면
            console.log( selectsql + ' : Query has error!');  // 에러가 있다고 콘솔에 출력
            }
    });
}

module.exports.insert_feed = function(fq,sq){
    var in_feed = "INSERT INTO feed (feed, feed_quan, snack, snack_quan) values (default," + fq + ", null,"+ sq + ")";  //먹이 모터 작동시간과 잔여량 추가
    dog.query(in_feed, function(err){               //쿼리문 실행
        if(!err){                                   
            console.log("insert success!");
        } else{
            console.log(err);
        }

    })
}

module.exports.insert_snack = function(fq,sq){
    var in_snack = "INSERT INTO feed (feed, feed_quan, snack, snack_quan) values (null," + fq + ", default,"+ sq + ")"; //간식 모터 작동시간과 잔여량 추가
    dog.query(in_snack, function(err){
        if(!err){
            console.log("insert success!");
        } else{
            console.log(err);
        }
    })
}