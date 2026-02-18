package br.com.grupo99.executionservice.infrastructure.persistence.entity;

import br.com.grupo99.executionservice.domain.model.StatusExecucao;
import br.com.grupo99.executionservice.domain.model.StatusTarefa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Persistence Entities - Testes Unitários")
class EntitiesTest {

    // --- ExecucaoOSEntity ---
    @Test
    @DisplayName("ExecucaoOSEntity - builder e getters")
    void execucaoOSEntityBuilder() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        Instant now = Instant.now();

        ExecucaoOSEntity entity = ExecucaoOSEntity.builder()
                .id(id)
                .osId(osId)
                .orcamentoId(orcId)
                .status(StatusExecucao.EM_ANDAMENTO)
                .mecanico("João")
                .dataInicio(now)
                .dataFinalizacao(now)
                .observacoes("obs")
                .version(1L)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(osId, entity.getOsId());
        assertEquals(orcId, entity.getOrcamentoId());
        assertEquals(StatusExecucao.EM_ANDAMENTO, entity.getStatus());
        assertEquals("João", entity.getMecanico());
        assertEquals(now, entity.getDataInicio());
        assertEquals(now, entity.getDataFinalizacao());
        assertEquals("obs", entity.getObservacoes());
        assertEquals(1L, entity.getVersion());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("ExecucaoOSEntity - no-args constructor com defaults")
    void execucaoOSEntityNoArgs() {
        ExecucaoOSEntity entity = new ExecucaoOSEntity();
        assertNull(entity.getId());
        assertNull(entity.getOsId());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, entity.getStatus());
        assertEquals(0L, entity.getVersion());
        assertNotNull(entity.getDiagnosticos());
        assertNotNull(entity.getTarefas());
        assertNotNull(entity.getPecas());
    }

    @Test
    @DisplayName("ExecucaoOSEntity - setters")
    void execucaoOSEntitySetters() {
        ExecucaoOSEntity entity = new ExecucaoOSEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setMecanico("Pedro");
        entity.setStatus(StatusExecucao.CONCLUIDA);
        entity.setObservacoes("Concluído");
        assertEquals(id, entity.getId());
        assertEquals("Pedro", entity.getMecanico());
        assertEquals(StatusExecucao.CONCLUIDA, entity.getStatus());
        assertEquals("Concluído", entity.getObservacoes());
    }

    @Test
    @DisplayName("ExecucaoOSEntity - collections defaults in builder")
    void execucaoOSEntityCollections() {
        ExecucaoOSEntity entity = ExecucaoOSEntity.builder()
                .id(UUID.randomUUID())
                .build();
        assertNotNull(entity.getDiagnosticos());
        assertNotNull(entity.getTarefas());
        assertNotNull(entity.getPecas());
    }

    // --- DiagnosticoEntity ---
    @Test
    @DisplayName("DiagnosticoEntity - builder e getters")
    void diagnosticoEntityBuilder() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ExecucaoOSEntity exec = new ExecucaoOSEntity();

        DiagnosticoEntity entity = DiagnosticoEntity.builder()
                .id(id)
                .execucaoOSEntity(exec)
                .descricao("Motor")
                .mecanico("João")
                .dataDiagnostico(now)
                .observacoes("obs")
                .createdAt(now)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(exec, entity.getExecucaoOSEntity());
        assertEquals("Motor", entity.getDescricao());
        assertEquals("João", entity.getMecanico());
        assertEquals(now, entity.getDataDiagnostico());
        assertEquals("obs", entity.getObservacoes());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    @DisplayName("DiagnosticoEntity - no-args e setters")
    void diagnosticoEntityNoArgs() {
        DiagnosticoEntity entity = new DiagnosticoEntity();
        assertNull(entity.getId());
        entity.setDescricao("Teste");
        assertEquals("Teste", entity.getDescricao());
    }

    // --- TarefaEntity ---
    @Test
    @DisplayName("TarefaEntity - builder e getters")
    void tarefaEntityBuilder() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ExecucaoOSEntity exec = new ExecucaoOSEntity();

        TarefaEntity entity = TarefaEntity.builder()
                .id(id)
                .execucaoOSEntity(exec)
                .descricao("Troca de óleo")
                .mecanico("Pedro")
                .tempoEstimadoMinutos(60)
                .tempoRealMinutos(55)
                .status(StatusTarefa.CONCLUIDA)
                .dataInicio(now)
                .dataFinalizacao(now)
                .createdAt(now)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(exec, entity.getExecucaoOSEntity());
        assertEquals("Troca de óleo", entity.getDescricao());
        assertEquals("Pedro", entity.getMecanico());
        assertEquals(60, entity.getTempoEstimadoMinutos());
        assertEquals(55, entity.getTempoRealMinutos());
        assertEquals(StatusTarefa.CONCLUIDA, entity.getStatus());
    }

    @Test
    @DisplayName("TarefaEntity - no-args com default status")
    void tarefaEntityNoArgs() {
        TarefaEntity entity = new TarefaEntity();
        assertNull(entity.getId());
        assertEquals(StatusTarefa.PENDENTE, entity.getStatus());
    }

    @Test
    @DisplayName("TarefaEntity - setters")
    void tarefaEntitySetters() {
        TarefaEntity entity = new TarefaEntity();
        entity.setDescricao("Alinhamento");
        entity.setMecanico("Ana");
        entity.setTempoEstimadoMinutos(30);
        assertEquals("Alinhamento", entity.getDescricao());
        assertEquals("Ana", entity.getMecanico());
        assertEquals(30, entity.getTempoEstimadoMinutos());
    }

    // --- UsoPecaEntity ---
    @Test
    @DisplayName("UsoPecaEntity - builder e getters")
    void usoPecaEntityBuilder() {
        UUID id = UUID.randomUUID();
        UUID pecaId = UUID.randomUUID();
        Instant now = Instant.now();
        ExecucaoOSEntity exec = new ExecucaoOSEntity();

        UsoPecaEntity entity = UsoPecaEntity.builder()
                .id(id)
                .execucaoOSEntity(exec)
                .pecaId(pecaId)
                .descricao("Filtro")
                .quantidade(2)
                .valorUnitario(new BigDecimal("25.00"))
                .valorTotal(new BigDecimal("50.00"))
                .dataUso(now)
                .createdAt(now)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(exec, entity.getExecucaoOSEntity());
        assertEquals(pecaId, entity.getPecaId());
        assertEquals("Filtro", entity.getDescricao());
        assertEquals(2, entity.getQuantidade());
        assertEquals(new BigDecimal("25.00"), entity.getValorUnitario());
        assertEquals(new BigDecimal("50.00"), entity.getValorTotal());
        assertEquals(now, entity.getDataUso());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    @DisplayName("UsoPecaEntity - no-args e setters")
    void usoPecaEntityNoArgs() {
        UsoPecaEntity entity = new UsoPecaEntity();
        assertNull(entity.getId());
        entity.setDescricao("Pastilha");
        entity.setQuantidade(4);
        assertEquals("Pastilha", entity.getDescricao());
        assertEquals(4, entity.getQuantidade());
    }

    // --- All-args constructors ---
    @Test
    @DisplayName("ExecucaoOSEntity - all-args constructor")
    void execucaoOSEntityAllArgs() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ExecucaoOSEntity entity = new ExecucaoOSEntity(
                id, UUID.randomUUID(), UUID.randomUUID(), StatusExecucao.AGUARDANDO_INICIO,
                "Mec", now, null, null, 0L,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), now, now);
        assertEquals(id, entity.getId());
    }

    @Test
    @DisplayName("DiagnosticoEntity - all-args constructor")
    void diagnosticoEntityAllArgs() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        DiagnosticoEntity entity = new DiagnosticoEntity(
                id, null, "Desc", "Mec", now, "obs", now);
        assertEquals(id, entity.getId());
    }

    @Test
    @DisplayName("TarefaEntity - all-args constructor")
    void tarefaEntityAllArgs() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        TarefaEntity entity = new TarefaEntity(
                id, null, "Desc", "Mec", 30, null, StatusTarefa.PENDENTE, now, null, now);
        assertEquals(id, entity.getId());
    }

    @Test
    @DisplayName("UsoPecaEntity - all-args constructor")
    void usoPecaEntityAllArgs() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UsoPecaEntity entity = new UsoPecaEntity(
                id, null, UUID.randomUUID(), "Desc", 1,
                new BigDecimal("10"), new BigDecimal("10"), now, now);
        assertEquals(id, entity.getId());
    }
}
