// Author: Jaydeep Dahiya
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
