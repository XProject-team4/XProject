# client(app)에서 beacon과 연결되면 arduino에 1(servo motor 작동) 보냄.(serial로)
import serial

def run_servo(run):
    ser= serial.Serial(
    # port='/dev/tty', # ubuntu serial port 확인 후 수정
    baudrate = 9600,
    )
    op = run
    ser.write(op.encode())
