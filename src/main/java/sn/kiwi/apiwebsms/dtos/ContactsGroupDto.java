package sn.kiwi.apiwebsms.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContactsGroupDto {

    private int groupId;
    private String groupName;
    private List<ContactsByGroupDto> contacts;

    public ContactsGroupDto() {}

    public ContactsGroupDto(int groupId, String groupName, List<ContactsByGroupDto> contacts) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.contacts = contacts;
    }
    @JsonProperty("contacts")
    public List<ContactsByGroupDto> getContacts() {
        return contacts;
    }
}



