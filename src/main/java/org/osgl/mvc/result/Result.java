package org.osgl.mvc.result;

import org.osgl.exception.FastRuntimeException;
import org.osgl.http.H;
import org.osgl.http.Http;
import org.osgl.mvc.MvcConfig;
import org.osgl.util.IO;
import org.osgl.util.KVStore;
import org.osgl.util.S;

public class Result extends FastRuntimeException {

    protected static class Payload extends KVStore {
        public String message;
        public Integer errorCode;
        public Throwable cause;
        public H.Format format;
        public H.Status status;

        public Payload message(String message) {
            this.message = message;
            return this;
        }

        public Payload message(String message, Object... args) {
            this.message = S.fmt(message, args);
            return this;
        }

        public Payload errorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Payload cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Payload format(H.Format format) {
            this.format = format;
            return this;
        }

        public Payload status(H.Status status) {
            this.status = status;
            return this;
        }
    }

    private transient volatile Payload _payload;

    protected static final ThreadLocal<Payload> payload = new ThreadLocal<Payload>() {
        @Override
        protected Payload initialValue() {
            return new Payload();
        }
    };

    protected Payload payload() {
        return payload.get();
    }

    private final Http.Status status;

    protected Result() {status = null;}

    protected Result(Http.Status status) {
        this.status = status;
    }

    protected Result(Http.Status status, String message) {
        super(message);
        this.status = status;
    }

    protected Result(Http.Status status, String message, Object... args) {
        super(message, args);
        this.status = status;
    }

    protected Result(Http.Status status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    protected Result(Http.Status status, Throwable cause, String message, Object... args) {
        super(cause, message, args);
        this.status = status;
    }

    public Http.Status status() {
        return status;
    }

    public int statusCode() {
        return status().code();
    }

    protected final void applyStatus(H.Response response) {
        response.status(statusCode());
    }

    protected final void applyBeforeCommitHandler(H.Request req, H.Response resp) {
        MvcConfig.applyBeforeCommitResultHandler(this, req, resp);
    }

    protected final void applyAfterCommitHandler(H.Request req, H.Response resp) {
        MvcConfig.applyAfterCommitResultHandler(this, req, resp);
    }

    protected void applyMessage(H.Request request, H.Response response) {
        String msg = getMessage();
        applyBeforeCommitHandler(request, response);
        if (S.notBlank(msg)) {
            response.writeContent(msg);
        } else {
            IO.close(response.outputStream());
        }
        applyAfterCommitHandler(request, response);
    }

    public void apply(H.Request req, H.Response resp) {
        try {
            applyStatus(resp);
            applyMessage(req, resp);
        } finally {
            clearThreadLocals();
        }
    }

    public static void clearThreadLocals() {
        payload.remove();
    }

}
