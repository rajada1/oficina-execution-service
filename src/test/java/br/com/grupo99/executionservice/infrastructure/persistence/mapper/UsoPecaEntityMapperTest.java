package br.com.grupo99.executionservice.infrastructure.persistence.mapper;

import br.com.grupo99.executionservice.domain.model.UsoPeca;
import br.com.grupo99.executionservice.infrastructure.persistence.entity.UsoPecaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsoPecaEntityMapper - Testes Unitários")
class UsoPecaEntityMapperTest {

    private UsoPecaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UsoPecaEntityMapper();
    }

    @Test
    @DisplayName("toDomain deve converter entity para domain")
    void toDomainConvert() {
        UUID id = UUID.randomUUID();
        UUID pecaId = UUID.randomUUID();
        Instant now = Instant.now();
        UsoPecaEntity entity = UsoPecaEntity.builder()
                .id(id)
                .pecaId(pecaId)
                .descricao("Filtro de óleo")
                .quantidade(2)
                .valorUnitario(new BigDecimal("25.00"))
                .valorTotal(new BigDecimal("50.00"))
                .dataUso(now)
                .createdAt(now)
                .build();

        UsoPeca domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(pecaId, domain.getPecaId());
        assertEquals("Filtro de óleo", domain.getDescricao());
        assertEquals(2, domain.getQuantidade());
        assertEquals(new BigDecimal("25.00"), domain.getValorUnitario());
        assertEquals(new BigDecimal("50.00"), domain.getValorTotal());
        assertEquals(now, domain.getDataUso());
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
        UUID pecaId = UUID.randomUUID();
        Instant now = Instant.now();
        UsoPeca domain = UsoPeca.builder()
                .id(id)
                .pecaId(pecaId)
                .descricao("Pastilha de freio")
                .quantidade(4)
                .valorUnitario(new BigDecimal("50.00"))
                .valorTotal(new BigDecimal("200.00"))
                .dataUso(now)
                .createdAt(now)
                .build();

        UsoPecaEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(pecaId, entity.getPecaId());
        assertEquals("Pastilha de freio", entity.getDescricao());
        assertEquals(4, entity.getQuantidade());
        assertEquals(new BigDecimal("50.00"), entity.getValorUnitario());
        assertEquals(new BigDecimal("200.00"), entity.getValorTotal());
        assertEquals(now, entity.getDataUso());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    @DisplayName("toEntity deve retornar null para domain null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }
}
