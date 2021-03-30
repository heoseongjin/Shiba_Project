#-*- coding:utf-8 -*-
import RPi.GPIO as GPIO
import random
from StepMotor import *
from Servo import *

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

SW=32
#SW의 신호는 번 핀 (누르면 멈춤)
GPIO.setup(SW, GPIO.IN, pull_up_down=GPIO.PUD_UP)  #마이크로 스위치, 풀업

shooter = drv8825("shooter", 35, 37, 20/10000)
body = drv8825("body", 5, 7, 15/10000)
feed = drv8825("Feed Filter", 19, 21, 40/10000)
servo = servo(8)

def home_position():
    while 1:
        shooter.step_motor_cons(1, 1, 20/10000)
        if GPIO.input(SW) == False:
            print("Limit")
            break

def feedShooter(mode):
    if mode == 180:
        feed.step_motor(100, 0)
        sleep(0.5)
        servo.servo_motor(50,10)
        sleep(0.5)
        shooter.step_motor(200, 0)
        sleep(0.5)
        servo.servo_motor(50,5)
        sleep(0.5)
        home_position()
    elif mode == 135:
        feed.step_motor(100, 0)
        sleep(0.5)
        servo.servo_motor(50,10)
        sleep(0.5)
        shooter.step_motor(150, 0)
        sleep(0.5)
        servo.servo_motor(50,5)
        sleep(0.5)
        home_position()


def action():
    data = input()
        
    if data == '1':                   # 수동 간식 
        mode = random.choice([180, 135])
        feedShooter(mode)
    elif data == '<':                   # 모터 왼쪽
        body.step_motor(200,0)
    elif data == '>':                   # 모터 오른쪽
        body.step_motor(200,1)
    else:
        print("incorrect command")  
    
while 1:
    action()
c.close()


