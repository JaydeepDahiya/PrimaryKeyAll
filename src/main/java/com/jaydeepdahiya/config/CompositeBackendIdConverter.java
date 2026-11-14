// Author: Jaydeep Dahiya
/*
 * This class is CompositeBackendIdConverter in com.jaydeepdahiya.config.
 *
 * It is a Spring Data REST custom ID converter. It implements BackendIdConverter and is marked with
 * @Component, so Spring automatically uses it for certain entity types.
 *
 * What it does:
 * - It handles composite primary keys for two entities:
 *   - ContactComposite, which uses @IdClass
 *   - ContactCompositeEmbedded, which uses @EmbeddedId
 * - It converts a readable composite key like firstname.lastname into a single URL-safe value.
 * - Example: John.Doe becomes Sm9obi5Eb2U
 * - It does the reverse when a REST request comes in.
 *
 * Why this is needed:
 * - Composite IDs are usually two fields, but URLs can only carry one path segment cleanly.
 * - This class encodes both fields into one safe Base64 string and decodes them back to the real
 *   key object when Hibernate/Spring Data REST needs it.
 *
 * Core methods:
 * - supports(Class<?>): tells Spring which entities this converter applies to
 * - fromRequestId(String, Class<?>): turns the incoming URL ID back into the JPA key object
 * - toRequestId(Serializable, Class<?>): turns the JPA key into the public URL-safe ID used in links
 *
 * Usage in this project:
 * - Spring Data REST automatically calls it for /contacts-composite and /contacts-composite-embedded
 * - The public ID is encoded as firstname.lastname in Base64 without padding
 * - That keeps item links stable and allows delete/update operations to resolve the exact composite key
 *
 * Example flow:
 * - Request path contains an encoded composite ID
 * - fromRequestId decodes it into CompositeId or CompositeIdEmbedded
 * - JPA loads the entity
 * - toRequestId encodes it again when building self-links or resource URLs
 */
package com.jaydeepdahiya.config;

import com.jaydeepdahiya.entity.composite.CompositeId;
import com.jaydeepdahiya.entity.composite.ContactComposite;
import com.jaydeepdahiya.entity.compositeembedded.CompositeIdEmbedded;
import com.jaydeepdahiya.entity.compositeembedded.ContactCompositeEmbedded;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Controls how Spring Data REST renders and parses IDs for entities with composite keys.
 *
 * <p>The public resource ID is URL-safe Base64 of {@code firstname.lastname}; for example,
 * {@code John.Doe} becomes {@code Sm9obi5Eb2U}. Encoding the complete key keeps it in one URL
 * path segment. The {@link ContactComposite} entity uses {@code @IdClass}, while
 * {@link ContactCompositeEmbedded} uses {@code @EmbeddedId}; Spring Data REST exposes different
 * identifier object types for these mappings, so the conversion branches handle each type
 * explicitly.</p>
 */
@Component
public class CompositeBackendIdConverter implements BackendIdConverter {

    @Override
    public boolean supports(Class<?> entityType) {
        // Do not change ID handling for other repositories.
        return ContactComposite.class.equals(entityType)
                || ContactCompositeEmbedded.class.equals(entityType);
    }

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        if (ContactComposite.class.equals(entityType)) {
            // EntityLookup handles this String form and resolves it to the full @IdClass key.
            return id;
        }
        String[] parts = decodeCompositeId(id);
        if (ContactCompositeEmbedded.class.equals(entityType)) {
            // The embedded-key repository can use its @EmbeddedId directly.
            return new CompositeIdEmbedded(parts[0], parts[1]);
        }
        throw new IllegalArgumentException("Unsupported composite-ID entity type: " + entityType.getName());
    }

    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        // EntityLookup supplies a String resource ID for @IdClass entities; preserve it as-is.
        if (ContactComposite.class.equals(entityType) && id instanceof String resourceId) {
            return resourceId;
        }
        // Depending on the Spring Data REST link-building path, the @IdClass ID may be
        // represented by the key object or by the entity itself.
        if (ContactComposite.class.equals(entityType) && id instanceof CompositeId compositeId) {
            return encodeCompositeId(compositeId.getFirstname(), compositeId.getLastname());
        }
        if (ContactComposite.class.equals(entityType) && id instanceof ContactComposite contact) {
            return encodeCompositeId(contact.getFirstname(), contact.getLastname());
        }
        if (ContactCompositeEmbedded.class.isAssignableFrom(entityType)
                && id instanceof CompositeIdEmbedded compositeId) {
            return encodeCompositeId(compositeId.getFirstname(), compositeId.getLastname());
        }
        if (ContactCompositeEmbedded.class.isAssignableFrom(entityType)
                && id instanceof ContactCompositeEmbedded contact) {
            CompositeIdEmbedded compositeId = contact.getCompositeIdEmbedded();
            return encodeCompositeId(compositeId.getFirstname(), compositeId.getLastname());
        }
        throw new IllegalArgumentException(
                "Unsupported ID type " + id.getClass().getName() + " for entity type: " + entityType.getName()
        );
    }

    /**
     * Converts a public resource ID into the JPA {@link CompositeId} used by the @IdClass
     * repository. The explicit method is also used by operations, such as DELETE, that need
     * the repository key rather than the resource ID string.
     */
    public static CompositeId decodeIdClassCompositeId(String encodedId) {
        String[] parts = decodeCompositeId(encodedId);
        return new CompositeId(parts[0], parts[1]);
    }

    /**
     * Encodes both key fields together so the ID remains one URL path segment.
     * URL-safe Base64 without padding avoids reserved path characters and extra trailing
     * {@code =} characters.
     */
    static String encodeCompositeId(String firstname, String lastname) {
        String compositeId = firstname + "." + lastname;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(compositeId.getBytes(StandardCharsets.UTF_8));
    }

    private static String[] decodeCompositeId(String encodedId) {
        // Split only at the first separator so a lastname may itself contain periods.
        String compositeId = new String(
                Base64.getUrlDecoder().decode(encodedId),
                StandardCharsets.UTF_8
        );
        int separator = compositeId.indexOf('.');
        if (separator <= 0 || separator == compositeId.length() - 1) {
            throw new IllegalArgumentException("Decoded composite ID must contain firstname.lastname");
        }
        return new String[]{compositeId.substring(0, separator), compositeId.substring(separator + 1)};
    }
}
