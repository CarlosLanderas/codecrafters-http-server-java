package compression;

import http.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipEncoding {

    public static Response encodeResponse(Response response) throws IOException {
        var ba = new ByteArrayOutputStream(response.body().length);
        try(var gzip = new GZIPOutputStream(ba)) {
            gzip.write(response.body());
            gzip.finish();
        }

        response.setBody(ba.toByteArray());

        return response;
    }
}
