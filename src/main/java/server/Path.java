package server;

import http.Method;

public record Path(String value, Method method) {
    public static Path of(String value, Method method) {
        return new Path(value, method);
    }
}
