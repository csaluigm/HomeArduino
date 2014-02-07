package com.boss.serial;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
public class SerialClass implements SerialPortEventListener {
 
 public SerialPort serialPort;
 /** The port we're normally going to use. */
 private static final String PORT_NAMES[] = {
 "/dev/tty.usbserial-A9007UX1", // Mac OS X
 "/dev/ttyUSB0", // Linux
 "COM3", // Windows
 };
 
public static BufferedReader input;
public static OutputStream output;
 /** Milliseconds to block while waiting for port open */
 public static final int TIME_OUT = 2000;
 /** Default bits per second for COM port. */
 public static final int DATA_RATE = 9600;
 
public void initialize() {
 CommPortIdentifier portId = null;
 Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
 
//First, Find an instance of serial port as set in PORT_NAMES.
 while (portEnum.hasMoreElements()) {
 CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
 for (String portName : PORT_NAMES) {
 if (currPortId.getName().equals(portName)) {
 portId = currPortId;
 break;
 }
 }
 }
 if (portId == null) {
 System.out.println("Could not find COM port.");
 return;
 }
 
try {
 // open serial port, and use class name for the appName.
 serialPort = (SerialPort) portId.open(this.getClass().getName(),
 TIME_OUT);
 
// set port parameters
 serialPort.setSerialPortParams(DATA_RATE,
 SerialPort.DATABITS_8,
 SerialPort.STOPBITS_1,
 SerialPort.PARITY_NONE);
 
// open the streams
 input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
 output = serialPort.getOutputStream();
 char ch = 1;
 output.write(ch);
 
 
 // add event listeners
 serialPort.addEventListener(this);
 serialPort.notifyOnDataAvailable(true);
 } catch (Exception e) {
 System.err.println(e.toString());
 }
 }
 
public synchronized void close() {
 if (serialPort != null) {
 serialPort.removeEventListener();
 serialPort.close();
 }
 }
 
public synchronized void serialEvent(SerialPortEvent oEvent) {
 if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
 try {
 String inputLine=input.readLine();
 System.out.println(inputLine);
 } catch (Exception e) {
 System.err.println(e.toString());
 }
 }
 
 }
 
 public static synchronized void writeData(String data) {
 //System.out.println("Sent: " + data);
 try {
 output.write(data.getBytes());

 } catch (Exception e) {
 System.out.println("could not write to port");
 }
 }
 
 private Long enBytes(long valor) {
     return new Long(valor / 1024);
 }
 
 public String imprimirInfoRam() throws SigarException {
	 Sigar sigar = new Sigar();
	 Mem memoria = sigar.getMem();    
     System.out.println("Cantidad de memoria RAM: "+ memoria.getRam() + "MB");
     //System.out.println("Total: "+enBytes(memoria.getTotal()));
     //System.out.println("Usada: "+enBytes(memoria.getUsed()));
     //System.out.println("Disponible: "+enBytes(memoria.getFree()));
     System.out.println("Uso: "+(((int)memoria.getUsedPercent())+"%"));
     
     return "RAM:"+memoria.getRam()+"MB"+"-"+((int)memoria.getUsedPercent())+"%";
 }
 
 public String imprimirInfoCpu()throws SigarException {
	 Sigar sigar = new Sigar();
	 return "CPU:"+CpuPerc.format(sigar.getCpuPerc().getCombined());
 }
 
public static void main(String[] args) throws Exception {
 final SerialClass main = new SerialClass();
 main.initialize();

// main.imprimirInfo();
 
 Thread t=new Thread() {
 public void run() {
 //the following line will keep this app alive for 1000 seconds,
 //waiting for events to occur and responding to them (printing incoming messages to console).
 try {Thread.sleep(3000);
// Date date=new Date();
// String d=date.toLocaleString();
//String []dpartes=d.split(" ");
 
 while(true){
Thread.sleep(60000);
writeData("limpialcd\n");
writeData("Onlcd\n");
writeData(main.imprimirInfoCpu()+"\n");
writeData("cambialn\n");
//writeData(dpartes[0]+"\n");
writeData(main.imprimirInfoRam()+"\n");
//writeData(dpartes[1]+"\n");
 //System.exit(0);
 }
} catch (InterruptedException ie) {} catch (SigarException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 }
 };
 t.start();
 System.out.println("Started");
 }
 
}

