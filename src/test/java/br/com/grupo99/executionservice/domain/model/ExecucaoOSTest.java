package br.com.grupo99.executionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExecucaoOS - Testes Unitários")
class ExecucaoOSTest {

    @Test
    @DisplayName("Deve criar execução com 2 args e status AGUARDANDO_INICIO")
    void deveCriarExecucaoComStatusAguardandoInicio() {
        UUID osId = UUID.randomUUID();
        ExecucaoOS execucao = ExecucaoOS.criar(osId, "Mecânico João");

        assertNotNull(execucao);
        assertNotNull(execucao.getId());
        assertEquals(osId, execucao.getOsId());
        assertEquals("Mecânico João", execucao.getMecanico());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, execucao.getStatus());
        assertNotNull(execucao.getDataCriacao());
        assertNotNull(execucao.getDataInicio());
        assertNull(execucao.getDataConclusao());
        assertTrue(execucao.getDiagnosticos().isEmpty());
        assertTrue(execucao.getTarefas().isEmpty());
        assertTrue(execucao.getPecas().isEmpty());
        assertEquals(0L, execucao.getVersion());
    }

    @Test
    @DisplayName("Deve criar execução com 3 args (osId, orcamentoId, mecanico)")
    void deveCriarExecucaoCom3Args() {
        UUID osId = UUID.randomUUID();
        UUID orcamentoId = UUID.randomUUID();
        ExecucaoOS execucao = ExecucaoOS.criar(osId, orcamentoId, "Mecânico João");

        assertNotNull(execucao);
        assertEquals(osId, execucao.getOsId());
        assertEquals(orcamentoId, execucao.getOrcamentoId());
        assertEquals("Mecânico João", execucao.getMecanico());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, execucao.getStatus());
    }

    @Test
    @DisplayName("Deve iniciar execução")
    void deveIniciarExecucao() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico José");
        execucao.iniciar();
        assertEquals(StatusExecucao.EM_ANDAMENTO, execucao.getStatus());
        assertTrue(execucao.isEmAndamento());
        assertFalse(execucao.isConcluida());
    }

    @Test
    @DisplayName("Deve finalizar execução com observações")
    void deveFinalizarExecucaoComObservacoes() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Pedro");
        execucao.iniciar();
        execucao.finalizar("Tudo OK");

        assertEquals(StatusExecucao.CONCLUIDA, execucao.getStatus());
        assertNotNull(execucao.getDataConclusao());
        assertEquals("Tudo OK", execucao.getObservacoes());
        assertTrue(execucao.isConcluida());
        assertFalse(execucao.isEmAndamento());
    }

    @Test
    @DisplayName("Deve finalizar execução sem observações (overload)")
    void deveFinalizarExecucaoSemObservacoes() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Pedro");
        execucao.iniciar();
        execucao.finalizar();
        assertEquals(StatusExecucao.CONCLUIDA, execucao.getStatus());
        assertEquals("Execução finalizada", execucao.getObservacoes());
    }

    @Test
    @DisplayName("Deve cancelar execução com motivo")
    void deveCancelarExecucaoComMotivo() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Ana");
        execucao.cancelar("Peça indisponível");
        assertEquals(StatusExecucao.CANCELADA, execucao.getStatus());
        assertEquals("Peça indisponível", execucao.getObservacoes());
    }

    @Test
    @DisplayName("Deve cancelar execução sem motivo (overload)")
    void deveCancelarExecucaoSemMotivo() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Ana");
        execucao.cancelar();
        assertEquals(StatusExecucao.CANCELADA, execucao.getStatus());
        assertEquals("Execução cancelada", execucao.getObservacoes());
    }

    @Test
    @DisplayName("Deve cancelar execução em andamento")
    void deveCancelarExecucaoEmAndamento() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        execucao.iniciar();
        execucao.cancelar("Problema");
        assertEquals(StatusExecucao.CANCELADA, execucao.getStatus());
    }

    @Test
    @DisplayName("Não deve iniciar execução já iniciada")
    void naoDeveIniciarExecucaoJaIniciada() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Carlos");
        execucao.iniciar();
        assertThrows(IllegalStateException.class, () -> execucao.iniciar());
    }

    @Test
    @DisplayName("Não deve finalizar execução não iniciada")
    void naoDeveFinalizarExecucaoNaoIniciada() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Maria");
        assertThrows(IllegalStateException.class, () -> execucao.finalizar());
    }

    @Test
    @DisplayName("Não deve iniciar execução concluída")
    void naoDeveIniciarExecucaoConcluida() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        execucao.iniciar();
        execucao.finalizar();
        assertThrows(IllegalStateException.class, () -> execucao.iniciar());
    }

    @Test
    @DisplayName("Não deve cancelar execução já cancelada")
    void naoDeveCancelarExecucaoJaCancelada() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        execucao.cancelar();
        assertThrows(IllegalStateException.class, () -> execucao.cancelar());
    }

    @Test
    @DisplayName("Não deve cancelar execução concluída")
    void naoDeveCancelarExecucaoConcluida() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        execucao.iniciar();
        execucao.finalizar();
        assertThrows(IllegalStateException.class, () -> execucao.cancelar());
    }

    @Test
    @DisplayName("Deve adicionar diagnóstico")
    void deveAdicionarDiagnostico() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Luis");
        Diagnostico diagnostico = new Diagnostico("Problema no motor", "Mecânico Luis");
        execucao.adicionarDiagnostico(diagnostico);
        assertEquals(1, execucao.getDiagnosticos().size());
        assertEquals("Problema no motor", execucao.getDiagnosticos().get(0).getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar diagnóstico null")
    void excecaoDiagnosticoNull() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        assertThrows(NullPointerException.class, () -> execucao.adicionarDiagnostico(null));
    }

    @Test
    @DisplayName("Deve adicionar tarefa")
    void deveAdicionarTarefa() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Sofia");
        Tarefa tarefa = new Tarefa("Troca de óleo", "Mecânico Sofia", 60);
        execucao.adicionarTarefa(tarefa);
        assertEquals(1, execucao.getTarefas().size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar tarefa null")
    void excecaoTarefaNull() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        assertThrows(NullPointerException.class, () -> execucao.adicionarTarefa(null));
    }

    @Test
    @DisplayName("Deve adicionar uso de peça")
    void deveAdicionarUsoPeca() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico Roberto");
        UsoPeca usoPeca = new UsoPeca(UUID.randomUUID(), 2, new BigDecimal("35.50"));
        execucao.adicionarUsoPeca(usoPeca);
        assertEquals(1, execucao.getPecas().size());
        assertEquals(new BigDecimal("71.00"), execucao.getPecas().get(0).getValorTotal());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar uso de peça null")
    void excecaoUsoPecaNull() {
        ExecucaoOS execucao = ExecucaoOS.criar(UUID.randomUUID(), "Mecânico");
        assertThrows(NullPointerException.class, () -> execucao.adicionarUsoPeca(null));
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios na criação (2 args)")
    void deveValidarCamposObrigatoriosNaCriacao2Args() {
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(null, "Mecânico"));
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(UUID.randomUUID(), (String) null));
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(UUID.randomUUID(), ""));
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios na criação (3 args)")
    void deveValidarCamposObrigatoriosNaCriacao3Args() {
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(null, orcId, "Mecânico"));
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(osId, null, "Mecânico"));
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(osId, orcId, null));
        assertThrows(IllegalArgumentException.class, () -> ExecucaoOS.criar(osId, orcId, ""));
    }

    @Test
    @DisplayName("Equals deve comparar por ID")
    void equalsPorId() {
        ExecucaoOS e1 = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        ExecucaoOS e2 = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        assertNotEquals(e1, e2);

        ExecucaoOS e3 = ExecucaoOS.builder().id(e1.getId()).build();
        assertEquals(e1, e3);
    }

    @Test
    @DisplayName("Equals com null e outro tipo")
    void equalsComNullEOutroTipo() {
        ExecucaoOS e = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        assertNotEquals(null, e);
        assertNotEquals("string", e);
        assertEquals(e, e);
    }

    @Test
    @DisplayName("HashCode baseado no ID")
    void hashCodePorId() {
        ExecucaoOS e1 = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        ExecucaoOS e2 = ExecucaoOS.builder().id(e1.getId()).build();
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    @DisplayName("ToString contém informações relevantes")
    void toStringContemInfo() {
        ExecucaoOS e = ExecucaoOS.criar(UUID.randomUUID(), "João");
        String s = e.toString();
        assertTrue(s.contains("João"));
        assertTrue(s.contains("ExecucaoOS"));
        assertTrue(s.contains("AGUARDANDO_INICIO"));
    }

    @Test
    @DisplayName("Construtor no-args e all-args funcionam")
    void construtorNoArgsEAllArgs() {
        ExecucaoOS e = new ExecucaoOS();
        assertNull(e.getId());

        ExecucaoOS e2 = ExecucaoOS.builder()
                .id(UUID.randomUUID())
                .osId(UUID.randomUUID())
                .mecanico("Mec")
                .status(StatusExecucao.AGUARDANDO_INICIO)
                .build();
        assertNotNull(e2.getId());
    }

    @Test
    @DisplayName("isEmAndamento e isConcluida retornam false para AGUARDANDO_INICIO")
    void isEmAndamentoEConcluidaFalseParaAguardando() {
        ExecucaoOS e = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        assertFalse(e.isEmAndamento());
        assertFalse(e.isConcluida());
    }

    @Test
    @DisplayName("getDataCriacao e getDataConclusao")
    void getDataCriacaoEConclusao() {
        ExecucaoOS e = ExecucaoOS.criar(UUID.randomUUID(), "Mec");
        assertNotNull(e.getDataCriacao());
        assertNull(e.getDataConclusao());
        
        e.iniciar();
        e.finalizar("ok");
        assertNotNull(e.getDataConclusao());
    }
}
