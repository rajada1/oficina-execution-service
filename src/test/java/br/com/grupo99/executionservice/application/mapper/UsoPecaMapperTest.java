package br.com.grupo99.executionservice.application.mapper;

import br.com.grupo99.executionservice.application.dto.UsoPecaRequestDTO;
import br.com.grupo99.executionservice.application.dto.UsoPecaResponseDTO;
import br.com.grupo99.executionservice.domain.model.UsoPeca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsoPecaMapper - Testes Unitários")
class UsoPecaMapperTest {

    private UsoPecaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UsoPecaMapper();
    }

    @Test
    @DisplayName("toDomain deve converter request para domain")
    void toDomainConvert() {
        UUID pecaId = UUID.randomUUID();
        UsoPecaRequestDTO request = new UsoPecaRequestDTO(pecaId, "Filtro", 2, new BigDecimal("25.00"));
        UsoPeca domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals(pecaId, domain.getPecaId());
        assertEquals("Filtro", domain.getDescricao());
        assertEquals(2, domain.getQuantidade());
        assertEquals(new BigDecimal("25.00"), domain.getValorUnitario());
        assertEquals(new BigDecimal("50.00"), domain.getValorTotal());
    }

    @Test
    @DisplayName("toDomain deve retornar null para request null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toResponseDTO deve converter domain para response")
    void toResponseDTOConvert() {
        UUID pecaId = UUID.randomUUID();
        UsoPeca domain = UsoPeca.criar(pecaId, "Pastilha de freio", 4, new BigDecimal("50.00"));
        UsoPecaResponseDTO dto = mapper.toResponseDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.id());
        assertEquals(pecaId, dto.pecaId());
        assertEquals("Pastilha de freio", dto.descricao());
        assertEquals(4, dto.quantidade());
        assertEquals(new BigDecimal("50.00"), dto.valorUnitario());
        assertEquals(new BigDecimal("200.00"), dto.valorTotal());
    }

    @Test
    @DisplayName("toResponseDTO deve retornar null para domain null")
    void toResponseDTONull() {
        assertNull(mapper.toResponseDTO(null));
    }
}
