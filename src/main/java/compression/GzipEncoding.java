package compression;

import http.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipEncoding {

    public static Response Encode(Response response) throws IOException {
        var ba = new ByteArrayOutputStream(response.body().length);
        var gzip = new GZIPOutputStream(ba);
        gzip.write(response.body());
        response.setBody(ba.toByteArray());

        return response;
    }
}
