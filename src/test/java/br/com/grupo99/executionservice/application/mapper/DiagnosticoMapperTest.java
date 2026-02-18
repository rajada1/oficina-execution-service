package br.com.grupo99.executionservice.application.mapper;

import br.com.grupo99.executionservice.application.dto.DiagnosticoRequestDTO;
import br.com.grupo99.executionservice.application.dto.DiagnosticoResponseDTO;
import br.com.grupo99.executionservice.domain.model.Diagnostico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiagnosticoMapper - Testes Unitários")
class DiagnosticoMapperTest {

    private DiagnosticoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiagnosticoMapper();
    }

    @Test
    @DisplayName("toDomain deve converter request para domain")
    void toDomainConvert() {
        DiagnosticoRequestDTO request = new DiagnosticoRequestDTO("Motor falhando", "João", "Verificar cilindros");
        Diagnostico domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals("Motor falhando", domain.getDescricao());
        assertEquals("João", domain.getMecanico());
        assertEquals("Verificar cilindros", domain.getObservacoes());
    }

    @Test
    @DisplayName("toDomain deve retornar null para request null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toResponseDTO deve converter domain para response")
    void toResponseDTOConvert() {
        Diagnostico domain = Diagnostico.criar("Barulho", "Pedro", "Checar freios");
        DiagnosticoResponseDTO dto = mapper.toResponseDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.id());
        assertEquals("Barulho", dto.descricao());
        assertEquals("Pedro", dto.mecanico());
        assertEquals("Checar freios", dto.observacoes());
    }

    @Test
    @DisplayName("toResponseDTO deve retornar null para domain null")
    void toResponseDTONull() {
        assertNull(mapper.toResponseDTO(null));
    }
}
