package br.com.grupo99.executionservice.infrastructure.persistence.mapper;

import br.com.grupo99.executionservice.domain.model.Diagnostico;
import br.com.grupo99.executionservice.infrastructure.persistence.entity.DiagnosticoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiagnosticoEntityMapper - Testes Unitários")
class DiagnosticoEntityMapperTest {

    private DiagnosticoEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiagnosticoEntityMapper();
    }

    @Test
    @DisplayName("toDomain deve converter entity para domain")
    void toDomainConvert() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        DiagnosticoEntity entity = DiagnosticoEntity.builder()
                .id(id)
                .descricao("Motor falhando")
                .mecanico("João")
                .dataDiagnostico(now)
                .observacoes("Verificar cilindros")
                .createdAt(now)
                .build();

        Diagnostico domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Motor falhando", domain.getDescricao());
        assertEquals("João", domain.getMecanico());
        assertEquals(now, domain.getDataDiagnostico());
        assertEquals("Verificar cilindros", domain.getObservacoes());
        assertEquals(now, domain.getCreatedAt());
    }

    @Test
    @DisplayName("toDomain deve retornar null para entity null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toEntity deve converter domain para entity")
    void toEntityConvert() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Diagnostico domain = Diagnostico.builder()
                .id(id)
                .descricao("Barulho no freio")
                .mecanico("Pedro")
                .dataDiagnostico(now)
                .observacoes("Checar pastilhas")
                .createdAt(now)
                .build();

        DiagnosticoEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Barulho no freio", entity.getDescricao());
        assertEquals("Pedro", entity.getMecanico());
        assertEquals(now, entity.getDataDiagnostico());
        assertEquals("Checar pastilhas", entity.getObservacoes());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    @DisplayName("toEntity deve retornar null para domain null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }
}
