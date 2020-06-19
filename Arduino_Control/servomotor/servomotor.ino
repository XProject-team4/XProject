#include <Servo.h>

Servo myservo;
int degree = 0;

void setup() {
	myservo.attach(7);
}

void loop() {
	while(true){
		degree += 90;
		myservo.write(degree);
		delay(3000);
		if(degree >= 360){
			degree = 0;
		}
	}
}
