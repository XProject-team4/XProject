int led = 2;

void setup() {
  pinMode(led, OUTPUT);
}

void blk(){
  digitalWrite(led,HIGH);
  delay(1000);
  digitalWrite(led,LOW);
  delay(1000);
}

void loop() {
  blk();
}
