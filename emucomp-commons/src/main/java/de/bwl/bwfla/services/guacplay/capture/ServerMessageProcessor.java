package de.bwl.bwfla.services.guacplay.capture;



/* Internal class (package-private) */


import de.bwl.bwfla.services.guacplay.protocol.AsyncWorker;
import de.bwl.bwfla.services.guacplay.protocol.BufferedMessageProcessor;
import de.bwl.bwfla.services.guacplay.protocol.Message;
import de.bwl.bwfla.services.guacplay.util.RingBufferSPSC;

/**
 * A worker for processing buffered messages.
 */
final class ServerMessageProcessor extends AsyncWorker {
    private final BufferedMessageProcessor processor;

    /**
     * Constructor
     */
    public ServerMessageProcessor(BufferedMessageProcessor processor) {
        super();

        this.processor = processor;
    }


    /* ========== AsyncMessageWorker Implementation ========== */

    @Override
    protected final void execute() throws Exception {
        RingBufferSPSC<Message> messages = processor.getMessages();
        Message message = null;

        // Processing of recieved messages, until the buffer is empty
        while ((message = messages.beginTakeOp()) != null) {
            try {
                processor.process(message);
            } finally {
                message.reset();
                messages.finishTakeOp();
            }
        }

        if (this.isRunning()) {
            // At this point there are currently no messages to process, block and wait.
            condition.await(BufferedMessageProcessor.TIMEOUT_ON_EMPTY_BUFFER);
        }
    }

    @Override
    protected void finish() throws Exception {
        // Process all pending messages, before the final termination
        if (!Thread.currentThread().isInterrupted())
            this.execute();
    }

    @Override
    protected String getName() {
        return processor.getName();
    }
}
