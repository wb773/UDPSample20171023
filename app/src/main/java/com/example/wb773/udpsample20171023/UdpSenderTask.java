package com.example.wb773.udpsample20171023;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by wb773 on 2017/10/23.
 */

public class UdpSenderTask extends AsyncTask<String, String, String> {

    private CallBackTask callbacktask;

    @Override
    protected String doInBackground(String... strings) {

        //--------------------------------
        // 送信
        //--------------------------------
        Log.d("UdpSenderTask","start sending.");
        String result = sendUdp();
        Log.d("UdpSenderTask","end sending result: " + result +" .");

        if(result.equals("@NG")){
            return result;
        }

        //--------------------------------
        // 返信待ち
        //--------------------------------
        // OKの場合受信待ちを行う
        Log.d("UdpSenderTask","start receiving.");
        String reveiveResult = "";
        try {

            reveiveResult = receiveUdp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("UdpSenderTask","end receiving result: " + reveiveResult +" .");
        return reveiveResult;
    }


    private String sendUdp() {
        // UDPパケットを送信する先となるブロードキャストアドレス (5100番ポート)
        InetSocketAddress remoteAddress = new InetSocketAddress("192.168.10.6", 10000);

        // UDPパケットに含めるデータ
        byte[] sendBuffer = "@START".getBytes();

        // UDPパケット
        DatagramPacket sendPacket =
                new DatagramPacket(sendBuffer, sendBuffer.length, remoteAddress);

        String result;

        // DatagramSocketインスタンスを生成して、UDPパケットを送信
        DatagramSocket socket = null;
        try {
            socket =  new DatagramSocket();
            socket.send(sendPacket);

            result = "@OK";
        } catch (IOException e) {
            result = "@NG";
            e.printStackTrace();
        }finally {
            if(socket != null){
                socket.close();
            }
        }

        return result;
    }

    private String receiveUdp() throws SocketException,IOException {
        // ポートを監視するUDPソケットを生成
        DatagramSocket receiveSocket = new DatagramSocket(10000);

        // 受け付けるデータバッファとUDPパケットを作成
        byte receiveBuffer[] = new byte[32];
        DatagramPacket receivePacket =
                new DatagramPacket(receiveBuffer, receiveBuffer.length);

        String receiveMessage;

        while (true) {
            // UDPパケットを受信
            receiveSocket.setSoTimeout(3000);
            try{
                receiveSocket.receive(receivePacket);
            }catch(SocketTimeoutException e){
                receiveSocket.close();
                return "Udp connection was timeouted.";
            }

            // 受信したデータを標準出力へ出力
            System.out.println
                    (new String(receivePacket.getData(),
                            0, receivePacket.getLength()));

            receiveMessage = new String(receivePacket.getData(),
                    0, receivePacket.getLength());

            if(!receiveMessage.equals("")){
                receiveSocket.close();
                return receiveMessage;
            }
        }

    }







    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }

    //----------------------------------------------------
    // Callback
    //----------------------------------------------------
    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    /**
     * コールバック用のstaticなclass
     */
    public static class CallBackTask {
        public void CallBack(String result) {
        }
    }



}
