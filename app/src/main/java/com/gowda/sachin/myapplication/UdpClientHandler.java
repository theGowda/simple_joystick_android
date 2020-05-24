package com.gowda.sachin.myapplication;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UdpClientHandler extends Thread {
    private int port;
    private String ipadrrString;
    private String message = "0.00,0.00";
    private DatagramSocket socket;
    private boolean threadKill = false;
    //InetAddress addr;
    public UdpClientHandler(String ipadrrString, int port){
        super();
        this.port = port;
        this.ipadrrString = ipadrrString;
    }
    public void setMessage(String msg){
        this.message = msg;
    }
    public void threadExit(){
        this.threadKill = true;
    }
    @Override
    public void run(){
        while(!this.threadKill){
            Log.d("Tag","Thread Started");
            try{
                this.socket = new DatagramSocket();
                InetAddress addr = InetAddress.getByName(this.ipadrrString);
                byte[] buff =  this.message.getBytes();
                DatagramPacket packet = new DatagramPacket(buff, this.message.length(), addr, this.port);
                socket.send(packet);
            } catch (SocketTimeoutException e) {
                Log.d("Tag", "Socket TimeOut");
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.d("Tag", "UnkwonHost");
            } catch (IOException e) {
                Log.d("Tag", "IOException");
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            finally {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
        }
    }
}
