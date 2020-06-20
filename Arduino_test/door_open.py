import serial

PORT = '/dev/ttyACM0'

arduino = serial.Serial(port=PORT,baudrate=9600)

while(True):
	push = input()
	arduino.write(push.encode())
