#-*- coding:utf-8 -*-
from socket import *

HOST='192.168.0.5'     # 서버주소
PORT=8403              # 서버포트

c = socket(AF_INET, SOCK_STREAM)    # 소켓 객체 생성
print ('connecting....')
c.connect((HOST, PORT))             # 소켓 연결
print ('ok')


while 1:
    data = input()             # 데이터 입력
    if data:
        c.send(data.encode())   # 데이터 전송
    else:
        break

    u = str(c.recv(1024), "utf-8")  # 데이터 수신대기
    print (u)
c.close()
