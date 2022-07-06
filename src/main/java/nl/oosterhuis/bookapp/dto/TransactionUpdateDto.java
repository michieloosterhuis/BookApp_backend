package nl.oosterhuis.bookapp.dto;

import javax.validation.constraints.NotNull;

public class TransactionUpdateDto {

    @NotNull(message = "exchange book id cannot be null")
    private Long exchangeBookId;

    public Long getExchangeBookId() {
        return exchangeBookId;
    }

    public void setExchangeBookId(Long exchangeBookId) {
        this.exchangeBookId = exchangeBookId;
    }
}
