#-*- coding:utf-8 -*-
import RPi.GPIO as GPIO
import random
from socket import *
from A4988 import *
from Servo import *

HOST = '192.168.0.53'     # 서버주소
PORT = 8403              # 서버포트

c = socket(AF_INET, SOCK_STREAM)    # 소켓 객체 생성
print('connecting....')
c.connect((HOST, PORT))             # 소켓 연결
print('ok')

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
SW1=33
#SW1의 신호는 33번 핀 (누르면 멈춤)
GPIO.setup(SW1, GPIO.IN, pull_up_down=GPIO.PUD_UP)  #마이크로 스위치, 풀업

a = a4988("shooter", 21, 10, 22, 5/10000)

def home_position():
    while 1:
        a.step_motor_cons(1,1)
        if GPIO.input(SW1) == False:
            print("Limit")
            a.step_motor_cons(1,0)
            break

def feedShooter(mode):
    if mode == 180:
        #s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(200, 0)
        sleep(1)
        #s.servo_motor(50,5)
        sleep(0.2)
        home_position()
    elif mode == 135:
        #s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(150, 0)
        sleep(1)
        #s.servo_motor(50,5)
        sleep(0.2)
        home_position();

while 1:
    
    while 1:
        data = str(c.recv(1024), "utf-8")
        if data:
            break
        
    print('recieve_data :',data)
    if data == 'a':                 # 자동 간식
        c.send(('d').encode())
        while 1:
            data = str(c.recv(1024), "utf-8")
            if data:
                break
        if data == 'y':
            print("dog O")
        elif data == 'n':
            print("dog X")

    elif data == '1':                # 수동 간식 
        c.send(('Sudong Snack').encode())
        mode = random.choice([180, 135])
        feedShooter(mode)
    elif data == '<':               # 모터 왼쪽
        c.send(('Turn left').encode())
        a.step_motor(33,0)
    elif data == '>':               # 모터 오른쪽
        c.send(('Turn right').encode())
        a.step_motor(33,1)
    else:
        c.send(data.encode())
c.close()