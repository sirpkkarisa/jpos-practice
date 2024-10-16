package com.pkteq.JPOSServer.task;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

public class DefaultRequestListener implements ISORequestListener, Configurable {
    Configuration cfg;
    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        cfg = configuration;
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        return false;
    }
}
