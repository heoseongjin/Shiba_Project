var net = require('net');
var rb = require('./server.js');
var app = require('./app_server.js');
var mysql = require("mysql");
var moment = require('moment');
var home = require('./smarthome_socket.js');


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
            callback("no data!");
            }
    });
}

module.exports.insert_time = function(){
    var in_feed = "INSERT INTO time (date) values (default)";  //먹이 모터 작동시간과 잔여량 추가
    dog.query(in_feed, function(err){               //쿼리문 실행
        if(err)
            console.log(err);
    })
}

module.exports.insert_purchase = function(data){
    var json = JSON.parse(data);
    console.log(json);
    for(var i=0;i<Object.keys(json).length;i++){
        item = json[i];
        item = JSON.parse(item);
        
        var in_purchase = "INSERT INTO purchase(date, name, count, price) values (default, item.name, item.count, item.price)";
        dog.query(in_purchase, function(err){
            if(err)
                console.log(err);
        });
    }
}

module.exports.insert_count = function(){
    var day = moment().format("YYYY-MM-DD");
    var select_date = "SELECT count from feed_count where date = ?";
    var params = [day];
    dog.query(select_date, params, function(err, result){
        if (result.length == 0){
            var insert_date = "insert into feed_count values (?, ?)";
            var params2 = [day, 1];
            dog.query(insert_date, params2, function(err){
                if(err)
                    console.log(err);
            });
        }else{
            c = result[0].count + 1;
            var update_count = "UPDATE feed_count SET count = ? WHERE date = ?";
            var params3 = [c, day];
            dog.query(update_count, params3, function(err){
                if (!err){
                    try{
                        home.send_to_home(c.toString());
                    }catch(err){
                        console.log(rb.time() + err);
                    }
                }
            });
        }
    });
}
