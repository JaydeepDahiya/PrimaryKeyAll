// Author: Jaydeep Dahiya
/*
 * CompositeIdLookupConfig is the Spring Data REST configuration that tells the framework how to
 * resolve the public composite ID for the ContactComposite repository.
 *
 * What it does:
 * - It implements RepositoryRestConfigurer
 * - It is annotated with @Configuration
 * - In configureRepositoryRestConfiguration(...) it registers an entity lookup for
 *   ContactCompositeRepository
 *
 * Important part:
 * - It maps a ContactComposite entity to its public URL ID by calling:
 *   CompositeBackendIdConverter.encodeCompositeId(contact.getFirstname(), contact.getLastname())
 * - It also maps the incoming public ID back to the repository object by calling:
 *   repository.findById(CompositeBackendIdConverter.decodeIdClassCompositeId(encodedId))
 *
 * Why this matters:
 * - For composite IDs, Spring Data REST cannot normally use a single field as the item ID easily
 * - This config tells it:
 *   - how to generate the external ID used in links
 *   - how to decode that external ID back into the real CompositeId when receiving a request
 *
 * So in practice:
 * - If an entity has firstname=John and lastname=Doe
 * - the public resource ID becomes the Base64 form of "John.Doe"
 * - when the app receives /contacts-composite/Sm9obi5Eb2U
 * - Spring Data REST uses this config to convert it back into the actual CompositeId(John, Doe)
 * - then repository.findById(...) loads the correct record
 *
 * Usage:
 * - This is automatically invoked by Spring Data REST when serving repository item endpoints
 * - It is specifically for the @IdClass-based composite key repository, not for the @EmbeddedId version
 *
 * In short: it connects the public REST URL ID to the real database composite key.
 */
package com.jaydeepdahiya.config;

import com.jaydeepdahiya.entity.composite.ContactComposite;
import com.jaydeepdahiya.repository.ContactCompositeRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Configures the public identifier used by Spring Data REST for the {@code @IdClass}
 * {@link ContactComposite} repository.
 *
 * <p>Spring Data REST's default lookup would expose only one of the entity's {@code @Id}
 * properties. This mapping instead publishes the Base64-encoded pair and decodes that pair
 * back into the repository's {@code CompositeId} when an item resource is requested.</p>
 */
@Configuration
public class CompositeIdLookupConfig implements RepositoryRestConfigurer {

    /**
     * Registers both directions of the resource-ID mapping: entity to public ID for generated
     * links, and public ID to repository lookup for incoming item URLs.
     */
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors
    ) {
        config.withEntityLookup()
                .forRepository(ContactCompositeRepository.class)
                .withIdMapping(contact -> CompositeBackendIdConverter.encodeCompositeId(
                        contact.getFirstname(),
                        contact.getLastname()
                ))
                .withLookup((repository, encodedId) ->
                        repository.findById(CompositeBackendIdConverter.decodeIdClassCompositeId(encodedId))
                );
    }

}
