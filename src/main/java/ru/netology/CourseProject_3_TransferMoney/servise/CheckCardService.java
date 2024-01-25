package ru.netology.CourseProject_3_TransferMoney.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.CourseProject_3_TransferMoney.model.Card;
import ru.netology.CourseProject_3_TransferMoney.repository.TransferRepository;

@Service
@AllArgsConstructor
public class CheckCardService {

    private final TransferRepository repository;

    public boolean isExistCardFrom(String cardNumber, String cardValidTill, String cardCvc) {
        if (repository.getListOfCardDemo().containsKey(cardNumber)) {
            return repository.getListOfCardDemo().get(cardNumber).getValidTill().equals(cardValidTill)
                    && repository.getListOfCardDemo().get(cardNumber).getCvc().equals(cardCvc);
        }
        return false;
    }

    public boolean isExistCardTo(String cardNumber) {
        return repository.getListOfCardDemo().containsKey(cardNumber);
    }

    public Card getCard(String cardNumber) {
        return repository.getListOfCardDemo().get(cardNumber);
    }
}
