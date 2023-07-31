package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
public class ContactsByGroupDto {

    private int contactId;
    private String firstName;
    private String lastName;
    private String cellular;
    private String email;

    public ContactsByGroupDto() {}

    public ContactsByGroupDto(int contactId, String firstName, String lastName, String cellular, String email) {
        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellular = cellular;
        this.email = email;
    }
}
