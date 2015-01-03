package org.osgl.mvc;

import org.osgl._;
import org.osgl.exception.NotAppliedException;
import org.osgl.http.H;
import org.osgl.mvc.result.ErrorResult;

/**
 *
 */
public class ErrorPageRenderer extends _.F3<H.Request, H.Response, ErrorResult, Void> {
    @Override
    public Void apply(H.Request request, H.Response response, ErrorResult error
    ) throws NotAppliedException, _.Break {
        H.Format fmt = request.format();
        if (request.isAjax() && fmt == H.Format.html) {
            fmt = H.Format.txt;
        }
        String s = renderTemplate(error, fmt);
        response.writeContent(s);
        return null;
    }

    protected String renderTemplate(ErrorResult error, H.Format format) {
        return error.getMessage();
    }
}
