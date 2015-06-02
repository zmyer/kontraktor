package org.nustaq.kontraktor.remoting.base;

/**
 * optional interface implementing some notification callbacks
 * related to remoting.
 */
public interface RemotedActor {

    /**
     * notification method
     */
    public void hasBeenUnpublished();

}
