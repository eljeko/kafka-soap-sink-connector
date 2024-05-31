package org.connect.soap.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpSoapClient {

    public String sendSoapRequest(String SOAPUrl, String soapRequest, String SOAPAction) throws Exception {

        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("YOUR PROXY", PORT NUMBER));


        // Create the connection where we're going to send the file.
        URL url = new URL(SOAPUrl);
        //URLConnection connection = url.openConnection(proxy);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;


        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length", String.valueOf(soapRequest.getBytes().length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        //httpConn.setDoInput(true);

        // send the XML that was read in to b.
        OutputStream out = httpConn.getOutputStream();
        out.write(soapRequest.getBytes());
        out.close();

        // Read the response.
        httpConn.connect();
        System.out.println("http connection status code "+ httpConn.getResponseCode()  +" - "  + httpConn.getResponseMessage());
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        StringBuffer response = new StringBuffer();
        while (in.ready()) {
            // Read and print characters one by one
            // by converting into character
            //char read = (char) in.read();
            response.append((char) in.read());
        }
        return response.toString();
    }

    public static void copy(InputStream in, OutputStream out)
            throws IOException {

        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1)
                        break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
