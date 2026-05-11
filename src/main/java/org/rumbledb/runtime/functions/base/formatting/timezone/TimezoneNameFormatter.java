package org.rumbledb.runtime.functions.base.formatting.timezone;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public interface TimezoneNameFormatter {

    String resolve(OffsetDateTime value, TimezoneNameContext context);

    String resolve(ZoneOffset offset, TimezoneNameContext context);
}
