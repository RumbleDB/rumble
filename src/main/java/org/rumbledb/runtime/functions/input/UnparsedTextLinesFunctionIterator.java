/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.input;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidEncodingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.items.parsing.StringToStringItemMapper;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.URIUtils;

import sparksoniq.spark.SparkSessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UnparsedTextLinesFunctionIterator extends RDDRuntimeIterator {

    private static final long serialVersionUID = 1L;
    public static final int MIN_PARTITIONS = 10;

    public UnparsedTextLinesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        Item url = urlIterator.materializeFirstItemOrNull(context);
        if (url == null) {
            return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .emptyRDD();
        }

        try {
            URI uri = resolveURI(url.getStringValue());
            Charset charset = getCharset(context);
            JavaRDD<String> strings;
            if (this.children.size() == 2 || "http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                List<String> lines = new ArrayList<>();
                try (
                    InputStream is = FileSystemUtil.getDataInputStream(
                        uri,
                        context.getRumbleRuntimeConfiguration(),
                        getMetadata()
                    );
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, charset))
                ) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                }
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(lines);
            } else {
                if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
                    throw new UnavailableResourceException("File " + uri + " not found.", getMetadata());
                }
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .textFile(FileSystemUtil.convertURIToStringForSpark(uri), MIN_PARTITIONS);
            }
            return strings.mapPartitions(new StringToStringItemMapper());
        } catch (InvalidEncodingException | UnavailableResourceException e) {
            throw e;
        } catch (RumbleException e) {
            UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
            ex.initCause(e);
            throw ex;
        } catch (IOException e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Unable to read the resource supplied to fn:unparsed-text-lines().",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private URI resolveURI(String href) {
        try {
            URI uri = URIUtils.resolveURIReference(this.staticURI, href);
            if (uri.getFragment() != null) {
                throw new URISyntaxException(href, "Fragment identifiers are not allowed");
            }
            return uri;
        } catch (URISyntaxException e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Invalid URI supplied to fn:unparsed-text-lines(): " + href,
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private Charset getCharset(DynamicContext context) {
        if (this.children.size() == 1) {
            return StandardCharsets.UTF_8;
        }
        Item encoding = this.children.get(1).materializeFirstItemOrNull(context);
        try {
            return Charset.forName(encoding.getStringValue());
        } catch (Exception e) {
            InvalidEncodingException ex = new InvalidEncodingException(
                    "Invalid encoding supplied to fn:unparsed-text-lines(): " + encoding.getStringValue(),
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
