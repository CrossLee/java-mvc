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

import org.osgl.http.Http;
import org.osgl.util.S;

public class SeeOther extends RedirectBase {

    private static SeeOther _INSTANCE = new SeeOther() {
        @Override
        protected String url() {
            return payload().message;
        }

        @Override
        public long timestamp() {
            return payload().timestamp;
        }
    };

    private SeeOther() {
        super(Http.Status.SEE_OTHER);
    }

    public SeeOther(String url) {
        super(Http.Status.SEE_OTHER, url);
    }

    public SeeOther(String url, Object... args) {
        this(S.fmt(url, args));
    }

    public static SeeOther of(String url) {
        touchPayload().message(url);
        return _INSTANCE;
    }

    public static SeeOther of(String url, Object... args) {
        touchPayload().message(url, args);
        return _INSTANCE;
    }

}
