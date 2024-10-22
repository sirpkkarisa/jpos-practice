package com.pkteq.JPOSServer.task.participants;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionManager;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;

import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Responsible for Validating the request fields depending on the type
 * of transaction
 */
public class CheckFieldsForBalanceRequestParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context) serializable;
        ISOMsg msg = context.get("REQUEST_KEY");

        // VALIDATE ACCOUNT NUMBER
        if (msg.hasField(2)) {
            // ^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$"
            String acc = msg.getString(2);
            if (acc == null || acc.isBlank() || !acc.matches("^[0-9]{8,19}$")) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        if (msg.hasField(4)) {
            // VALIDATE
            String amtStr = msg.getString(4);
            long amt = Long.parseLong(amtStr);
            if (amt < 0 || amtStr.length() != 12) {
                // REJECT NEGATIVE AMOUNTS
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate Transmission date & time
        if (msg.hasField(7)) {
            String tranDateTime = msg.getString(7);
            if (tranDateTime == null || tranDateTime.isBlank() || tranDateTime.length() != 10 || !validateYYMMDDHHMM(tranDateTime)) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate STAN
        if (msg.hasField(11)) {
            String stan = msg.getString(11);
            if (stan == null || stan.isBlank() || stan.length() != 6) {
                return ABORTED;
            }
        }else {
            return ABORTED;
        }
        
        // Validate Transaction time
        if (msg.hasField(12)) {
            String tranTime = msg.getString(12);
            if (tranTime == null || tranTime.isBlank() || validateHHMMSS(tranTime)) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate Transaction date
        if (msg.hasField(13)) {
            String tranDate = msg.getString(13);
            if (tranDate == null || tranDate.isBlank() || validateYYMMDD(tranDate)) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate method used
        if (msg.hasField(22)) {
            String method = msg.getString(22);
            // 040 = By online capture
            // 000 = Unknown
            // 010 = By hand
            if (method == null || method.isBlank() || !method.equals("040")) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate Acquiring institution identification code
        if (msg.hasField(32)) {
            String acqInst = msg.getString(32);
            if (acqInst == null || acqInst.isBlank() || acqInst.length() != 11) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate RRN
        if (msg.hasField(37)) {
            String acqInst = msg.getString(37);
            if (acqInst == null || acqInst.isBlank() || acqInst.length() != 12) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate Terminal ID
        if (msg.hasField(41)) {
            String acqInst = msg.getString(41);
            if (acqInst == null || acqInst.isBlank() || acqInst.length() != 8) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        // Validate Merchant ID
        if (msg.hasField(42)) {
            String acqInst = msg.getString(42);
            if (acqInst == null || acqInst.isBlank() || acqInst.length() != 8) {
                return ABORTED;
            }
        } else {
            return ABORTED;
        }

        return PREPARED;
    }

    public static boolean validateYYMMDDHHMM(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validateYYMMDD(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean validateHHMMSS(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        TransactionParticipant.super.commit(id, context);
    }

    @Override
    public void abort(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("CheckFieldsForBalanceRequestParticipant: abort");
        sendMessage((Context) serializable);
    }

    private void sendMessage(Context context){
        ISOSource source = context.get("RESOURCE_KEY");
        ISOMsg msgResp = context.get("RESPONSE_KEY");
        try {
            source.send(msgResp);
        } catch (IOException | ISOException e) {
            System.out.println(e.getMessage());
        }
    }
}
