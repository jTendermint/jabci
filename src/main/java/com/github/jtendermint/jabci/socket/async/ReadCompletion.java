package com.github.jtendermint.jabci.socket.async;

import java.nio.channels.CompletionHandler;

public final class ReadCompletion implements CompletionHandler<Integer, Object> {

    private Lambda d;

    @Override
    public void completed(Integer bytes, Object attachment) {
        d.onCompletion(bytes, attachment);
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.err.println(exc.getMessage());
    }

    public static ReadCompletion make(Lambda d) {
        ReadCompletion r = new ReadCompletion();
        r.d = d;
        return r;
    }

    public interface Lambda {
        public void onCompletion(Integer bytesRead, Object attachment);
    }

}
