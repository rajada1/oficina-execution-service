package br.com.grupo99.executionservice.infrastructure.persistence.mapper;

import br.com.grupo99.executionservice.domain.model.*;
import br.com.grupo99.executionservice.infrastructure.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExecucaoOSEntityMapper - Testes Unitários")
class ExecucaoOSEntityMapperTest {

    private ExecucaoOSEntityMapper mapper;

    @BeforeEach
    void setUp() {
        TarefaEntityMapper tarefaMapper = new TarefaEntityMapper();
        DiagnosticoEntityMapper diagnosticoMapper = new DiagnosticoEntityMapper();
        UsoPecaEntityMapper usoPecaMapper = new UsoPecaEntityMapper();
        mapper = new ExecucaoOSEntityMapper(tarefaMapper, diagnosticoMapper, usoPecaMapper);
    }

    @Test
    @DisplayName("toDomain deve converter entity para domain com sub-collections")
    void toDomainWithSubCollections() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        Instant now = Instant.now();

        DiagnosticoEntity diagEntity = DiagnosticoEntity.builder()
                .id(UUID.randomUUID())
                .descricao("Motor OK")
                .mecanico("João")
                .dataDiagnostico(now)
                .createdAt(now)
                .build();

        TarefaEntity tarefaEntity = TarefaEntity.builder()
                .id(UUID.randomUUID())
                .descricao("Troca de óleo")
                .mecanico("João")
                .tempoEstimadoMinutos(60)
                .status(StatusTarefa.PENDENTE)
                .dataInicio(now)
                .createdAt(now)
                .build();

        UsoPecaEntity pecaEntity = UsoPecaEntity.builder()
                .id(UUID.randomUUID())
                .pecaId(UUID.randomUUID())
                .descricao("Filtro")
                .quantidade(1)
                .valorUnitario(new BigDecimal("20.00"))
                .valorTotal(new BigDecimal("20.00"))
                .dataUso(now)
                .createdAt(now)
                .build();

        ExecucaoOSEntity entity = ExecucaoOSEntity.builder()
                .id(id)
                .osId(osId)
                .orcamentoId(orcId)
                .status(StatusExecucao.EM_ANDAMENTO)
                .mecanico("João")
                .dataInicio(now)
                .dataFinalizacao(null)
                .observacoes("obs")
                .version(1L)
                .diagnosticos(new ArrayList<>(List.of(diagEntity)))
                .tarefas(new ArrayList<>(List.of(tarefaEntity)))
                .pecas(new ArrayList<>(List.of(pecaEntity)))
                .createdAt(now)
                .updatedAt(now)
                .build();

        ExecucaoOS domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(osId, domain.getOsId());
        assertEquals(orcId, domain.getOrcamentoId());
        assertEquals(StatusExecucao.EM_ANDAMENTO, domain.getStatus());
        assertEquals("João", domain.getMecanico());
        assertEquals(now, domain.getDataInicio());
        assertNull(domain.getDataFinalizacao());
        assertEquals("obs", domain.getObservacoes());
        assertEquals(1L, domain.getVersion());
        assertEquals(1, domain.getDiagnosticos().size());
        assertEquals(1, domain.getTarefas().size());
        assertEquals(1, domain.getPecas().size());
        assertEquals(now, domain.getCreatedAt());
        assertEquals(now, domain.getUpdatedAt());
    }

    @Test
    @DisplayName("toDomain deve retornar null para entity null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain com coleções vazias")
    void toDomainEmptyCollections() {
        ExecucaoOSEntity entity = ExecucaoOSEntity.builder()
                .id(UUID.randomUUID())
                .osId(UUID.randomUUID())
                .orcamentoId(UUID.randomUUID())
                .status(StatusExecucao.AGUARDANDO_INICIO)
                .mecanico("Mec")
                .dataInicio(Instant.now())
                .diagnosticos(new ArrayList<>())
                .tarefas(new ArrayList<>())
                .pecas(new ArrayList<>())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        ExecucaoOS domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertTrue(domain.getDiagnosticos().isEmpty());
        assertTrue(domain.getTarefas().isEmpty());
        assertTrue(domain.getPecas().isEmpty());
    }

    @Test
    @DisplayName("toEntity deve converter domain para entity com sub-collections")
    void toEntityWithSubCollections() {
        ExecucaoOS domain = ExecucaoOS.criar(UUID.randomUUID(), UUID.randomUUID(), "Pedro");
        domain.adicionarDiagnostico(Diagnostico.criar("Diagnóstico 1", "Pedro", "obs"));
        domain.adicionarTarefa(Tarefa.criar("Tarefa 1", "Pedro", 30));
        domain.adicionarUsoPeca(UsoPeca.criar(UUID.randomUUID(), "Peça 1", 1, new BigDecimal("10.00")));

        ExecucaoOSEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getOsId(), entity.getOsId());
        assertEquals(domain.getOrcamentoId(), entity.getOrcamentoId());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, entity.getStatus());
        assertEquals("Pedro", entity.getMecanico());
        assertEquals(1, entity.getDiagnosticos().size());
        assertEquals(1, entity.getTarefas().size());
        assertEquals(1, entity.getPecas().size());
    }

    @Test
    @DisplayName("toEntity deve retornar null para domain null")
    void toEntityNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity com coleções vazias")
    void toEntityEmptyCollections() {
        ExecucaoOS domain = ExecucaoOS.criar(UUID.randomUUID(), UUID.randomUUID(), "Ana");
        ExecucaoOSEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertTrue(entity.getDiagnosticos().isEmpty());
        assertTrue(entity.getTarefas().isEmpty());
        assertTrue(entity.getPecas().isEmpty());
    }
}
