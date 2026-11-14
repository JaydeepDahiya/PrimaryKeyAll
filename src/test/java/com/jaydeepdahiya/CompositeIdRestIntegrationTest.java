// Author: Jaydeep Dahiya
package com.jaydeepdahiya;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the @IdClass composite resource through Spring Data REST to verify its public ID
 * format and full POST/GET/DELETE lifecycle.
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CompositeIdRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Confirms POST emits the encoded pair as its self link, GET resolves that link, and the
     * custom DELETE handler removes the entity using both key fields.
     */
    @Test
    void postReturnsEncodedCompositeIdLinkAndLinkCanBeFetched() throws Exception {
        // The API contract encodes the UTF-8 bytes of "firstname.lastname" as URL-safe Base64
        // without padding; keep the expected path ID independently derived from that contract.
        String requestId = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("Rest.Composite".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(post("/contacts-composite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstname": "Rest",
                                  "lastname": "Composite",
                                  "name": "Rest Composite",
                                  "email": "rest_composite@example.com",
                                  "status": "NEW"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href").value(org.hamcrest.Matchers.endsWith(
                        "/contacts-composite/" + requestId
                )));

        mockMvc.perform(get("/contacts-composite/{id}", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("rest_composite@example.com"));

        mockMvc.perform(delete("/contacts-composite/{id}", requestId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/contacts-composite/{id}", requestId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
