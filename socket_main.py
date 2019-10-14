#-*- coding:utf-8 -*-
import RPi.GPIO as GPIO
import random
from socket import *
from StepMotor import *
from Servo import *

HOST = '192.168.0.53'       # 서버주소
PORT = 8403                 # 서버포트

c = socket(AF_INET, SOCK_STREAM)    # 소켓 객체 생성
print('connecting....')
c.connect((HOST, PORT))             # 소켓 연결
print('ok')

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

SW=32
#SW의 신호는 번 핀 (누르면 멈춤)
GPIO.setup(SW, GPIO.IN, pull_up_down=GPIO.PUD_UP)  #마이크로 스위치, 풀업

shooter = drv8825("shooter", 5, 7, 5/10000)
body = drv8825("body", 19, 21, 5/10000)
feed = drv8825("Feed Filter", 35, 37, 20/10000)
servo = servo(8)

def home_position():
    while 1:
        shooter.step_motor_cons(1, 1, 3/1000)
        if GPIO.input(SW) == False:
            print("Limit")
            break

def feedShooter(mode):
    if mode == 180:
        feed.step_motor(100, 0)
        sleep(0.5)
        servo.servo_motor(50,15)
        sleep(0.5)
        shooter.step_motor(200, 0)
        sleep(0.5)
        servo.servo_motor(50,10)
        sleep(0.5)
        home_position()
    elif mode == 135:
        feed.step_motor(100, 0)
        sleep(0.5)
        servo.servo_motor(50,15)
        sleep(0.5)
        shooter.step_motor(150, 0)
        sleep(0.5)
        servo.servo_motor(50,10)
        sleep(0.5)
        home_position();

def auto():
    print("auto mode on")
    cnt = 0
    while 1:
        body.step_motor(50, 0)
        c.send(('d').encode())
        while 1:
            data = str(c.recv(1024), "utf-8")
            if data:
                break
        if data == 'y':
            cnt += 1
            print("dog O -- "+cnt)
            mode = random.choice([180, 135])
            feedShooter(mode)
        elif data == 'n':
            print("dog X")

        if cnt == 3:
            print("auto mode off")
            break;

def socket_action():
    while 1:
        data = str(c.recv(1024), "utf-8")
        if data:
            break
        
    print('recieve_data :',data)
    if data == 'a':                     # 자동 간식
        auto()
    elif data == '1':                   # 수동 간식 
        c.send(('Sudong Snack').encode())
        mode = random.choice([180, 135])
        feedShooter(mode)
    elif data == '<':                   # 모터 왼쪽
        feed.step_motor(100,0)
        c.send(('Turn left').encode())
        body.step_motor(33,0)
    elif data == '>':                   # 모터 오른쪽
        c.send(('Turn right').encode())
        body.step_motor(33,1)
    else:
        c.send(data.encode())

while 1:
    socket_action()
c.close()
