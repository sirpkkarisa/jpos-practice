package com.pkteq.JPOSServer.task.participants;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.IOException;
import java.io.Serializable;

/**
 * Responsible for checking the interchange version used
 */
public class CheckVersionParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context) serializable;
        ISOMsg msg = context.get("REQUEST_KEY");
        try {
            if (msg.hasMTI() && msg.getMTI().startsWith("1")){
                    return PREPARED;
            }
            return ABORTED;
        } catch (ISOException e) {
            System.out.println(e.getMessage());
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        TransactionParticipant.super.commit(id, context);
    }

    @Override
    public void abort(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("CheckVersionParticipant: abort");
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
