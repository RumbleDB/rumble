package org.rumbledb.runtime.functions.io;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public final class TextResourceUtil {

    private TextResourceUtil() {
    }

    public static byte[] fetchBytes(URI uri, ExceptionMetadata metadata)
            throws IOException {
        try (InputStream is = FileSystemUtil.getDataInputStream(uri, metadata)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    public static CharsetDecoder strictDecoder(Charset charset) {
        return charset.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);
    }

    public static String decodeStrict(byte[] bytes, Charset charset) throws CharacterCodingException {
        return strictDecoder(charset).decode(ByteBuffer.wrap(bytes)).toString();
    }

    /**
     * Detects the resource encoding as required by fn:json-doc and fn:unparsed-text: UTF-8, UTF-16, or UTF-32,
     * from a leading byte-order mark or from the NUL pattern of the first
     * bytes.
     */
    public static Charset detectEncoding(byte[] b) {
        int b0 = b.length > 0 ? b[0] & 0xFF : -1;
        int b1 = b.length > 1 ? b[1] & 0xFF : -1;
        int b2 = b.length > 2 ? b[2] & 0xFF : -1;
        int b3 = b.length > 3 ? b[3] & 0xFF : -1;

        if (b0 == 0x00 && b1 == 0x00 && b2 == 0xFE && b3 == 0xFF) {
            return Charset.forName("UTF-32BE");
        }
        if (b0 == 0xFF && b1 == 0xFE && b2 == 0x00 && b3 == 0x00) {
            return Charset.forName("UTF-32LE");
        }
        if (b0 == 0xFE && b1 == 0xFF) {
            return StandardCharsets.UTF_16BE;
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            return StandardCharsets.UTF_16LE;
        }
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return StandardCharsets.UTF_8;
        }

        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 > 0x00) {
            return Charset.forName("UTF-32BE");
        }
        if (b0 > 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            return Charset.forName("UTF-32LE");
        }
        if (b0 == 0x00 && b1 > 0x00) {
            return StandardCharsets.UTF_16BE;
        }
        if (b0 > 0x00 && b1 == 0x00) {
            return StandardCharsets.UTF_16LE;
        }

        return StandardCharsets.UTF_8;
    }
}
