import serial

arduino = serial.Serial(
	port='/dev/ttyACM0',
	baudrate=9600,
)

print('Connected Serial Port is ' + arduino.portstr)

while True:
	arduino.write([0])
	arduino.write([1])
	data = arduino.read()
	print(data)
