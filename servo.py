import RPi.GPIO as GPIO
import time

class servo:
    SIG = 0

    def __init__(self, sig):
        self.SIG = sig
        
        GPIO.setmode(GPIO.BOARD)
        GPIO.setwarnings(False)

        GPIO.setup(self.SIG, GPIO.OUT)

    def __del__(self):
        print("")

    def servo_motor(self, frequency, cycle):
        print("servo_motor : " + cycle)
        p = GPIO.PWM(self.SIG, frequency)
        p.start(5)

        p.ChangeDutyCycle(cycle)
        time.sleep(1)

if __name__ == "__main__":
    try:
    #예외처리
        while 1:
            print("hello")


    except KeyboardInterrupt:
    #키보드 인터럽트 발생시
        pass
        #예외 처리 안함.
