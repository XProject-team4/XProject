int led1 = 1;
int led2 = 2;

boolean info = false;

void setup(){
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
}

void loop(){
  info = Serial.read();
  if(info==true){
    for(int i=0;i<5;i++){on();}
  }
}

void on(){
  digitalWrite(led1,HIGH);
  digitalWrite(led2,HIGH);
}

void off(){
  digitalWrite(led1,LOW);
  digitalWrite(led2,LOW);
}
