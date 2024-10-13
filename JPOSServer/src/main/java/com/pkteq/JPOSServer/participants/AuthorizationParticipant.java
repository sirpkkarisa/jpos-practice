package com.pkteq.JPOSServer.participants;


import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.IOException;
import java.io.Serializable;

public class AuthorizationParticipant implements TransactionParticipant {
    @Override
    public int prepare(long l, Serializable serializable) {
        Context context = (Context)serializable;
        ISOMsg resIsoMsg = context.get("REQUEST_KEY");
        ISOSource source = context.get("RESOURCE_KEY");
        Log.getLog("Q2","participant").info("AuthorizationParticipant: starting preparation");
        try {
            resIsoMsg.setResponseMTI();

            // Authorize transaction (dummy authorization check)
            String amount = resIsoMsg.getString(4);  // Assume Field 4 contains amount
            if (Integer.parseInt(amount) > 1000) {
                resIsoMsg.set(39,"119");
                context.put("RESPONSE_KEY",resIsoMsg);
                source.send(resIsoMsg);
                return ABORTED;
            }

            resIsoMsg.set(39,"000");
            context.put("RESPONSE_KEY",resIsoMsg);
        } catch (NumberFormatException e) {
            return ABORTED;
        } catch (ISOException | IOException e) {
            throw new RuntimeException(e);
        }
        return PREPARED;
    }

    @Override
    public void commit(long id, Serializable context) {
        Log.getLog("Q2","participant").info("AuthorizationParticipant: commit preparation");

        TransactionParticipant.super.commit(id, context);
    }

    @Override
    public void abort(long id, Serializable context) {
        Log.getLog("Q2","participant").info("AuthorizationParticipant: abort preparation");

        TransactionParticipant.super.abort(id, context);
    }
}
