package com.pkteq.JPOSClient.service;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ISOClientService {
    public void sendISOMessage() {
        try {
            // Initialize channel with the same packager as the server
            ASCIIChannel channel = new ASCIIChannel("localhost", 5000, new ISO93APackager());
            channel.setTimeout(30000); // 30-second timeout
            channel.connect();

            // Create ISO message
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("1200");
            isoMsg.set(3, "000000"); // Processing code
            isoMsg.set(4, "1000");   // Transaction amount
            isoMsg.set(7, "0918093031"); // Date & Time
            isoMsg.set(11, "123456"); // STAN
            isoMsg.set(37, "000000123456"); // Retrieval Reference Number
            isoMsg.set(41, "ATM-1234"); // Terminal ID
            isoMsg.set(49, "840"); // Currency code (USD)

            // Send the message
            channel.send(isoMsg);
            System.out.println("ISO message sent: " + isoMsg);

            // Receive the response
            ISOMsg response = channel.receive();
            if (response != null) {
                System.out.println("Response received: " + response);
                System.out.println("Response MTI: " + response.getMTI());
                System.out.println("Response Code: " + response.getString(39));
            } else {
                System.out.println("No response received from server.");
            }

            // Disconnect
            channel.disconnect();
        } catch (ISOException | IOException e) {
            System.out.println("Error sending ISO message: " + e.getMessage());
        }
    }
}
