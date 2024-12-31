package org.bazaar.productCatalogue.config;

import org.bazaar.productCatalogue.client.ClientException;
import org.bazaar.productCatalogue.client.InventoryClient;
import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleEvent;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.dto.PriceUpdateRequest;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.exception.SaleException;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import lombok.AllArgsConstructor;

@Configuration
@EnableStateMachineFactory
@AllArgsConstructor
public class SaleStateMachineConfig extends EnumStateMachineConfigurerAdapter<SaleStatusEnum, SaleEvent> {
    private final InventoryClient inventoryClient;

    @Override
    public void configure(StateMachineStateConfigurer<SaleStatusEnum, SaleEvent> states) throws Exception {
        states
                .withStates()
                .initial(SaleStatusEnum.INACTIVE) // Default state
                .state(SaleStatusEnum.ACTIVE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SaleStatusEnum, SaleEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SaleStatusEnum.INACTIVE).target(SaleStatusEnum.ACTIVE).event(SaleEvent.ACTIVATE)
                .action(activateAction(), ErrorAction())
                .and()
                .withExternal()
                .source(SaleStatusEnum.ACTIVE).target(SaleStatusEnum.INACTIVE).event(SaleEvent.DEACTIVATE)
                .action(deactivateAction(), ErrorAction());
    }

    // Actions
    private Action<SaleStatusEnum, SaleEvent> activateAction() {
        return context -> {
            Sale sale = (Sale) context.getExtendedState().getVariables().get("sale");
            if (sale == null) {
                throw new SaleException(ErrorMessage.SALE_ID_NOT_FOUND);
            }
            try {
                inventoryClient
                        .updateProductPrices(
                                new PriceUpdateRequest(sale.getProductIds(), sale.getDiscountPercentage()));
            } catch (Exception e) {
                throw new ClientException(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR);
            }
        };
    }

    private Action<SaleStatusEnum, SaleEvent> deactivateAction() {
        return context -> {
            Sale sale = (Sale) context.getExtendedState().getVariables().get("sale");
            if (sale == null) {
                throw new SaleException(ErrorMessage.SALE_ID_NOT_FOUND);
            }
            try {
                // 100% percentage to disable sale
                inventoryClient.updateProductPrices(new PriceUpdateRequest(sale.getProductIds(), 1.0f));
            } catch (Exception e) {
                throw new ClientException(ErrorMessage.INVENTORY_SERVICE_CONNECTION_ERROR);
            }
        };
    }

    private Action<SaleStatusEnum, SaleEvent> ErrorAction() {
        return context -> {
            context.getExtendedState().getVariables().put("ERROR", context.getException());
        };
    }
}
