package org.example.ebankbackend.repository;

import org.example.ebankbackend.domain.entity.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryDataJpaTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void existsByEmail_shouldReturnTrue_whenClientWithEmailExists() {
        // Arrange
        Client client = new Client();
        client.setFirstName("Test");
        client.setLastName("User");
        client.setIdentityNumber("ID-TEST-001");
        client.setBirthDate(LocalDate.of(2000, 1, 1));
        client.setEmail("test.user@example.com");
        client.setPostalAddress("Test Address");

        clientRepository.save(client);

        // Act
        boolean exists = clientRepository.existsByEmail("test.user@example.com");

        // Assert
        assertThat(exists).isTrue();
    }
}
