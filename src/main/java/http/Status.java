package http;

public enum Status {
    OK("OK"),
    CREATED("Created"),
    NOTFOUND("Not Found"),
    ERROR("Error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }

    public static Status fromCode(int code) {
        return switch (code) {
            case 200 -> OK;
            case 201 -> CREATED;
            case 404 -> NOTFOUND;
            case 500 -> ERROR;
            default -> throw new IllegalArgumentException("Unsupported HTTP status code: " + code);
        };
    }
}
