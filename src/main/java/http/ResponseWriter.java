package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    private final OutputStream outputStream;
    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    public ResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(byte[] data) throws IOException {
      this.outputStream.write(data);
    }

    public void close() throws IOException {
        this.outputStream.flush();
        this.outputStream.close();
    }
}
