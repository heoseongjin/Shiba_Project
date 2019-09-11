import RPi.GPIO as GPIO
from time import sleep

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

SW0=29
#SW0의 신호는 31번 핀 (시계 방향으로 돌기 시작)
SW1=33
#SW1의 신호는 33번 핀 (누르면 멈춤)
SW2=35
#SW2의 신호는 35번 핀 (반시계 방향으로 90도)
SW3=37
#SW3의 신호는 37번 핀 (반시계 방향으로 30도 더)

ENABLE=21
#ENABLE의 신호는 21번 핀
STEP=10
#STEP의 신호는 16번 핀
DIR=22
#DIR의 신호는 18번 핀

#delay = 500/1000000 #500us
delay = 250/1000000

GPIO.setup(SW0, GPIO.IN)
GPIO.setup(SW1, GPIO.IN)
GPIO.setup(SW2, GPIO.IN)
GPIO.setup(SW3, GPIO.IN)

GPIO.setup(ENABLE, GPIO.OUT, initial=GPIO.HIGH)
#ENABLE핀을 출력으로 설정하고 초기값은 LOW
GPIO.setup(STEP, GPIO.OUT,initial=GPIO.LOW)
#STEP핀을 출력으로 설정하고 초기값은 LOW
GPIO.setup(DIR, GPIO.OUT,initial=GPIO.HIGH)
#DIR핀을 출력으로 설정하고 초기값은 LOW


def motor(step, dir):
    GPIO.output(ENABLE, GPIO.LOW)
    #motor on
    GPIO.output(DIR, dir)
    #1은 반시계 0은 시계
    for cnt in range(0,step):
        GPIO.output(STEP, GPIO.HIGH)
        sleep(delay)
        GPIO.output(STEP, GPIO.LOW)
        sleep(delay)

    GPIO.output(ENABLE, GPIO.HIGH)


def home_position():
    GPIO.output(DIR, GPIO.HIGH)
    #1은 반시계 0은 시계
    GPIO.output(ENABLE, GPIO.LOW)
    while 1:
        GPIO.output(STEP, GPIO.HIGH)
        sleep(delay)
        GPIO.output(STEP, GPIO.LOW)
        sleep(delay)
        if GPIO.input(SW1) == False:
            print("sw1 on")
            GPIO.output(ENABLE, GPIO.HIGH)
            break


def sw_input():
    Input0 = GPIO.input(SW0)
    Input2 = GPIO.input(SW2)
    Input3 = GPIO.input(SW3)

    if Input0 == False:
        print("sw0 on")
        home_position()
        sleep(0.3)
    elif Input2 == False:
        print("sw2 on")
        motor(200, 0)
        sleep(0.3)
    elif Input3 == False:
        print("sw3 on")
        motor(150, 0)
        sleep(0.3)

try:
#예외처리
    while 1:
        sw_input()



except KeyboardInterrupt:
#키보드 인터럽트 발생시
    pass
    #예외 처리 안함.

GPIO.cleanup()
#클리어 사용 중인 핀을 포함해서 모든 리소스 시스템에 반환