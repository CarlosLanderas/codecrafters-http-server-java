package http;

import compression.GzipEncoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    private final OutputStream outputStream;
    private final Request request;
    private boolean written = false;

    public ResponseWriter(OutputStream outputStream, Request request) {
        this.outputStream = outputStream;
        this.request = request;
    }

    public void write(Response response) throws IOException {
        if(written) {
            throw new RuntimeException("Response has already been written");
        }

        if(hasGzipEncoding()) {
            GzipEncoding.Encode(response);
        }

        this.outputStream.write(response.render());

        written = true;
    }

    private boolean hasGzipEncoding() {
        return this.request.acceptEncoding().isPresent()
                && this.request.acceptEncoding().get().contains("gzip");
    }

    public void close() throws IOException {
        this.outputStream.flush();
        this.outputStream.close();
    }
}
