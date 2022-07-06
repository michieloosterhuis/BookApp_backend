package nl.oosterhuis.bookapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransactionInputDto {
    @NotNull(message = "requester username cannot be null")
    @NotBlank(message = "requester username cannot be empty")
    private String requesterUsername;

    @NotNull(message = "requested book id cannot be null")
    private Long requestedBookId;

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public Long getRequestedBookId() {
        return requestedBookId;
    }

    public void setRequestedBookId(Long requestedBookId) {
        this.requestedBookId = requestedBookId;
    }
}
