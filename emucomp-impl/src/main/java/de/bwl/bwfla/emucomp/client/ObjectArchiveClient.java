package de.bwl.bwfla.emucomp.client;


import de.bwl.bwfla.emucomp.FileCollection;
import de.bwl.bwfla.emucomp.ObjectArchiveBinding;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ObjectArchiveClient {
    private final Client client = ClientBuilder.newClient();

    private final String host;

    public ObjectArchiveClient(String host) {
        this.host = host;
    }

    public FileCollection fetchObjectReference(ObjectArchiveBinding binding) throws Exception {
        String url = String.format("%s/archive/reference?archive=%s&id=%s", host, binding.getArchive(), binding.getObjectId());

        Response response = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(FileCollection.class);
        } else {
            throw new Exception("Failed to fetch FileCollection. HTTP error: " + response.getStatus());
        }
    }

    public void close() {
        client.close();
    }
}
