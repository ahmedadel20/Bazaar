package org.bazaar.productCatalogue.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bazaar.productCatalogue.client.ClientException;
import org.bazaar.productCatalogue.client.InventoryClient;
import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleEvent;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleMapper;
import org.bazaar.productCatalogue.sale.dto.SaleProduct;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.dto.SaleUpdateRequest;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.exception.SaleException;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.bazaar.productCatalogue.sale.service.SaleService;
import org.bazaar.productCatalogue.sale.service.SaleServiceImpl;
import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;
import org.bazaar.productCatalogue.saleStatus.service.SaleStatusService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.StateMachineEventResult.ResultType;
import org.springframework.statemachine.access.StateMachineAccessor;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import feign.FeignException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
public class SaleServiceTest {
    @TestConfiguration
    static class ServiceTestConfig {
        @Bean
        SaleService service(SaleRepo repo,
                SaleStatusService saleStatusService,
                SaleMapper mapper,
                InventoryClient inventoryClient,
                StateMachineFactory<SaleStatusEnum, SaleEvent> stateMachineFactory) {
            return new SaleServiceImpl(repo, saleStatusService, mapper, inventoryClient, stateMachineFactory);
        }
    }

    @MockitoBean
    private SaleRepo repo;

    @MockitoBean
    private SaleStatusService saleStatusService;

    @MockitoBean
    private SaleMapper mapper;

    @MockitoBean
    private InventoryClient inventoryClient;

    @MockitoBean
    private StateMachineFactory<SaleStatusEnum, SaleEvent> stateMachineFactory;

    @MockitoBean
    private StateMachine<SaleStatusEnum, SaleEvent> stateMachine;

    @Autowired
    private SaleService saleService;

    private static Date startDate;
    private static Date endDate;
    private static SaleStatus activeSaleStatus;
    private static SaleStatus inactiveSaleStatus;
    private static List<Long> categoryIds;

    private static List<SaleProduct> saleProducts;

    private static Sale createdSale;
    private static Sale updatedSale;
    private static SaleCreateRequest saleCreateRequest;
    private static SaleUpdateRequest saleUpdateRequest;
    private static SaleResponse saleResponse;
    private static SaleResponse updatedSaleResponse;

    private static List<Sale> sales;
    private static List<SaleResponse> saleResponses;

    Message<SaleEvent> activateMessage = new GenericMessage<SaleEvent>(SaleEvent.ACTIVATE);
    Message<SaleEvent> deactivatMessage = new GenericMessage<SaleEvent>(SaleEvent.DEACTIVATE);

    @SuppressWarnings("unchecked")
    StateMachineEventResult<SaleStatusEnum, SaleEvent> mockResult = Mockito.mock(StateMachineEventResult.class);

    @SuppressWarnings("unchecked")
    State<SaleStatusEnum, SaleEvent> state = Mockito.mock(State.class);

    @BeforeAll
    public static void setUp() {
        startDate = Date.valueOf("2026-01-01");
        endDate = Date.valueOf("2026-2-01");

        categoryIds = new ArrayList<>(List.of(1L, 4L, 11L));

        activeSaleStatus = new SaleStatus(1, SaleStatusEnum.ACTIVE);
        inactiveSaleStatus = new SaleStatus(2, SaleStatusEnum.INACTIVE);

        saleProducts = new ArrayList<>();
        saleProducts.add(new SaleProduct(null, null, null));

        saleCreateRequest = new SaleCreateRequest("A sale", 0.5f,
                startDate, endDate,
                categoryIds);

        createdSale = new Sale(1L, saleCreateRequest.name(),
                0.5f,
                startDate, endDate, inactiveSaleStatus, null,
                categoryIds);

        saleResponse = new SaleResponse(createdSale.getId(),
                saleCreateRequest.name(), saleCreateRequest.discountPercentage(),
                startDate, endDate,
                inactiveSaleStatus, categoryIds, null);

        saleUpdateRequest = new SaleUpdateRequest(createdSale.getId(), "A sale 2: Electric Boogaloo",
                0.7f,
                startDate, endDate,
                categoryIds);

        updatedSale = new Sale(createdSale.getId(), saleUpdateRequest.name(),
                saleUpdateRequest.discountPercentage(),
                startDate, endDate, inactiveSaleStatus, new ArrayList<>(),
                categoryIds);

        updatedSaleResponse = new SaleResponse(createdSale.getId(), updatedSale.getName(),
                updatedSale.getDiscountPercentage(),
                startDate, endDate,
                inactiveSaleStatus, categoryIds, saleProducts);

        sales = new ArrayList<>(List.of(createdSale, updatedSale));

        saleResponses = new ArrayList<>(List.of(saleResponse, updatedSaleResponse));
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUpForEach() {
        createdSale.setStatus(inactiveSaleStatus);
        updatedSale.setStatus(inactiveSaleStatus);

        createdSale.setStartDate(startDate);
        createdSale.setEndDate(endDate);
        updatedSale.setStartDate(startDate);
        updatedSale.setEndDate(endDate);

        // SaleRepo
        when(repo.findById(createdSale.getId())).thenReturn(Optional.of(createdSale));
        when(repo.findById((-1L))).thenReturn(Optional.empty());

        when(repo.findByStartDate(any())).thenReturn(sales);
        when(repo.findByEndDate(any())).thenReturn(sales);

        when(repo.findAll()).thenReturn(sales);

        when(repo.findByCategoryIdsAndOverlappingDates(categoryIds, startDate, endDate)).thenReturn(Optional.empty());

        when(repo.findByName(createdSale.getName())).thenReturn(Optional.empty());
        when(repo.findByName(updatedSale.getName())).thenReturn(Optional.empty());

        when(repo.save(createdSale)).thenReturn(createdSale);
        when(repo.save(updatedSale)).thenReturn(updatedSale);

        // Mapper
        when(mapper.toSale(saleCreateRequest)).thenReturn(createdSale);
        when(mapper.toSale(saleUpdateRequest, createdSale)).thenReturn(updatedSale);

        when(mapper.toSaleResponse(eq(createdSale), any())).thenReturn(saleResponse);
        when(mapper.toSaleResponse(eq(updatedSale), any())).thenReturn(updatedSaleResponse);

        // Inventory Client
        when(inventoryClient.getProductsByCategories(anyList())).thenReturn(new ArrayList<>());

        // State Machine
        when(stateMachineFactory.getStateMachine()).thenReturn(stateMachine);
        when(stateMachine.getState()).thenReturn(state);
        when(stateMachine.startReactively()).thenReturn(Mono.empty());
        when(stateMachine.getStateMachineAccessor()).thenReturn(mock(StateMachineAccessor.class));
        when(stateMachine.getExtendedState()).thenReturn(mock(ExtendedState.class));
        when(stateMachine.sendEvent(any(Mono.class))).thenReturn(Flux.just(mockResult));

        // Sale Status Service
        when(saleStatusService.getSaleStatusFromStatus(SaleStatusEnum.ACTIVE)).thenReturn(activeSaleStatus);
        when(saleStatusService.getSaleStatusFromStatus(SaleStatusEnum.INACTIVE)).thenReturn(inactiveSaleStatus);
    }

    @Test
    public void createSale_Successful() {
        assertEquals(saleResponse, saleService.createSale(saleCreateRequest));
    }

    @Test
    public void createSale_OverlappingSale() {
        when(repo.findByCategoryIdsAndOverlappingDates(categoryIds, startDate, endDate))
                .thenReturn(Optional.of(createdSale));

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.createSale(saleCreateRequest);
        });

        assertEquals(ErrorMessage.OVERLAPPING_SALE, ex.getMessage());
    }

    @Test
    public void createSale_InvalidDates() {
        createdSale.setStartDate(endDate);
        createdSale.setEndDate(startDate);

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.createSale(saleCreateRequest);
        });

        assertEquals(ErrorMessage.START_DATE_CANNOT_BE_AFTER_END_DATE, ex.getMessage());

    }

    @Test
    public void createSale_DuplicateName() {
        when(repo.findByName(createdSale.getName())).thenReturn(Optional.of(createdSale));

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.createSale(saleCreateRequest);
        });

        assertEquals(ErrorMessage.DUPLICATE_SALE_NAME, ex.getMessage());
    }

    @Test
    public void createSale_ClientError_FeignException() {
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(FeignException.class);

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.createSale(saleCreateRequest);
                });
        assertEquals(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR, ex.getMessage());
    }

    @Test
    public void createSale_ClientError_RuntimeException() {
        // Simulate RuntimeException
        String customErrorMessage = "Error!";
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(new RuntimeException(customErrorMessage));

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.createSale(saleCreateRequest);
                });
        assertEquals(customErrorMessage, ex.getMessage());
    }

    @Test
    public void getSingleSale_Existant() {
        assertEquals(saleResponse, saleService.getSingleSale(createdSale.getId()));
    }

    @Test
    public void getSingleSale_NonExistant() {
        SaleException ex = assertThrows(SaleException.class,
                () -> {
                    saleService.getSingleSale(-1L);
                });

        assertEquals(ErrorMessage.SALE_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    public void getSingleSale_ClientError_FeignException() {
        // Simulate FeignException
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(FeignException.class);

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.getSingleSale(createdSale.getId());
                });
        assertEquals(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR, ex.getMessage());
    }

    @Test
    public void getSingleSale_ClientError_RuntimeException() {
        // Simulate RuntimeException
        String customErrorMessage = "Error!";
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(new RuntimeException(customErrorMessage));

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.getSingleSale(createdSale.getId());
                });
        assertEquals(customErrorMessage, ex.getMessage());
    }

    @Test
    public void getAllSales() {
        assertEquals(saleResponses, saleService.getAllSales());
    }

    @Test
    public void updateSaleDetails_Successful() {
        assertEquals(updatedSaleResponse, saleService.updateSaleDetails(saleUpdateRequest));
    }

    @Test
    public void updateSaleDetails_OverlappingSale() {
        when(repo.findByCategoryIdsAndOverlappingDates(categoryIds, startDate, endDate))
                .thenReturn(Optional.of(updatedSale));

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.updateSaleDetails(saleUpdateRequest);
        });

        assertEquals(ErrorMessage.OVERLAPPING_SALE, ex.getMessage());
    }

    @Test
    public void updateSaleDetails_InvalidDates() {
        updatedSale.setStartDate(endDate);
        updatedSale.setEndDate(startDate);

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.updateSaleDetails(saleUpdateRequest);
        });

        assertEquals(ErrorMessage.START_DATE_CANNOT_BE_AFTER_END_DATE, ex.getMessage());
    }

    @Test
    public void updateSaleDetails_DuplicateName() {
        when(repo.findByName(updatedSale.getName())).thenReturn(Optional.of(updatedSale));

        SaleException ex = assertThrows(SaleException.class, () -> {
            saleService.updateSaleDetails(saleUpdateRequest);
        });

        assertEquals(ErrorMessage.DUPLICATE_SALE_NAME, ex.getMessage());
    }

    @Test
    public void updateSaleDetails_NonExistant() {
        SaleException ex = assertThrows(SaleException.class,
                () -> {
                    saleService
                            .updateSaleDetails(new SaleUpdateRequest(-1L, null, null, startDate, endDate, categoryIds));
                });

        assertEquals(ErrorMessage.SALE_ID_NOT_FOUND, ex.getMessage());
        verify(repo, times(0)).save(any(Sale.class));
        verify(inventoryClient, times(0)).getProductsById(any());
    }

    @Test
    public void updateSaleDetails_ClientError_FeignException() {
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(FeignException.class);

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.updateSaleDetails(saleUpdateRequest);
                });
        assertEquals(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR, ex.getMessage());
    }

    @Test
    public void updateSaleDetails_ClientError_RuntimeException() {
        String customErrorMessage = "Error!";
        when(inventoryClient.getProductsByCategories(anyList())).thenThrow(new RuntimeException(customErrorMessage));

        ClientException ex = assertThrows(ClientException.class,
                () -> {
                    saleService.updateSaleDetails(saleUpdateRequest);
                });
        assertEquals(customErrorMessage, ex.getMessage());
    }

    @Test
    public void activateSales_Valid() {
        when(mockResult.getResultType()).thenReturn(ResultType.ACCEPTED);
        when(stateMachine.sendEvent(Mono.just(activateMessage))).thenReturn(Flux.just(mockResult));
        when(stateMachine.getState().getId()).thenReturn(SaleStatusEnum.ACTIVE);

        assertEquals(inactiveSaleStatus, createdSale.getStatus());
        saleService.activateSales();
        assertEquals(activeSaleStatus, createdSale.getStatus());
    }

    @Test
    public void activateSales_Invalid() {
        createdSale.setStatus(activeSaleStatus);

        when(mockResult.getResultType()).thenReturn(ResultType.DENIED);
        when(stateMachine.sendEvent(Mono.just(activateMessage))).thenReturn(Flux.just(mockResult));

        Map<Object, Object> variables = new HashMap<>();
        variables.put("ERROR", new RuntimeException("Mocked Error"));
        when(stateMachine.getExtendedState().getVariables()).thenReturn(variables);

        assertEquals(activeSaleStatus, createdSale.getStatus());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> {
                    saleService.activateSales();
                });
        assertEquals("Mocked Error", ex.getMessage());
    }

    @Test
    public void deactivateSales_Valid() {
        createdSale.setStatus(activeSaleStatus);

        when(mockResult.getResultType()).thenReturn(ResultType.ACCEPTED);
        when(stateMachine.sendEvent(Mono.just(deactivatMessage))).thenReturn(Flux.just(mockResult));
        when(stateMachine.getState().getId()).thenReturn(SaleStatusEnum.INACTIVE);

        assertEquals(activeSaleStatus, createdSale.getStatus());
        saleService.activateSales();
        assertEquals(inactiveSaleStatus, createdSale.getStatus());
    }

    @Test
    public void deactivateSales_Invalid() {
        when(mockResult.getResultType()).thenReturn(ResultType.DENIED);
        when(stateMachine.sendEvent(Mono.just(deactivatMessage))).thenReturn(Flux.just(mockResult));

        Map<Object, Object> variables = new HashMap<>();
        variables.put("ERROR", new RuntimeException("Mocked Error"));
        when(stateMachine.getExtendedState().getVariables()).thenReturn(variables);

        assertEquals(inactiveSaleStatus, createdSale.getStatus());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> {
                    saleService.deactivateSales();
                });
        assertEquals("Mocked Error", ex.getMessage());
    }

    @Test
    public void deleteSale_NonExistant() {
        SaleException ex = assertThrows(SaleException.class,
                () -> {
                    saleService.deleteSale(-1L);
                });
        assertEquals(ErrorMessage.SALE_ID_NOT_FOUND, ex.getMessage());

        verify(repo, times(0)).delete(any(Sale.class));
    }

    @Test
    public void deleteSale_Existant() {
        assertEquals("Sale deleted successfully.", saleService.deleteSale(createdSale.getId()));

        verify(repo, times(1)).delete(createdSale);
    }
}
