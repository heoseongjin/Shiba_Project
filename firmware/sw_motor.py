import RPi.GPIO as GPIO
from time import sleep

from A4988 import a4988
from Servo import servo

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

SW0=31
#SW0의 신호는 31번 핀 (시계 방향으로 돌기 시작)
SW1=33
#SW1의 신호는 33번 핀 (누르면 멈춤)
SW2=35
#SW2의 신호는 35번 핀 (반시계 방향으로 90도)
SW3=37
#SW3의 신호는 37번 핀 (반시계 방향으로 30도 더)

GPIO.setup(SW0, GPIO.IN)
GPIO.setup(SW1, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(SW2, GPIO.IN)
GPIO.setup(SW3, GPIO.IN)

s = servo(40)
#서보모터 클래스 선언
a = a4988("shooter", 21, 10, 22, 5/10000)
#투석기 클래스 선언
b = a4988("body", )

def home_position():
    while 1:
        a.step_motor_cons(1,1)
        if GPIO.input(SW1) == False:
            print("sw1 on")
            a.step_motor_cons(1,0)
            break

def feedShooter(mode):
    if mode == 180:
        s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(200, 0)
        sleep(1)
        s.servo_motor(50,5)
        sleep(0.2)
        home_position()
    elif mode == 135:
        s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(150, 0)
        sleep(1)
        s.servo_motor(50,5)
        sleep(0.2)
        home_position();

def sw_input():

    Input0 = GPIO.input(SW0)
    #Input1 = GPIO.input(SW1)
    Input2 = GPIO.input(SW2)
    Input3 = GPIO.input(SW3)

    if Input0 == False:
        print("sw0 on")
        a.step_motor(100,0)
        sleep(0.2)
    elif Input2 == False:
        print("sw2 on")
        s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(200, 0)
        sleep(1)
        s.servo_motor(50,5)
        sleep(0.2)
        home_position()
    elif Input3 == False:
        print("sw3 on")
        s.servo_motor(50,10)
        sleep(0.2)
        a.step_motor(150, 0)
        sleep(1)
        s.servo_motor(50,5)
        sleep(0.2)
        home_position()

cnt = 0

try:
#예외처리
    while 1:
        """ cnt += 1
        print(str(cnt)+"번째 180도 테스트")
        feedShooter(180)
        sleep(1)
        print(str(cnt)+"번째 135도 테스트")
        feedShooter(135)
        if cnt == 51:
            break; """
        sw_input()


except KeyboardInterrupt:
#키보드 인터럽트 발생시
    pass
    #예외 처리 안함.

GPIO.cleanup()
#클리어 사용 중인 핀을 포함해서 모든 리소스 시스템에 반환