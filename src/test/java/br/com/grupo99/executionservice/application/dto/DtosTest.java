package br.com.grupo99.executionservice.application.dto;

import br.com.grupo99.executionservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTOs - Testes Unitários")
class DtosTest {

    // --- ExecucaoOSRequestDTO ---
    @Test
    @DisplayName("ExecucaoOSRequestDTO - record fields")
    void execucaoRequestDTO() {
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        ExecucaoOSRequestDTO dto = new ExecucaoOSRequestDTO(osId, orcId, "João");
        assertEquals(osId, dto.osId());
        assertEquals(orcId, dto.orcamentoId());
        assertEquals("João", dto.mecanico());
    }

    // --- ExecucaoOSResponseDTO ---
    @Test
    @DisplayName("ExecucaoOSResponseDTO - fromDomain")
    void execucaoResponseFromDomain() {
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        ExecucaoOS execucao = ExecucaoOS.criar(osId, orcId, "João");
        execucao.adicionarDiagnostico(new Diagnostico("D1", "João"));
        execucao.adicionarTarefa(new Tarefa("T1", "João", 30));
        execucao.adicionarUsoPeca(new UsoPeca(UUID.randomUUID(), 1, new BigDecimal("10.00")));

        ExecucaoOSResponseDTO dto = ExecucaoOSResponseDTO.fromDomain(execucao);

        assertEquals(execucao.getId(), dto.id());
        assertEquals(osId, dto.osId());
        assertEquals(orcId, dto.orcamentoId());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, dto.status());
        assertEquals("João", dto.mecanico());
        assertNotNull(dto.dataInicio());
        assertNull(dto.dataFinalizacao());
        assertNull(dto.observacoes());
        assertEquals(1, dto.totalDiagnosticos());
        assertEquals(1, dto.totalTarefas());
        assertEquals(1, dto.totalPecas());
        assertNotNull(dto.createdAt());
        assertNotNull(dto.updatedAt());
    }

    @Test
    @DisplayName("ExecucaoOSResponseDTO - record constructor")
    void execucaoResponseRecord() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        ExecucaoOSResponseDTO dto = new ExecucaoOSResponseDTO(
                id, UUID.randomUUID(), UUID.randomUUID(),
                StatusExecucao.EM_ANDAMENTO, "Mec", now, null, "obs",
                2, 3, 1, now, now);
        assertEquals(id, dto.id());
        assertEquals(2, dto.totalDiagnosticos());
    }

    // --- TarefaRequestDTO ---
    @Test
    @DisplayName("TarefaRequestDTO - record fields")
    void tarefaRequestDTO() {
        TarefaRequestDTO dto = new TarefaRequestDTO("Troca de óleo", "Pedro", 60);
        assertEquals("Troca de óleo", dto.descricao());
        assertEquals("Pedro", dto.mecanico());
        assertEquals(60, dto.tempoEstimadoMinutos());
    }

    // --- TarefaResponseDTO ---
    @Test
    @DisplayName("TarefaResponseDTO - fromDomain")
    void tarefaResponseFromDomain() {
        Tarefa tarefa = Tarefa.criar("Alinhamento", "Pedro", 45);
        TarefaResponseDTO dto = TarefaResponseDTO.fromDomain(tarefa);
        assertEquals(tarefa.getId(), dto.id());
        assertEquals("Alinhamento", dto.descricao());
        assertEquals("Pedro", dto.mecanico());
        assertEquals(45, dto.tempoEstimadoMinutos());
        assertNull(dto.tempoRealMinutos());
        assertEquals(StatusTarefa.PENDENTE, dto.status());
        assertNotNull(dto.dataInicio());
        assertNull(dto.dataFinalizacao());
    }

    @Test
    @DisplayName("TarefaResponseDTO - record constructor")
    void tarefaResponseRecord() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        TarefaResponseDTO dto = new TarefaResponseDTO(id, "Desc", "Mec", 30, 35,
                StatusTarefa.CONCLUIDA, now, now);
        assertEquals(id, dto.id());
        assertEquals(35, dto.tempoRealMinutos());
    }

    // --- DiagnosticoRequestDTO ---
    @Test
    @DisplayName("DiagnosticoRequestDTO - record fields")
    void diagnosticoRequestDTO() {
        DiagnosticoRequestDTO dto = new DiagnosticoRequestDTO("Motor", "Ana", "Verificar");
        assertEquals("Motor", dto.descricao());
        assertEquals("Ana", dto.mecanico());
        assertEquals("Verificar", dto.observacoes());
    }

    // --- DiagnosticoResponseDTO ---
    @Test
    @DisplayName("DiagnosticoResponseDTO - fromDomain")
    void diagnosticoResponseFromDomain() {
        Diagnostico d = Diagnostico.criar("Barulho no freio", "Pedro", "Checar pastilhas");
        DiagnosticoResponseDTO dto = DiagnosticoResponseDTO.fromDomain(d);
        assertEquals(d.getId(), dto.id());
        assertEquals("Barulho no freio", dto.descricao());
        assertEquals("Pedro", dto.mecanico());
        assertNotNull(dto.dataDiagnostico());
        assertEquals("Checar pastilhas", dto.observacoes());
    }

    @Test
    @DisplayName("DiagnosticoResponseDTO - record constructor")
    void diagnosticoResponseRecord() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        DiagnosticoResponseDTO dto = new DiagnosticoResponseDTO(id, "Desc", "Mec", now, "obs");
        assertEquals(id, dto.id());
    }

    // --- UsoPecaRequestDTO ---
    @Test
    @DisplayName("UsoPecaRequestDTO - record fields")
    void usoPecaRequestDTO() {
        UUID pecaId = UUID.randomUUID();
        UsoPecaRequestDTO dto = new UsoPecaRequestDTO(pecaId, "Filtro", 2, new BigDecimal("25.00"));
        assertEquals(pecaId, dto.pecaId());
        assertEquals("Filtro", dto.descricao());
        assertEquals(2, dto.quantidade());
        assertEquals(new BigDecimal("25.00"), dto.valorUnitario());
    }

    // --- UsoPecaResponseDTO ---
    @Test
    @DisplayName("UsoPecaResponseDTO - fromDomain")
    void usoPecaResponseFromDomain() {
        UUID pecaId = UUID.randomUUID();
        UsoPeca up = UsoPeca.criar(pecaId, "Filtro de óleo", 2, new BigDecimal("25.00"));
        UsoPecaResponseDTO dto = UsoPecaResponseDTO.fromDomain(up);
        assertEquals(up.getId(), dto.id());
        assertEquals(pecaId, dto.pecaId());
        assertEquals("Filtro de óleo", dto.descricao());
        assertEquals(2, dto.quantidade());
        assertEquals(new BigDecimal("25.00"), dto.valorUnitario());
        assertEquals(new BigDecimal("50.00"), dto.valorTotal());
        assertNotNull(dto.dataUso());
    }

    @Test
    @DisplayName("UsoPecaResponseDTO - record constructor")
    void usoPecaResponseRecord() {
        UUID id = UUID.randomUUID();
        UUID pecaId = UUID.randomUUID();
        Instant now = Instant.now();
        UsoPecaResponseDTO dto = new UsoPecaResponseDTO(id, pecaId, "Desc", 3,
                new BigDecimal("10.00"), new BigDecimal("30.00"), now);
        assertEquals(id, dto.id());
        assertEquals(3, dto.quantidade());
    }
}
