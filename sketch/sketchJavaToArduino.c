#include <LiquidCrystal.h>

LiquidCrystal lcd(8,13, 9, 4, 5, 6, 7);

void setup()
{
  Serial.begin(9600);
 lcd.begin(16,2);
 pinMode(10, OUTPUT);
 lcd.print("Esperando datos");


}

void loop() {
 // when characters arrive over the serial port...
     while (Serial.available() > 0) {
     // lcd.clear();
      Serial.println("leyendo orden");
      String token=Serial.readStringUntil('\n');
     if( token=="cambialn"){
     lcd.setCursor(0,1);     
     }
    else if(token=="limpialcd"){
     lcd.clear();  
     }
     else if(token=="Offlcd"){
     digitalWrite(10, LOW);
     }
     else if(token=="Onlcd"){
     digitalWrite(10, HIGH);
     }
       
    else{  // display each character to the LCD
      lcd.print(token);
    }
    }
}