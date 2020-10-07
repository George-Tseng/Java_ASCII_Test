package javaTest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Get_Mac_Address {

    public static String getMacAddress() {

        InetAddress myIP;
        String resultMacString = "";

        try {
            /*取得本地的裝置Domain Name+IP位址*/
            myIP = InetAddress.getLocalHost();
            /*取得本地的IP位址*/
            //System.out.println("Local Host IP為：" + myIP.getHostAddress());

            /*用getByInetAddress()找出跟本地IP有關的資訊*/
            NetworkInterface myNetIF = NetworkInterface.getByInetAddress(myIP);
            /*取出實體位址(Mac Address)，並存入陣列中*/
            byte[] myMacAddress = myNetIF.getHardwareAddress();
            /*生成StringBuilder物件*/
            StringBuilder sb0 = new StringBuilder();
            /*用迴圈讓StringBuilder裝填完Mac Address*/
            for (int i = 0; i < myMacAddress.length; i++) {
                sb0.append(String.format(("%02X%s"), myMacAddress[i], (i < myMacAddress.length - 1) ? "-" : ""));
            }
            /*用toString()方法讓StringBuilder轉回String*/
            resultMacString =  sb0.toString();
        } catch (UnknownHostException e) {

        } catch (SocketException e) {

        } catch(Exception e) {

        } finally{
            return resultMacString;
        }
    }
}
