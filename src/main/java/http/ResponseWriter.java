package http;

import compression.GzipEncoding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ResponseWriter {
    private final OutputStream outputStream;
    private final Request request;
    private final ReentrantLock writeLock = new ReentrantLock();
    private boolean written = false;

    public ResponseWriter(OutputStream outputStream, Request request) {
        this.outputStream = outputStream;
        this.request = request;
    }

    public void write(Response response) throws IOException {
        writeLock.lock();
        try {
            if (written) {
                throw new RuntimeException("Response has already been written");
            }

            if (hasGzipEncoding()) {
                response.setContentEncoding(Optional.of("gzip"));
                GzipEncoding.encodeResponse(response);
            }

            this.outputStream.write(response.render());

            written = true;
        } finally {
            writeLock.unlock();
        }
    }

    private boolean hasGzipEncoding() {
        return (this.request.acceptEncoding().isPresent()
                && this.request.acceptEncoding().get().contains("gzip"));
    }

    public void close() throws IOException {
        this.outputStream.flush();
        this.outputStream.close();
    }
}
