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

/**
 * Responsible for Validating the request fields depending on the type
 * of transaction
 */
public class CheckFieldsForBalanceRequestParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context) serializable;
        ISOMsg msg = context.get("REQUEST_KEY");
        if (msg.hasField(2)) {
            // VALIDATE ACCOUNT NUMBER
            // ^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$"
            String acc = msg.getString(2);
            if (acc != null && !acc.isBlank() && acc.matches("^[0-9]{8,19}$")) {
                return PREPARED;
            }
        }
//       CONTINUE CHECKS
        return ABORTED;
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
