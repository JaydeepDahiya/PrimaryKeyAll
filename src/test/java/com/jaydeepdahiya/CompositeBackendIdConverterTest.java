// Author: Jaydeep Dahiya
package com.jaydeepdahiya;

import com.jaydeepdahiya.config.CompositeBackendIdConverter;
import com.jaydeepdahiya.entity.composite.ContactComposite;
import com.jaydeepdahiya.entity.compositeembedded.CompositeIdEmbedded;
import com.jaydeepdahiya.entity.compositeembedded.ContactCompositeEmbedded;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the ID conversion branches used by @IdClass and @EmbeddedId resources.
 */
class CompositeBackendIdConverterTest {

    private final CompositeBackendIdConverter converter = new CompositeBackendIdConverter();

    /**
     * The @IdClass resource lookup uses the encoded String as its public identifier; the
     * conversion layer must preserve that form rather than interpreting it as the JPA key.
     */
    @Test
    void preservesEncodedIdClassCompositeResourceId() {
        String requestId = "Sm9obi5Eb2U";

        assertThat(converter.toRequestId(requestId, ContactComposite.class)).isEqualTo(requestId);
        assertThat(converter.fromRequestId(requestId, ContactComposite.class)).isEqualTo(requestId);
    }

    /**
     * Embedded IDs are converted to and from the URL-safe, unpadded Base64 representation
     * of {@code firstname.lastname}, matching the IDs emitted in REST links.
     */
    @Test
    void convertsEmbeddedCompositeKeyToAndFromBase64Url() {
        CompositeIdEmbedded id = new CompositeIdEmbedded("John", "Doe");

        String requestId = converter.toRequestId(id, ContactCompositeEmbedded.class);

        assertThat(requestId).isEqualTo("Sm9obi5Eb2U");
        assertThat(converter.fromRequestId(requestId, ContactCompositeEmbedded.class)).isEqualTo(id);
    }
}
