import RPi.GPIO as GPIO
from time import sleep

class a4988:
    NAME = "a4988"
    ENABLE = 0
    STEP = 0
    DIR = 0
    
    delay = 500/1000000
    #delay = 500/1000000 #500us

    def __init__(self, enable, step, dir):
        self.ENABLE = enable
        self.STEP = step
        self.DIR = dir

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

    def motor(self, step, dir):
        print("motor is running!!!!")
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


    def home_position(self):
        GPIO.output(self.DIR, GPIO.HIGH)
        #1은 반시계 0은 시계
        GPIO.output(self.ENABLE, GPIO.LOW)
        while 1:
            GPIO.output(self.STEP, GPIO.HIGH)
            sleep(delay)
            GPIO.output(self.STEP, GPIO.LOW)
            sleep(delay)
            if GPIO.input(SW1) == False:
                print("sw1 on")
                GPIO.output(self.ENABLE, GPIO.HIGH)
                break
    '''
    def sw_input(self):
        if Input0 == False:
            print("sw0 on")
            self.home_position()
            sleep(0.3)
            '''


if __name__ == "__main__":
    try:
    #예외처리
        while 1:
            sw_input()


    except KeyboardInterrupt:
    #키보드 인터럽트 발생시
        pass
        #예외 처리 안함.
    