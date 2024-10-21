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
 * Responsible for routing the different types of requests depending on field 3
 */
public class RoutingRequestsParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context) serializable;
        ISOMsg msg = context.get("REQUEST_KEY");
        try {
            if (!msg.hasField(3)) {
                msg.set(39, "922"); //Message number out of sequence
                context.put("ERROR_MESSAGE","Message number out of sequence");
            } else {
                String fld3 = msg.getString(3);
                if (fld3.startsWith("31")){
                        TransactionManager tm = NameRegistrar.get("balance_enquiry_txnmgr");
                        tm.push(context);
                        return PREPARED;
                }
            }

            return ABORTED;
        } catch (NameRegistrar.NotFoundException e) {
            msg.set(39, "907");
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
        Log.getLog("Q2","participant").info("RoutingRequestsParticipant: abort");
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
