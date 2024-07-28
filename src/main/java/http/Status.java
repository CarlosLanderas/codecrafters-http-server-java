package http;

public enum Status {
    Ok("OK"),
    Created("Created"),
    NotFound("Not Found"),
    Error("Error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }

    public static Status fromCode(int code) {
        return switch (code) {
            case 200 -> Ok;
            case 201 -> Created;
            case 404 -> NotFound;
            case 500 -> Error;
            default -> throw new IllegalArgumentException("Unsupported HTTP status code: " + code);
        };
    }


}
