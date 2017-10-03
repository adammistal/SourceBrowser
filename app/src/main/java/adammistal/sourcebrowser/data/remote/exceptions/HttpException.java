package adammistal.sourcebrowser.data.remote.exceptions;


import java.io.IOException;

import okhttp3.Response;

public class HttpException extends RuntimeException {
    private static String getMessage(Response response) {
        return "HTTP " + response.code() + " " + response.message();
    }

    private final int code;
    private final String message;
    private final transient Response response;

    public HttpException(Response response) {
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Response response() {
        return response;
    }

    public String getErrorString() {
        try {
            return response.body().string();
        } catch (IOException e) {
            return "";
        }
    }
}
