package br.com.grupo99.executionservice.infrastructure.persistence.mapper;

import br.com.grupo99.executionservice.domain.model.StatusTarefa;
import br.com.grupo99.executionservice.domain.model.Tarefa;
import br.com.grupo99.executionservice.infrastructure.persistence.entity.TarefaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TarefaEntityMapper - Testes Unitários")
class TarefaEntityMapperTest {

    private TarefaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TarefaEntityMapper();
    }

    @Test
    @DisplayName("toDomain deve converter entity para domain")
    void toDomainConvert() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        TarefaEntity entity = TarefaEntity.builder()
                .id(id)
                .descricao("Troca de óleo")
                .mecanico("João")
                .tempoEstimadoMinutos(60)
                .tempoRealMinutos(55)
                .status(StatusTarefa.CONCLUIDA)
                .dataInicio(now)
                .dataFinalizacao(now)
                .createdAt(now)
                .build();

        Tarefa domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Troca de óleo", domain.getDescricao());
        assertEquals("João", domain.getMecanico());
        assertEquals(60, domain.getTempoEstimadoMinutos());
        assertEquals(55, domain.getTempoRealMinutos());
        assertEquals(StatusTarefa.CONCLUIDA, domain.getStatus());
        assertEquals(now, domain.getDataInicio());
        assertEquals(now, domain.getDataFinalizacao());
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
        Tarefa domain = Tarefa.builder()
                .id(id)
                .descricao("Alinhamento")
                .mecanico("Pedro")
                .tempoEstimadoMinutos(45)
                .tempoRealMinutos(null)
                .status(StatusTarefa.PENDENTE)
                .dataInicio(now)
                .createdAt(now)
                .build();

        TarefaEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Alinhamento", entity.getDescricao());
        assertEquals("Pedro", entity.getMecanico());
        assertEquals(45, entity.getTempoEstimadoMinutos());
        assertNull(entity.getTempoRealMinutos());
        assertEquals(StatusTarefa.PENDENTE, entity.getStatus());
    }

    @Test
    @DisplayName("toEntity deve retornar null para domain null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }
}
