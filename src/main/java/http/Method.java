package http;

public enum Method {
    GET,
    POST;

    public static Method fromString(String method) {
        for(Method m : Method.values()) {
            if(m.toString().equals(method)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }
}
