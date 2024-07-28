package http;

public enum Status {
    Ok("OK"),
    NotFound("Not Found");

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
            case 404 -> NotFound;
            default -> throw new IllegalArgumentException("Unsupported HTTP status code: " + code);
        };
    }


}
