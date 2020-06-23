int led = 2;

void setup() {
  pinMode(led, OUTPUT);
}

void off(){
  digitalWrite(led,LOW);
  delay(1000);
}

void loop() {
  off();
}
