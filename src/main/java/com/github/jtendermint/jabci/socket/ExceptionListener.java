package com.github.jtendermint.jabci.socket;

import java.util.Optional;

/**
 * Simple Listener interface for TSocket failures that could occur
 * 
 * @author wolfposd
 */
@FunctionalInterface
public interface ExceptionListener {

    /**
     * called when am exception occured somewhere in the TSocket
     * @param event
     *            the Event that was happening in the TSocket
     * @param exception
     *            the original exception
     */
    public void notifyExceptionOccured(Optional<String> socketName, Event event, Exception exception);

    public enum Event {

        ServerSocket, //
        Socket_accept, //

        SocketHandler_closeConnections, //
        SocketHandler_run, //
        SocketHandler_readFromStream, //
        SocketHandler_handleRequest, //

        Thread_sleep
    }

}
