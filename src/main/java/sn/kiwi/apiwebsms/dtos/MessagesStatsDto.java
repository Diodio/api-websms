package sn.kiwi.apiwebsms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MessagesStatsDto {

    private String status;
    private int number;
    private List<ContactsByStatusDto> contacts;

    public MessagesStatsDto() {
    }

    public MessagesStatsDto(String status, int number, List<ContactsByStatusDto> contacts) {
        this.status = status;
        this.number = number;
        this.contacts = contacts;
    }

    @JsonProperty("contacts")
    public List<ContactsByStatusDto> getContacts() {
        return contacts;
    }
}
