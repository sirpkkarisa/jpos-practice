package com.pkteq.JPOSClient.service;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ISOClientService {
    public void sendISOMessage(Integer option) {
        try {
            // Initialize channel with the same packager as the server
            ASCIIChannel channel = new ASCIIChannel("localhost", 7000, new ISO93APackager());
            channel.setTimeout(30000); // 30-second timeout
            channel.connect();

            // Create ISO message
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("1200");
            isoMsg.set(2,"1141231234");

            if (option == 1) {
                isoMsg.set(3, "310000"); // Processing code
            } else if (option == 2) {
                isoMsg.set(3, "200000");
            }
            isoMsg.set(4, "000000001000");   // Transaction amount
            isoMsg.set(7, genDateTime()); // Date & Time
            isoMsg.set(11,genSTAN()); // STAN
            isoMsg.set(12,genTime());
            isoMsg.set(13,genDate());
            isoMsg.set(22,"040");
            isoMsg.set(32,"00000000001");
            isoMsg.set(37, genRRN()); // Retrieval Reference Number
            isoMsg.set(41, "ATM-1234"); // Terminal ID
            isoMsg.set(42, "ONL-1234"); // Merchant ID
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
                if (response.hasField(54)) {
                    System.out.println("Account Balance: " + response.getString(54));
                }
            } else {
                System.out.println("No response received from server.");
            }

            // Disconnect
            channel.disconnect();
        } catch (ISOException | IOException e) {
            System.out.println("Error sending ISO message: " + e.getMessage());
        }
    }

    public String genSTAN() {
        return String.valueOf(Math.round((Math.pow(10,5)-1)* Math.random()));
    }
    public String genRRN() {
        return String.valueOf(Math.round((Math.pow(10,12)-1)* Math.random()));
    }
    public String genDateTime() {
        // Get current instant and add 3 hours
        Instant instant = Instant.now().plus(Duration.ofHours(3));

        // Convert to ZonedDateTime in system default timezone
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        // Define the formatter with the desired pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");

        // Format and return the date-time string
        return formatter.format(zonedDateTime);
    }
    public String genDate() {
        Instant instant = Instant.now().plus(Duration.ofHours(3));
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return formatter.format(zonedDateTime);
    }
    public String genTime() {
        Instant instant = Instant.now().plus(Duration.ofHours(3));
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");

        return formatter.format(zonedDateTime);
    }
}
