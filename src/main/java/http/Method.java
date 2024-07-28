package http;

public enum Method {
    GET,
    PUT,
    POST;

    public static Method fromString(String method) {
        for(Method m : Method.values()) {
            if(m.toString().equalsIgnoreCase(method)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Not implemented method: " + method);
    }
}
