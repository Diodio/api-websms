package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupDto {

    private long id;
    private String name;
    private String description;
    private String numberOfContacts;

    public GroupDto() {
    }

    public GroupDto(long id, String name, String description, String numberOfContacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfContacts = numberOfContacts;
    }
}
