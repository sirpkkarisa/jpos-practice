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
        Space space = SpaceFactory.getSpace(spaceName);

        try {
            ISOMsg respMsg = (ISOMsg) isoMsg.clone();
            context.put("REQUEST_KEY",isoMsg);
            context.put("RESPONSE_KEY",respMsg);
            context.put("RESOURCE_KEY",source);

            TransactionManager tm = NameRegistrar.get("default_txnmgr");
            tm.push(context);

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
