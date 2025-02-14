package de.bwl.bwfla.control.connectors;

import java.net.URI;

/*
    Info dummy connector. 
 */
public class InfoDummyConnector implements IConnector {

    private String info;

    public InfoDummyConnector(String info)
    {
        this.info = info;
    }

    @Override
    public URI getControlPath(URI componentResource) {
        try {
            return URI.create("info://hostname/"  + info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getProtocol() {
        return "info";
    }
}
