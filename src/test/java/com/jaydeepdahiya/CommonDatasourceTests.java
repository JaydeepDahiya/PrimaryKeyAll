// Author: Jaydeep Dahiya
package com.jaydeepdahiya;

import com.jaydeepdahiya.audit.AuditorAwareImpl;
import com.jaydeepdahiya.entity.*;
import com.jaydeepdahiya.entity.composite.CompositeId;
import com.jaydeepdahiya.entity.composite.ContactComposite;
import com.jaydeepdahiya.entity.compositeembedded.CompositeIdEmbedded;
import com.jaydeepdahiya.entity.compositeembedded.ContactCompositeEmbedded;
import com.jaydeepdahiya.entity.table.AuthorTable;
import com.jaydeepdahiya.entity.table.BookTable;
import com.jaydeepdahiya.entity.table.ContactTable;
import com.jaydeepdahiya.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(AuditorAwareImpl.class)
public abstract class CommonDatasourceTests {

    @Autowired
    ContactSequenceRepository contactSequenceRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AuthorTableRepository authorTableRepository;

    @Autowired
    BookTableRepository bookTableRepository;

    @Autowired
    ContactTableRepository contactTableRepository;

    @Autowired
    ContactIdentityRepository contactIdentityRepository;

    @Autowired
    ContactUUIDRepository contactUUIDRepository;

    @Autowired
    ContactUUIDBinaryRepository contactUUIDBinaryRepository;

    @Autowired
    ContactNaturalRepository contactNaturalRepository;

    @Autowired
    ContactCompositeRepository contactCompositeRepository;

    @Autowired
    ContactCompositeEmbeddedRepository contactCompositeEmbeddedRepository;

    @Test
    @DisplayName("TableTest")
    void tableTest() {
        AuthorTable authorTable = AuthorTable.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        BookTable bookTable = BookTable.builder()
                .title("My First Book")
                .isbn("Some ISBN")
                .publisher("My Publisher")
                .build();

        ContactTable contactTable = ContactTable.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        // Verify generated IDs advance independently for each entity's sequence segment.
        AuthorTable savedAuthorTable = authorTableRepository.save(authorTable);
        assertThat(savedAuthorTable).isNotNull();
        assertThat(savedAuthorTable.getId()).isNotNull();
        assertThat(savedAuthorTable.getFirstName()).isEqualTo("John");
        assertThat(savedAuthorTable.getLastName()).isEqualTo("Doe");

        AuthorTable secondAuthor = authorTableRepository.save(AuthorTable.builder()
                .firstName("Jane")
                .lastName("Doe")
                .build());
        assertThat(secondAuthor.getId()).isGreaterThan(savedAuthorTable.getId());

        // Confirm generated IDs can be used to retrieve the saved entities.
        Optional<AuthorTable> fetchedAuthorTable = authorTableRepository.findById(savedAuthorTable.getId());
        assertThat(fetchedAuthorTable).isPresent();
        assertThat(fetchedAuthorTable.orElseThrow().getFirstName()).isEqualTo("John");
        assertThat(fetchedAuthorTable.orElseThrow().getLastName()).isEqualTo("Doe");

        BookTable savedBookTable = bookTableRepository.save(bookTable);
        assertThat(savedBookTable).isNotNull();
        assertThat(savedBookTable.getId()).isNotNull();
        assertThat(savedBookTable.getPublisher()).isEqualTo("My Publisher");
        assertThat(savedBookTable.getTitle()).isEqualTo("My First Book");

        BookTable secondBook = bookTableRepository.save(BookTable.builder()
                .title("My Second Book")
                .isbn("Another ISBN")
                .publisher("My Publisher")
                .build());
        assertThat(secondBook.getId()).isGreaterThan(savedBookTable.getId());

        Optional<BookTable> fetchedBookTable = bookTableRepository.findById(savedBookTable.getId());
        assertThat(fetchedBookTable).isPresent();
        assertThat(fetchedBookTable.orElseThrow().getIsbn()).isEqualTo("Some ISBN");

        // ContactTable also inherits auditing fields from BaseEntity.
        ContactTable savedContactTable = contactTableRepository.save(contactTable);
        assertThat(savedContactTable).isNotNull();
        assertThat(savedContactTable.getId()).isNotNull();
        assertThat(savedContactTable.getName()).isEqualTo("John Doe");
        assertThat(savedContactTable.getEmail()).isEqualTo("john_doe@gmail.com");
        assertThat(savedContactTable.getCreatedAt()).isNotNull();
        assertThat(savedContactTable.getCreatedBy()).isEqualTo(System.getProperty("user.name"));

        ContactTable secondContact = contactTableRepository.save(ContactTable.builder()
                .name("Jane Doe")
                .email("jane_doe@gmail.com")
                .status("NEW")
                .build());
        assertThat(secondContact.getId()).isGreaterThan(savedContactTable.getId());

        Optional<ContactTable> fetched = contactTableRepository.findById(savedContactTable.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // Each generator should create exactly one row using its configured pkColumnValue.
        List<String> sequenceSegments = jdbcTemplate.queryForList(
                "SELECT seq_name FROM common_seq WHERE seq_name IN (?, ?, ?) ORDER BY seq_name",
                String.class,
                "authors", "books", "contacts"
        );
        assertThat(sequenceSegments.size()).isEqualTo(3);
        assertTrue(sequenceSegments.containsAll(List.of("authors", "books", "contacts")));

        // Each entity type has two rows, while sharing the common sequence table.
        assertThat(authorTableRepository.findAll().size()).isEqualTo(2);
        assertThat(bookTableRepository.findAll().size()).isEqualTo(2);
        assertThat(contactTableRepository.findAll().size()).isEqualTo(2);

        // Updating an existing row should populate the last-modified audit fields.
        savedContactTable.setName("John Doe Updated");
        ContactTable updatedContact = contactTableRepository.saveAndFlush(savedContactTable);
        assertThat(updatedContact.getUpdatedAt()).isNotNull();
        assertThat(updatedContact.getUpdatedBy()).isEqualTo(System.getProperty("user.name"));

        // Deleting entities must not remove their sequence segments.
        authorTableRepository.deleteById(savedAuthorTable.getId());
        authorTableRepository.deleteById(secondAuthor.getId());
        bookTableRepository.deleteById(savedBookTable.getId());
        bookTableRepository.deleteById(secondBook.getId());
        contactTableRepository.deleteById(savedContactTable.getId());
        contactTableRepository.deleteById(secondContact.getId());
        assertTrue(authorTableRepository.findAll().isEmpty());
        assertTrue(bookTableRepository.findAll().isEmpty());
        assertTrue(contactTableRepository.findAll().isEmpty());
        assertThat(jdbcTemplate.queryForList(
                "SELECT seq_name FROM common_seq WHERE seq_name IN (?, ?, ?)",
                String.class,
                "authors", "books", "contacts"
        ).size()).isEqualTo(3);

    }

    @Test
    @DisplayName("IdentityTest")
    void contactIdentityTest() {
        ContactIdentity contactIdentity = ContactIdentity.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactIdentity saved = contactIdentityRepository.save(contactIdentity);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        Optional<ContactIdentity> fetched = contactIdentityRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactIdentityRepository.findAll().size()).isOne();

        // should be zero
        contactIdentityRepository.deleteById(saved.getId());
        assertThat(contactIdentityRepository.findAll().size()).isZero();

    }

    @Test
    @DisplayName("UUIDTest")
    void contactUUIDTest() {
        ContactUUID contactUUID = ContactUUID.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactUUID saved = contactUUIDRepository.save(contactUUID);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        Optional<ContactUUID> fetched = contactUUIDRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactUUIDRepository.findAll().size()).isOne();

        // should be zero
        contactUUIDRepository.deleteById(saved.getId());
        assertThat(contactUUIDRepository.findAll().size()).isZero();

    }

    @Test
    @DisplayName("UUIDBinaryTest")
    void contactUUIDBinaryTest() {
        ContactUUIDBinary contactUUIDBinary = ContactUUIDBinary.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactUUIDBinary saved = contactUUIDBinaryRepository.save(contactUUIDBinary);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        Optional<ContactUUIDBinary> fetched = contactUUIDBinaryRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactUUIDBinaryRepository.findAll().size()).isOne();

        // should be zero
        contactUUIDBinaryRepository.deleteById(saved.getId());
        assertThat(contactUUIDBinaryRepository.findAll().size()).isZero();

    }

    @Test
    @DisplayName("NaturalTest")
    void contactNaturalTest() {
        ContactNatural contactNatural = ContactNatural.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactNatural saved = contactNaturalRepository.save(contactNatural);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        Optional<ContactNatural> fetched = contactNaturalRepository.findById("john_doe@gmail.com");
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactNaturalRepository.findAll().size()).isOne();

        // should be zero
        contactNaturalRepository.deleteById("john_doe@gmail.com");
        assertThat(contactNaturalRepository.findAll().size()).isZero();

    }

    @Test
    @DisplayName("CompositeTest")
    void contactCompositeTest() {
        CompositeId compositeId = new CompositeId("John", "Doe");
        ContactComposite contactComposite = ContactComposite.builder()
                .firstname(compositeId.getFirstname())
                .lastname(compositeId.getLastname())
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactComposite saved = contactCompositeRepository.save(contactComposite);
        assertThat(saved).isNotNull();
        assertThat(saved.getFirstname()).isEqualTo(compositeId.getFirstname());
        assertThat(saved.getLastname()).isEqualTo(compositeId.getLastname());
        assertThat(saved.getName()).isEqualTo("John Doe");

        Optional<ContactComposite> fetched = contactCompositeRepository.findById(compositeId);
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactCompositeRepository.findAll().size()).isOne();

        // should be zero
        contactCompositeRepository.deleteById(compositeId);
        assertThat(contactCompositeRepository.findAll().size()).isZero();

    }

    @Test
    @DisplayName("CompositeEmbeddedTest")
    void contactCompositeEmbeddedTest() {
        CompositeIdEmbedded compositeIdEmbedded = new CompositeIdEmbedded("John", "Doe");
        ContactCompositeEmbedded contactCompositeEmbedded = ContactCompositeEmbedded.builder()
                .compositeIdEmbedded(compositeIdEmbedded)
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactCompositeEmbedded saved = contactCompositeEmbeddedRepository.save(contactCompositeEmbedded);
        assertThat(saved).isNotNull();
        assertThat(saved.getCompositeIdEmbedded()).isEqualTo(compositeIdEmbedded);
        assertThat(saved.getName()).isEqualTo("John Doe");

        Optional<ContactCompositeEmbedded> fetched = contactCompositeEmbeddedRepository.findById(compositeIdEmbedded);
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactCompositeEmbeddedRepository.findAll().size()).isOne();

        // should be zero
        contactCompositeEmbeddedRepository.deleteById(compositeIdEmbedded);
        assertThat(contactCompositeEmbeddedRepository.findAll().size()).isZero();

    }

}

@ActiveProfiles("test")
class H2Test extends CommonDatasourceTests {
    @Test
    @DisplayName("NativeSequenceTest")
    void contactSequenceTest() {
        ContactSequence contactSequence = ContactSequence.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactSequence saved = contactSequenceRepository.save(contactSequence);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        // H2 uses a native database sequence, so verify it exists in the sequence metadata.
        Integer sequenceCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SEQUENCES " +
                        "WHERE SEQUENCE_NAME = 'CONTACTS_SEQUENCE_SEQ'",
                Integer.class
        );
        assertThat(sequenceCount).isEqualTo(1);

        Optional<ContactSequence> fetched = contactSequenceRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactSequenceRepository.findAll().size()).isOne();

        // should be zero
        contactSequenceRepository.deleteById(saved.getId());
        assertThat(contactSequenceRepository.findAll().size()).isZero();

    }
}

@ActiveProfiles("prod")
@TestPropertySource("classpath:application-prod.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("MySQLTest")
class MySQLIntegrationTest extends CommonDatasourceTests {

    @Test
    @DisplayName("SequenceTableTest")
    void contactSequenceTableTest() {
        ContactSequence contactSequence = ContactSequence.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .status("NEW")
                .build();

        ContactSequence saved = contactSequenceRepository.save(contactSequence);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("John Doe");
        assertThat(saved.getEmail()).isEqualTo("john_doe@gmail.com");

        // MySQL emulates sequences with a table; verify the generator advanced its stored value.
        Long nextSequenceValue = jdbcTemplate.queryForObject(
                "SELECT next_val FROM contacts_sequence_seq",
                Long.class
        );
        assertThat(nextSequenceValue).isGreaterThan(1L);

        Optional<ContactSequence> fetched = contactSequenceRepository.findById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.orElseThrow().getEmail()).isEqualTo("john_doe@gmail.com");

        // should be one
        assertThat(contactSequenceRepository.findAll().size()).isOne();

        // should be zero
        contactSequenceRepository.deleteById(saved.getId());
        assertThat(contactSequenceRepository.findAll().size()).isZero();
    }
}