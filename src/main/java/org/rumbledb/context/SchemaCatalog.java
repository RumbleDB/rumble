/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.context;

import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSModel;

/** The XML Schema components compiled for an XQuery module. */
public record SchemaCatalog(Schema validationSchema, XSModel schemaModel) {
}
