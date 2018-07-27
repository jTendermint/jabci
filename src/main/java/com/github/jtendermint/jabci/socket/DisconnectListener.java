package com.github.jtendermint.jabci.socket;

import java.util.Optional;

/**
 * Simple Listener interface for TSocket Disconnections
 * 
 * @author wolfposd
 */
@FunctionalInterface
public interface DisconnectListener {

    /**
     * called when a new SocketConnection with tendermint was terminated
     * 
     * @param socketName
     *            the name of the Socket, if known
     * @param remaining
     *            the remaining amount of connected sockets
     */
    public void disconnected(Optional<String> socketName, int remaining);

}
