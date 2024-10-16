package com.pkteq.JPOSServer.boilerplate;

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

public class Server implements ISORequestListener, Configurable {
    private Configuration configuration;
    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }

    public boolean process(ISOSource source, ISOMsg m) {
        String spaceN = configuration.get("space");
        long timeout = configuration.getLong("spaceTimeout");
        String queueN = configuration.get("queue");
        Context context = new Context();
        Space<String,Context> space = SpaceFactory.getSpace(spaceN);

        try {
            ISOMsg respMsg = (ISOMsg)m.clone();
            respMsg.setResponseMTI();
            respMsg.set(39,"000");

            context.put("REQUEST_KEY",m);
            context.put("RESPONSE_KEY",respMsg);
            context.put("RESOURCE_KEY",source);

        } catch (ISOException e) {
            System.out.println(e.getMessage());
        }

        space.out(queueN,context,timeout);
        return false;
    }
}
