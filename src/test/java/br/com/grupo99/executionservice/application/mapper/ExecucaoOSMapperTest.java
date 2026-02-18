package br.com.grupo99.executionservice.application.mapper;

import br.com.grupo99.executionservice.application.dto.ExecucaoOSRequestDTO;
import br.com.grupo99.executionservice.application.dto.ExecucaoOSResponseDTO;
import br.com.grupo99.executionservice.domain.model.ExecucaoOS;
import br.com.grupo99.executionservice.domain.model.StatusExecucao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExecucaoOSMapper - Testes Unitários")
class ExecucaoOSMapperTest {

    private ExecucaoOSMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ExecucaoOSMapper();
    }

    @Test
    @DisplayName("toDomain deve converter request para domain")
    void toDomainConvert() {
        UUID osId = UUID.randomUUID();
        UUID orcId = UUID.randomUUID();
        ExecucaoOSRequestDTO request = new ExecucaoOSRequestDTO(osId, orcId, "João");

        ExecucaoOS domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals(osId, domain.getOsId());
        assertEquals(orcId, domain.getOrcamentoId());
        assertEquals("João", domain.getMecanico());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, domain.getStatus());
    }

    @Test
    @DisplayName("toDomain deve retornar null para request null")
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toResponseDTO deve converter domain para response")
    void toResponseDTOConvert() {
        ExecucaoOS domain = ExecucaoOS.criar(UUID.randomUUID(), UUID.randomUUID(), "Pedro");

        ExecucaoOSResponseDTO dto = mapper.toResponseDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.id());
        assertEquals(domain.getOsId(), dto.osId());
        assertEquals("Pedro", dto.mecanico());
        assertEquals(StatusExecucao.AGUARDANDO_INICIO, dto.status());
    }

    @Test
    @DisplayName("toResponseDTO deve retornar null para domain null")
    void toResponseDTONull() {
        assertNull(mapper.toResponseDTO(null));
    }
}
