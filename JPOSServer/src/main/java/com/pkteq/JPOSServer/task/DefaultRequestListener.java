package com.pkteq.JPOSServer.task;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionManager;
import org.jpos.util.NameRegistrar;

import java.io.IOException;

public class DefaultRequestListener implements ISORequestListener, Configurable {
    Configuration cfg;
    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        cfg = configuration;
    }

    @Override
    public boolean process(ISOSource source, ISOMsg isoMsg) {
        String spaceName = cfg.get("space");
        long timeout = cfg.getLong("spaceTimeout");
        String queueN = cfg.get("queue");
        Context context = new Context();
//        spaceName = "balance_enquiry_queue";
        Space space = SpaceFactory.getSpace(spaceName);
        System.out.println(spaceName+" QUEUE: "+queueN);

        try {
            if (!isoMsg.hasField(3)) {
                isoMsg.set(39, "922"); //Message number out of sequence
                context.put("ERROR_MESSAGE","Message number out of sequence");
            } else {
                String fld3 = isoMsg.getString(3);
                switch (fld3) {
                    case "310000" -> {
                        // Handle Balance Enquiry
                        TransactionManager tm = NameRegistrar.get("balance_enquiry_txnmgr");
//                        tm.run();
//                        space = tm.getSpace();
//                        queueN = tm.getQueueName();
//                        space.out(queueN,);
                        isoMsg.set(39, "000");
                        context.put("REQUEST_KEY",isoMsg);
                        ISOMsg resp = (ISOMsg)isoMsg.clone();
                        context.put("RESPONSE_KE",resp);
                        context.put("RESOURCE_KEY",source);
                        tm.push(context);


//                        source.send(isoMsg);
                        System.out.println("BAL Enquiry Routing");
                    }
                    case "200000" -> {
                        TransactionManager tm = NameRegistrar.get("deposit_txnmgr");
//                        tm.run();
//                        space = tm.getSpace();
//                        queueN = tm.getQueueName();
//                        space.out(queueN,);
                        isoMsg.set(39, "000");
                        context.put("REQUEST_KEY", isoMsg);
                        ISOMsg resp = (ISOMsg) isoMsg.clone();
                        context.put("RESPONSE_KE", resp);
                        context.put("RESOURCE_KEY", source);
                        tm.push(context);

                    }
//                        source.send(isoMsg);
                    case "010000" -> System.out.println("withdrawal");
                    default -> {
                        isoMsg.set(39, "908"); //Transaction destination cannot be found for routing
                        context.put("ERROR_MESSAGE","Message number out of sequence");
                    }
                }
            }

            source.send(isoMsg);
            return true;
        }catch (Exception e) {
            isoMsg.set(39,"907");
            context.put("ERROR_MESSAGE","Message number out of sequence");
            try {
                source.send(isoMsg);
            } catch (IOException | ISOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(e.getMessage());
        }

        space.out(queueN,context,timeout);
        return false;
    }
}
