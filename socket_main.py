#-*- coding:utf-8 -*-
from socket import *
from A4988 import *

HOST = '192.168.0.53'     # 서버주소
PORT = 8403              # 서버포트

c = socket(AF_INET, SOCK_STREAM)    # 소켓 객체 생성
print('connecting....')
c.connect((HOST, PORT))             # 소켓 연결
print('ok')

while 1:
    
    while 1:
        data = str(c.recv(1024), "utf-8")
        if data:
            break;
        
    print('recieve_data :',data)
    if data == 'a':                 # 자동 간식
        c.send(('Auto Snack').encode())
    elif data == '1':                # 수동 간식 
        c.send(('Sudong Snack').encode())
        snack = a4988(11,10,22)
        snack.motor(400,0)
    elif data == '<':               # 모터 왼쪽
        c.send(('Turn left').encode())
        left = a4988(11,10,22)
        left.motor(33,0)
    elif data == '>':               # 모터 오른쪽
        c.send(('Turn right').encode())
        right = a4988(11,10,22)
        right.motor(33,1)
    else:
        c.send(data.encode())

    GPIO.cleanup()
    #클리어 사용 중인 핀을 포함해서 모든 리소스 시스템에 반환

c.close()