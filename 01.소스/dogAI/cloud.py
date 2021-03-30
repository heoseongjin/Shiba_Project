# -*- coding:utf-8 -*-
from socket import *
from webYolo import PytorchYolo

HOST = '192.168.13.17'  # 서버주소
PORT = 8401  # 서버포트

c = socket(AF_INET, SOCK_STREAM)  # 소켓 객체 생성
print('connecting....')
c.connect((HOST, PORT))  # 소켓 연결
print('ok')

detect = PytorchYolo("http://192.168.13.31:8409/?action=snapshot", "dog")

while 1:
    while 1:
        data = str(c.recv(1024), "utf-8")
        if data:
            break

    print('recieved_data :', data)

    if data == 'd':  # 개 인식 요청
        try:
            c.send((detect.yolov3()).encode())
        except:
            print("에러")
            c.send(('n').encode())
    else:
        c.send((data+"is not defined").encode())

    data = ''

c.close()