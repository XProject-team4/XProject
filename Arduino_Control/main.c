#include <Servo.h>

Servo servo;
int value = 0;

void setup(){
	servo.attach(7); //pin 7 used
	Serial.begin(9600); //Serial montor for testing
}

void loop(){
	if(Serial.available()){
		char in_data;
		in_data = Serial.read();
		if(in_data=='1'){
			value += 90;
			if(value>=90) value=0;
		}
		else vlaue = 0;
		servo.write(value);
	}
}
