package org.bazaar.productCatalogue.sale.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.sale.dto.SaleMapper;
import org.bazaar.productCatalogue.client.ClientException;
import org.bazaar.productCatalogue.client.InventoryClient;
import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleEvent;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.dto.ProductResponse;
import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.exception.SaleException;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.bazaar.productCatalogue.saleStatus.service.SaleStatusService;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.StateMachineEventResult.ResultType;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepo repo;
    private final SaleStatusService saleStatusService;
    private final SaleMapper mapper;
    private final InventoryClient inventoryClient;
    private final StateMachineFactory<SaleStatusEnum, SaleEvent> stateMachineFactory;

    @Override
    public SaleResponse createSale(SaleCreateRequest saleCreateRequest) {
        Sale sale = mapper.toSale(saleCreateRequest);
        sale.setId(null);

        /*
         * Send request to InventoryService to retrieve a list of product id's
         * associated with the categories given in sale request
         */
        try {
            sale.setProductIds(inventoryClient.getProductsByCategories(saleCreateRequest.categoryIds()));
            List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());

            return mapper.toSaleResponse(repo.save(sale), productDtos);
        } catch (Exception e) {
            throw new ClientException(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR);
        }
    }

    @Override
    public SaleResponse getSingleSale(Long id) {
        Sale sale = searchId(id);
        try {
            List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());
            return mapper.toSaleResponse(sale, productDtos);
        } catch (Exception e) {
            throw new ClientException(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR);
        }

    }

    @Override
    public List<SaleResponse> getAllSales() {
        List<SaleResponse> saleResponses = new ArrayList<>();
        List<Sale> sales = repo.findAll();

        for (Sale sale : sales) {
            try {
                List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());
                saleResponses.add(mapper.toSaleResponse(sale, productDtos));
            } catch (Exception e) {
                throw new ClientException(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR);
            }
        }

        return saleResponses;
    }

    // @Override
    // public SaleResponse updateSaleDetails(SaleUpdateRequest saleUpdateRequest) {
    // Sale sale = searchId(saleUpdateRequest.id());
    // sale = mapper.toSale(saleUpdateRequest);

    // return mapper.toSaleResponse(repo.save(sale));
    // }

    @Scheduled(cron = "0 1 0 * * ?") // 12:01 AM daily
    @Override
    public void activateSales() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<Sale> salesStartingToday = repo.findByStartDate(currentDate);

        for (Sale sale : salesStartingToday) {
            processSaleEvent(sale, SaleEvent.ACTIVATE);
        }
    }

    @Scheduled(cron = "0 59 23 * * ?") // 11:59 PM daily
    @Override
    public void deactivateSales() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<Sale> salesEndingToday = repo.findByEndDate(currentDate);

        for (Sale sale : salesEndingToday) {
            processSaleEvent(sale, SaleEvent.DEACTIVATE);
        }
    }

    @Override
    public String deleteSale(Long id) {
        Sale sale = searchId(id);
        repo.delete(sale);

        return "Sale deleted successfully.";
    }

    @Override
    public Sale testActivate(Long id) {
        Sale sale = searchId(id);
        processSaleEvent(sale, SaleEvent.ACTIVATE);
        return sale;
    }

    @Override
    public Sale testDeactivate(Long id) {
        Sale sale = searchId(id);
        processSaleEvent(sale, SaleEvent.DEACTIVATE);
        return sale;
    }

    // Helper Functions
    private Sale searchId(Long id) {
        if (id == null) {
            throw new SaleException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        Optional<Sale> saleOptional = repo.findById(id);
        if (saleOptional.isEmpty()) {
            throw new SaleException(ErrorMessage.SALE_ID_NOT_FOUND);
        }
        return saleOptional.get();
    }

    private void processSaleEvent(Sale sale, SaleEvent event) {
        // Initialize and start state machine
        StateMachine<SaleStatusEnum, SaleEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.startReactively().block();

        // Reset state machine to current sale status
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(accessor -> accessor.resetStateMachineReactively(
                        new DefaultStateMachineContext<>(sale.getStatus().getStatus(), null, null, null)).block());

        // Store sale in extended state for further use
        stateMachine.getExtendedState().getVariables().put("sale", sale);

        // Process the event
        Message<SaleEvent> message = new GenericMessage<SaleEvent>(event);
        StateMachineEventResult<SaleStatusEnum, SaleEvent> res = stateMachine.sendEvent(Mono.just(message)).blockLast();

        // Check that the event was processed successfully
        if (res.getResultType() == ResultType.DENIED) {
            throw (RuntimeException) stateMachine.getExtendedState().getVariables().get("ERROR");
        }

        // Update sale's status with the new state
        SaleStatusEnum newStatusEnum = stateMachine.getState().getId();
        sale.setStatus(saleStatusService.getSaleStatusFromStatus(newStatusEnum));

        repo.save(sale);
    }
}