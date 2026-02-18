package br.com.grupo99.executionservice.application.mapper;

import br.com.grupo99.executionservice.application.dto.TarefaRequestDTO;
import br.com.grupo99.executionservice.application.dto.TarefaResponseDTO;
import br.com.grupo99.executionservice.domain.model.StatusTarefa;
import br.com.grupo99.executionservice.domain.model.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TarefaMapper - Testes Unitários")
class TarefaMapperTest {

    private TarefaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TarefaMapper();
    }

    @Test
    @DisplayName("toDomain deve converter request para domain")
    void toDomainConvert() {
        TarefaRequestDTO request = new TarefaRequestDTO("Troca de óleo", "Pedro", 60);
        Tarefa domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals("Troca de óleo", domain.getDescricao());
        assertEquals("Pedro", domain.getMecanico());
        assertEquals(60, domain.getTempoEstimadoMinutos());
        assertEquals(StatusTarefa.PENDENTE, domain.getStatus());
    }

    @Test
    @DisplayName("toDomain deve retornar null para request null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toResponseDTO deve converter domain para response")
    void toResponseDTOConvert() {
        Tarefa domain = Tarefa.criar("Alinhamento", "Ana", 45);
        TarefaResponseDTO dto = mapper.toResponseDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.id());
        assertEquals("Alinhamento", dto.descricao());
        assertEquals("Ana", dto.mecanico());
        assertEquals(45, dto.tempoEstimadoMinutos());
    }

    @Test
    @DisplayName("toResponseDTO deve retornar null para domain null")
    void toResponseDTONull() {
        assertNull(mapper.toResponseDTO(null));
    }
}
