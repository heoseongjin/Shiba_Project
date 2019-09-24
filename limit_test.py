import RPi.GPIO as GPIO
from time import sleep

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

SW=33
#SW1의 신호는 33번 핀 (누르면 멈춤)

def detect():
    print("event_detect edge on the SW")

GPIO.setup(SW, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.add_event_detect(SW, GPIO.FALLING, callback=detect, bouncetime=300)

""" def sw_input():

    Input = GPIO.wait_for_edge(SW, GPIO.FALLING, timeout=5000)
    if Input is None:
        print('Timeout occurred')
    else:
        print('Edge detected on the SW') """

while 1:
    try:
    #예외처리
        #sw_input() 
        sleep(5)   
    except KeyboardInterrupt:
    #키보드 인터럽트 발생시
        GPIO.cleanup()
        #클리어 사용 중인 핀을 포함해서 모든 리소스 시스템에 반환
        break
        #탈출

