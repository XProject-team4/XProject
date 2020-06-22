int led = 1;

int data = 0;

void setup() {
	Serial.begin(9600);
	pinMode(led, OUTPUT);
}

void blk(){
	digitalWrite(led,HIGH);
	delay(1000);
	digitalWrite(led,LOW);
	delay(1000);
}

void loop() {
	data = Serial.read();
	printf(data);
	Serial.print(data);
	delay(1000);
}
