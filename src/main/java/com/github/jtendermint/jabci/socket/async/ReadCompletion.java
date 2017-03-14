package com.github.jtendermint.jabci.socket.async;

import java.nio.channels.CompletionHandler;

public final class ReadCompletion implements CompletionHandler<Integer, Object> {

    private Lambda lambda;

    @Override
    public void completed(Integer bytes, Object attachment) {
        lambda.onCompletion(bytes, attachment);
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.err.println(exc.getMessage());
    }

    public static ReadCompletion lambda(Lambda lambda) {
        return new ReadCompletion(lambda);
    }

    private ReadCompletion(Lambda lamb) {
        this.lambda = lamb;
    }

    public interface Lambda {
        public void onCompletion(Integer bytesRead, Object attachment);
    }
}
