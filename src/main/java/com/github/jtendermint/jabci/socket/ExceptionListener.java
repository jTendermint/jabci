package com.github.jtendermint.jabci.socket;

/**
 * Simple Listener interface for TSocket failures that could occur
 * 
 * @author wolfposd
 */
@FunctionalInterface
public interface ExceptionListener {

    /**
     * called when am exception occured somewhere in the TSocket
     * 
     * @param exception
     *            the original exception
     * @param event
     *            the Event that was happening in the TSocket
     */
    public void notifyExceptionOccured(Exception exception, Event event);

    public enum Event {

        ServerSocket, //
        Socket_accept, //

        SocketHandler_closeConnections, //
        SocketHandler_run, //

        Thread_sleep
    }

}
