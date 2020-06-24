#include <Servo.h>

Servo servo;

int servoPin = 1;
int led = 2;

int data = 0; // for serial
int angle = 0; // for servo_motor

void setup() {
	servo.attach(servoPin);
	Serial.begin(9600);
	pinMode(led, OUTPUT);
}
/*
void servo_open(){
	for(angle = 0; angle < 90; angle++){
    servo.write(angle);
	}
	for(angle = 90; angle > 0; angle--){
    servo.write(angle);
 }
}
*/
void blk(){
	digitalWrite(led,HIGH);
	delay(1000);
	digitalWrite(led,LOW);
	delay(1000);
}

void loop() {
	data = Serial.read();
	if(data==1){
		blk();
    blk();
	}
	data = 0;
	Serial.print(data);
}
