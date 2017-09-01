package org.osgl.mvc.result;

/*-
 * #%L
 * OSGL MVC
 * %%
 * Copyright (C) 2014 - 2017 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.osgl.http.H;
import org.osgl.util.E;
import org.osgl.util.S;

public abstract class RenderContent extends Result {

    private String content;
    private H.Format format;
    private boolean outputEncoding;

    /**
     * Create a RenderContent object with blank content, format and
     * the outputEncoding set to {@code true}
     *
     * @param format the content type
     */
    protected RenderContent(H.Format format) {
        this("", format, true);
    }

    /**
     * Create a RenderContent object with content, format and
     * the outputEncoding set to {@code true}
     *
     * @param content the content string
     * @param format the content type
     */
    protected RenderContent(String content, H.Format format) {
        this(content, format, true);
    }

    /**
     * Create a RenderContent object with content, format and
     * the outputEncoding set to {@code true}
     *
     * @param status the response status
     * @param content the content string
     * @param format the content type
     */
    protected RenderContent(H.Status status, String content, H.Format format) {
        this(status, content, format, true);
    }

    /**
     * Create a RenderContent object with content, format and outputEncoding
     * specified.
     *
     * <p>
     * If outputEncoding is set to {@code true} then when applying the
     * Result, it will set content type to "content-type; charset=encoding"
     * style
     * </p>
     *
     * @param content the content string
     * @param format the content type
     * @param outputEncoding output encoding
     */
    protected RenderContent(String content, H.Format format, boolean outputEncoding) {
        this(H.Status.OK, content, format, outputEncoding);
    }

    /**
     * Create a RenderContent object with content, format and outputEncoding
     * specified.
     *
     * <p>
     * If outputEncoding is set to {@code true} then when applying the
     * Result, it will set content type to "content-type; charset=encoding"
     * style
     * </p>
     *
     * @param status HTTP response status
     * @param content the content string
     * @param format the content type
     * @param outputEncoding output encoding
     */
    protected RenderContent(H.Status status, String content, H.Format format, boolean outputEncoding) {
        super(status);
        E.NPE(format);

        this.content = content;
        this.format = format;
        this.outputEncoding = outputEncoding;
    }

    protected void setContentType(H.Response resp) {
        String s = format().contentType();
        if (isOutputEncoding()) {
            String encoding = resp.characterEncoding();
            if (S.notBlank(encoding)) {
                s = S.builder(s).append("; charset=").append(encoding).toString();
            }
        }
        resp.initContentType(s);
    }

    protected boolean isOutputEncoding() {
        return outputEncoding;
    }

    public void setOutputEncoding(boolean outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public H.Format format() {
        return format;
    }

    public String content() {
        return content;
    }

    public void apply(H.Request req, H.Response resp) {
        try {
            applyStatus(resp);
            setContentType(resp);
            applyCookies(resp);
            applyHeaders(resp);
            applyBeforeCommitHandler(req, resp);
            resp.writeContent(content());
        } finally {
            try {
                resp.commit();
                applyAfterCommitHandler(req, resp);
            } finally {
                clearThreadLocals();
            }
        }
    }
}
