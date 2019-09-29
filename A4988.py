import RPi.GPIO as GPIO
from time import sleep

class a4988:
    NAME = 0
    ENABLE = 0
    STEP = 0
    DIR = 0
    
    delay = 0
    #delay = 5/10000 #500us

    def __init__(self, name, enable, step, dir, delay):
        self.NAME = name
        self.ENABLE = enable
        self.STEP = step
        self.DIR = dir
        self.delay = delay

        print(self.NAME+" is ready")

        GPIO.setmode(GPIO.BOARD)
        GPIO.setwarnings(False)

        GPIO.setup(self.ENABLE, GPIO.OUT, initial=GPIO.HIGH)
        #ENABLE핀을 출력으로 설정하고 초기값은 LOW
        GPIO.setup(self.STEP, GPIO.OUT,initial=GPIO.LOW)
        #STEP핀을 출력으로 설정하고 초기값은 LOW
        GPIO.setup(self.DIR, GPIO.OUT,initial=GPIO.HIGH)
        #DIR핀을 출력으로 설정하고 초기값은 LOW

    def __del__(self):
        print("")

    def step_motor(self, step, dir):
        print(self.NAME+" run")
        GPIO.output(self.ENABLE, GPIO.LOW)
        #motor on
        GPIO.output(self.DIR, dir)
        #1은 반시계 0은 시계
        for cnt in range(0,step):
            GPIO.output(self.STEP, GPIO.HIGH)
            sleep(self.delay)
            GPIO.output(self.STEP, GPIO.LOW)
            sleep(self.delay)

        GPIO.output(self.ENABLE, GPIO.HIGH)
    
    def step_motor_cons(self, dir, mode):
        if(mode == 1):
            GPIO.output(self.ENABLE, GPIO.LOW)
            #motor on
            GPIO.output(self.DIR, dir)
            #1은 반시계 0은 시계
            GPIO.output(self.STEP, GPIO.HIGH)
            sleep(self.delay)
            GPIO.output(self.STEP, GPIO.LOW)
            sleep(self.delay)

            sleep(0.1)
        elif(mode == 0):
            GPIO.output(self.ENABLE, GPIO.HIGH)
            #motor off


if __name__ == "__main__":
    try:
    #예외처리
        while 1:
            print("hello")

    except KeyboardInterrupt:
            #키보드 인터럽트 발생시
        pass
    #예외 처리 안함.
    