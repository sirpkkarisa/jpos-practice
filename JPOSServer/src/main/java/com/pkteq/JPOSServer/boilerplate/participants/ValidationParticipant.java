package com.pkteq.JPOSServer.boilerplate.participants;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.Serializable;

public class ValidationParticipant implements TransactionParticipant {

    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context)serializable;
        ISOMsg resIsoMsg = context.get("REQUEST_KEY");
        Log.getLog("Q2","participant").info("ValidationParticipant: starting preparation");
        System.out.println(resIsoMsg+" MESSAGE");

        if (!resIsoMsg.hasField(4)) { // check if amount field is present
            return ABORTED;
        }
        // Your validation logic here
        Log.getLog("Q2","participant").info("ValidationParticipant: finished preparation");
        return PREPARED;
    }

    @Override
    public void commit(long id, Serializable context) {
        Log.getLog("Q2","participant").info("ValidationParticipant: commit preparation");
        TransactionParticipant.super.commit(id, context);
    }

    @Override
    public void abort(long id, Serializable context) {
        Log.getLog("Q2","participant").info("ValidationParticipant: abort preparation");

        TransactionParticipant.super.abort(id, context);
    }
}
