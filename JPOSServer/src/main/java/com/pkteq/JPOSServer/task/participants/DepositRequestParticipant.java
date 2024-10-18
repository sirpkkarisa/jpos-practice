package com.pkteq.JPOSServer.task.participants;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.IOException;
import java.io.Serializable;

public class DepositRequestParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        System.out.println("DepositRequestParticipant");
        return PREPARED;
    }

    @Override
    public void commit(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("DepositRequestParticipant: commit preparation");
        sendMessage((Context) serializable);
    }

    @Override
    public void abort(long id, Serializable serializable) {
        Log.getLog("Q2","participant").info("DepositRequestParticipant: abort preparation");
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
