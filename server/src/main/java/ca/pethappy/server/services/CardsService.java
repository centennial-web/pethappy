package ca.pethappy.server.services;

import ca.pethappy.server.models.Card;
import ca.pethappy.server.repositories.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CardsService {
    private final CardsRepository cardsRepository;

    @Autowired
    public CardsService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return cardsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }

    @Transactional(readOnly = true)
    public List<Card> findAllByUserId(Long userId) {
        return cardsRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Card> getUserCardsForMobile(Long userId) {
        List<Card> originalCards = cardsRepository.findAllByUserId(userId);
        List<Card> mobileCards = new ArrayList<>(originalCards.size());

        // Convert to movile cards
        for (Card card : originalCards) {
            Card mobileCard = new Card();
            mobileCard.setId(card.getId());
            mobileCard.setNumber(CardType.detect(card.getNumber()) + " ending in " + card.getNumber().substring(card.getNumber().length() - 4));
            mobileCard.setExpMonth(card.getExpMonth());
            mobileCard.setExpYear(card.getExpYear());
            mobileCard.setCvv(0);
            mobileCard.setNameOnCard(card.getNameOnCard());
            mobileCard.setUser(null);
            mobileCards.add(mobileCard);
        }

        return mobileCards;
    }

    public enum CardType {
        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$"),
        MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$"),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
        DINERS_CLUB("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
        DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"),
        JCB("^(?:2131|1800|35\\d{3})\\d{11}$"),
        CHINA_UNION_PAY("^62[0-9]{14,17}$");

        private Pattern pattern;

        CardType() {
            this.pattern = null;
        }

        CardType(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public static CardType detect(String cardNumber) {
            for (CardType cardType : CardType.values()) {
                if (null == cardType.pattern) continue;
                if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
            }
            return UNKNOWN;
        }
    }

    public boolean isValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public String getCardName(Card card) {
        return CardType.detect(card.getNumber()) + " ending in " + card.getNumber().substring(card.getNumber().length() - 4);
    }
}
