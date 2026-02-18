package br.com.grupo99.executionservice.infrastructure.persistence.adapter;

import br.com.grupo99.executionservice.domain.model.ExecucaoOS;
import br.com.grupo99.executionservice.domain.model.StatusExecucao;
import br.com.grupo99.executionservice.infrastructure.persistence.entity.ExecucaoOSEntity;
import br.com.grupo99.executionservice.infrastructure.persistence.mapper.ExecucaoOSEntityMapper;
import br.com.grupo99.executionservice.infrastructure.persistence.repository.PostgresExecucaoOSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExecucaoOSRepositoryAdapter - Testes Unitários")
class ExecucaoOSRepositoryAdapterTest {

    @Mock
    private PostgresExecucaoOSRepository postgresRepository;

    @Mock
    private ExecucaoOSEntityMapper mapper;

    @InjectMocks
    private ExecucaoOSRepositoryAdapter adapter;

    private ExecucaoOS domain;
    private ExecucaoOSEntity entity;
    private UUID id;
    private UUID osId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        osId = UUID.randomUUID();
        Instant now = Instant.now();

        domain = ExecucaoOS.builder()
                .id(id)
                .osId(osId)
                .orcamentoId(UUID.randomUUID())
                .status(StatusExecucao.AGUARDANDO_INICIO)
                .mecanico("João")
                .dataInicio(now)
                .diagnosticos(new ArrayList<>())
                .tarefas(new ArrayList<>())
                .pecas(new ArrayList<>())
                .version(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();

        entity = ExecucaoOSEntity.builder()
                .id(id)
                .osId(osId)
                .orcamentoId(domain.getOrcamentoId())
                .status(StatusExecucao.AGUARDANDO_INICIO)
                .mecanico("João")
                .dataInicio(now)
                .diagnosticos(new ArrayList<>())
                .tarefas(new ArrayList<>())
                .pecas(new ArrayList<>())
                .version(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    @DisplayName("save deve converter, salvar e retornar domain")
    void saveShouldWork() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(postgresRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ExecucaoOS result = adapter.save(domain);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(mapper).toEntity(domain);
        verify(postgresRepository).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById deve retornar Optional com domain quando encontrado")
    void findByIdFound() {
        when(postgresRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<ExecucaoOS> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postgresRepository).findById(id);
    }

    @Test
    @DisplayName("findById deve retornar Optional vazio quando não encontrado")
    void findByIdNotFound() {
        when(postgresRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ExecucaoOS> result = adapter.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByOsId deve retornar Optional com domain quando encontrado")
    void findByOsIdFound() {
        when(postgresRepository.findByOsId(osId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<ExecucaoOS> result = adapter.findByOsId(osId);

        assertTrue(result.isPresent());
        assertEquals(osId, result.get().getOsId());
    }

    @Test
    @DisplayName("findByOsId deve retornar Optional vazio quando não encontrado")
    void findByOsIdNotFound() {
        when(postgresRepository.findByOsId(osId)).thenReturn(Optional.empty());

        Optional<ExecucaoOS> result = adapter.findByOsId(osId);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByStatus deve retornar lista de domains")
    void findByStatus() {
        when(postgresRepository.findByStatus("AGUARDANDO_INICIO")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ExecucaoOS> result = adapter.findByStatus(StatusExecucao.AGUARDANDO_INICIO);

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    @DisplayName("findByStatus deve retornar lista vazia quando não há resultados")
    void findByStatusEmpty() {
        when(postgresRepository.findByStatus("EM_ANDAMENTO")).thenReturn(List.of());

        List<ExecucaoOS> result = adapter.findByStatus(StatusExecucao.EM_ANDAMENTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByMecanico deve retornar lista de domains")
    void findByMecanico() {
        when(postgresRepository.findByMecanico("João")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ExecucaoOS> result = adapter.findByMecanico("João");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByMecanico deve retornar lista vazia")
    void findByMecanicoEmpty() {
        when(postgresRepository.findByMecanico("Inexistente")).thenReturn(List.of());

        List<ExecucaoOS> result = adapter.findByMecanico("Inexistente");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll deve retornar lista de todos os domains")
    void findAll() {
        when(postgresRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ExecucaoOS> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia")
    void findAllEmpty() {
        when(postgresRepository.findAll()).thenReturn(List.of());

        List<ExecucaoOS> result = adapter.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deleteById deve delegar para repository")
    void deleteById() {
        doNothing().when(postgresRepository).deleteById(id);

        adapter.deleteById(id);

        verify(postgresRepository).deleteById(id);
    }

    @Test
    @DisplayName("existsByOsId deve retornar true quando existe")
    void existsByOsIdTrue() {
        when(postgresRepository.existsByOsId(osId)).thenReturn(true);

        assertTrue(adapter.existsByOsId(osId));
    }

    @Test
    @DisplayName("existsByOsId deve retornar false quando não existe")
    void existsByOsIdFalse() {
        when(postgresRepository.existsByOsId(osId)).thenReturn(false);

        assertFalse(adapter.existsByOsId(osId));
    }

    @Test
    @DisplayName("existsById deve retornar true quando existe")
    void existsByIdTrue() {
        when(postgresRepository.existsById(id)).thenReturn(true);

        assertTrue(adapter.existsById(id));
    }

    @Test
    @DisplayName("existsById deve retornar false quando não existe")
    void existsByIdFalse() {
        when(postgresRepository.existsById(id)).thenReturn(false);

        assertFalse(adapter.existsById(id));
    }
}
