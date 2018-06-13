package com.github.jtendermint.jabci.socket;

import java.util.Optional;

/**
 * Simple Listener interface for TSocket Connections
 * 
 * @author wolfposd
 */
@FunctionalInterface
public interface ConnectionListener {

    /**
     * called when a new SocketConnection with tendermint was established
     * 
     * @param socketName
     *            the name of the Socket, if known
     * @param count
     *            the current amount of connected sockets
     */
    public void connected(Optional<String> socketName, int count);

}
