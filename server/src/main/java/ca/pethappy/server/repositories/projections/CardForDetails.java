package ca.pethappy.server.repositories.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CardForDetails {
    Long getId();
    int getExpMonth();
    int getExpYear();
    String getNameOnCard();
    @Value("#{@cardsService.getCardName(target)}")
    String getNumber();
}
