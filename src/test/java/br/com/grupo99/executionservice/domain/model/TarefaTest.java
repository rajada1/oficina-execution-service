package br.com.grupo99.executionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tarefa - Testes Unitários")
class TarefaTest {

    @Test
    @DisplayName("Deve criar tarefa com construtor de 3 args e status PENDENTE")
    void deveCriarTarefaComStatusPendente() {
        Tarefa tarefa = new Tarefa("Troca de óleo", "Mecânico João", 60);
        assertNotNull(tarefa);
        assertNotNull(tarefa.getId());
        assertEquals("Troca de óleo", tarefa.getDescricao());
        assertEquals("Mecânico João", tarefa.getMecanico());
        assertEquals(60, tarefa.getTempoEstimadoMinutos());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
        assertNull(tarefa.getTempoRealMinutos());
        assertNotNull(tarefa.getDataInicio());
        assertNotNull(tarefa.getCreatedAt());
    }

    @Test
    @DisplayName("Deve criar tarefa com factory method")
    void deveCriarTarefaComFactory() {
        Tarefa tarefa = Tarefa.criar("Alinhamento", "Pedro", 45);
        assertNotNull(tarefa.getId());
        assertEquals("Alinhamento", tarefa.getDescricao());
        assertEquals("Pedro", tarefa.getMecanico());
        assertEquals(45, tarefa.getTempoEstimadoMinutos());
        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
    }

    @Test
    @DisplayName("Deve criar tarefa com tempoEstimado null")
    void deveCriarTarefaComTempoEstimadoNull() {
        Tarefa tarefa = Tarefa.criar("Teste", "Mec", null);
        assertNotNull(tarefa);
        assertNull(tarefa.getTempoEstimadoMinutos());
    }

    @Test
    @DisplayName("Deve iniciar tarefa")
    void deveIniciarTarefa() {
        Tarefa tarefa = new Tarefa("Alinhamento", "Mecânico Pedro", 45);
        tarefa.iniciar();
        assertEquals(StatusTarefa.EM_ANDAMENTO, tarefa.getStatus());
        assertTrue(tarefa.isEmAndamento());
        assertFalse(tarefa.isPendente());
        assertFalse(tarefa.isConcluida());
    }

    @Test
    @DisplayName("Deve finalizar tarefa com tempo real")
    void deveFinalizarTarefaComTempoReal() {
        Tarefa tarefa = new Tarefa("Balanceamento", "Mecânico Ana", 30);
        tarefa.iniciar();
        tarefa.finalizar(35);
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
        assertEquals(35, tarefa.getTempoRealMinutos());
        assertNotNull(tarefa.getDataFinalizacao());
        assertTrue(tarefa.isConcluida());
    }

    @Test
    @DisplayName("Deve finalizar tarefa sem tempo real (usa estimado)")
    void deveFinalizarTarefaSemTempoReal() {
        Tarefa tarefa = new Tarefa("Revisão", "Mec", 60);
        tarefa.iniciar();
        tarefa.finalizar();
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
        assertEquals(60, tarefa.getTempoRealMinutos());
    }

    @Test
    @DisplayName("Deve finalizar tarefa com tempo real null")
    void deveFinalizarTarefaComTempoRealNull() {
        Tarefa tarefa = new Tarefa("Teste", "Mec", 30);
        tarefa.iniciar();
        tarefa.finalizar(null);
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
        assertNull(tarefa.getTempoRealMinutos());
    }

    @Test
    @DisplayName("Deve cancelar tarefa")
    void deveCancelarTarefa() {
        Tarefa tarefa = new Tarefa("Revisão", "Mecânico Carlos", 90);
        tarefa.cancelar();
        assertEquals(StatusTarefa.CANCELADA, tarefa.getStatus());
    }

    @Test
    @DisplayName("Não deve iniciar tarefa já iniciada")
    void naoDeveIniciarTarefaJaIniciada() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        tarefa.iniciar();
        assertThrows(IllegalStateException.class, () -> tarefa.iniciar());
    }

    @Test
    @DisplayName("Não deve iniciar tarefa já concluída")
    void naoDeveIniciarTarefaJaConcluida() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        tarefa.iniciar();
        tarefa.finalizar(30);
        assertThrows(IllegalStateException.class, () -> tarefa.iniciar());
    }

    @Test
    @DisplayName("Não deve iniciar tarefa cancelada")
    void naoDeveIniciarTarefaCancelada() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        tarefa.cancelar();
        assertThrows(IllegalStateException.class, () -> tarefa.iniciar());
    }

    @Test
    @DisplayName("Não deve finalizar tarefa não iniciada")
    void naoDeveFinalizarTarefaNaoIniciada() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        assertThrows(IllegalStateException.class, () -> tarefa.finalizar(30));
    }

    @Test
    @DisplayName("Não deve finalizar tarefa com tempo real negativo")
    void naoDeveFinalizarTarefaComTempoRealNegativo() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        tarefa.iniciar();
        assertThrows(IllegalArgumentException.class, () -> tarefa.finalizar(-10));
    }

    @Test
    @DisplayName("Não deve finalizar tarefa com tempo real zero")
    void naoDeveFinalizarTarefaComTempoRealZero() {
        Tarefa tarefa = new Tarefa("Teste", "Mecânico", 30);
        tarefa.iniciar();
        assertThrows(IllegalArgumentException.class, () -> tarefa.finalizar(0));
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios no construtor")
    void deveValidarCamposObrigatoriosConstrutor() {
        assertThrows(IllegalArgumentException.class, () -> new Tarefa(null, "Mec", 30));
        assertThrows(IllegalArgumentException.class, () -> new Tarefa("Desc", null, 30));
        assertThrows(IllegalArgumentException.class, () -> new Tarefa("", "Mec", 30));
        assertThrows(IllegalArgumentException.class, () -> new Tarefa("Desc", "", 30));
    }

    @Test
    @DisplayName("Deve validar tempo estimado no construtor")
    void deveValidarTempoEstimadoConstrutor() {
        assertThrows(IllegalArgumentException.class, () -> new Tarefa("Desc", "Mec", 0));
        assertThrows(IllegalArgumentException.class, () -> new Tarefa("Desc", "Mec", -5));
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios no factory method")
    void deveValidarCamposObrigatoriosFactory() {
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar(null, "Mec", 30));
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar("Desc", null, 30));
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar("", "Mec", 30));
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar("Desc", "", 30));
    }

    @Test
    @DisplayName("Deve validar tempo estimado no factory method")
    void deveValidarTempoEstimadoFactory() {
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar("Desc", "Mec", 0));
        assertThrows(IllegalArgumentException.class, () -> Tarefa.criar("Desc", "Mec", -5));
    }

    @Test
    @DisplayName("isPendente retorna true para tarefa nova")
    void isPendenteTrue() {
        Tarefa tarefa = new Tarefa("Teste", "Mec", 30);
        assertTrue(tarefa.isPendente());
    }

    @Test
    @DisplayName("Equals deve comparar por ID")
    void equalsPorId() {
        Tarefa t1 = Tarefa.criar("A", "M", 10);
        Tarefa t2 = Tarefa.criar("A", "M", 10);
        assertNotEquals(t1, t2);

        Tarefa t3 = Tarefa.builder().id(t1.getId()).build();
        assertEquals(t1, t3);
    }

    @Test
    @DisplayName("Equals com null e outro tipo")
    void equalsComNullEOutroTipo() {
        Tarefa t = Tarefa.criar("A", "M", 10);
        assertNotEquals(null, t);
        assertNotEquals("string", t);
        assertEquals(t, t);
    }

    @Test
    @DisplayName("HashCode baseado no ID")
    void hashCodePorId() {
        Tarefa t1 = Tarefa.criar("A", "M", 10);
        Tarefa t2 = Tarefa.builder().id(t1.getId()).build();
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("ToString contém informações relevantes")
    void toStringContemInfo() {
        Tarefa t = Tarefa.criar("Troca de óleo", "João", 60);
        String s = t.toString();
        assertTrue(s.contains("Troca de óleo"));
        assertTrue(s.contains("Tarefa"));
    }

    @Test
    @DisplayName("Construtor no-args funciona")
    void construtorNoArgs() {
        Tarefa t = new Tarefa();
        assertNull(t.getId());
    }

    @Test
    @DisplayName("Builder funciona")
    void builderFunciona() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Tarefa t = Tarefa.builder()
                .id(id)
                .descricao("Desc")
                .mecanico("Mec")
                .tempoEstimadoMinutos(30)
                .status(StatusTarefa.PENDENTE)
                .dataInicio(now)
                .createdAt(now)
                .build();
        assertEquals(id, t.getId());
        assertEquals("Desc", t.getDescricao());
    }
}
