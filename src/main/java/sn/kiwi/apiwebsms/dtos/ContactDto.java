package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactDto {

    private long id;
    private String firstName;
    private String lastName;
    private String cellular;
    private String email;

    public ContactDto() {
    }

    public ContactDto(long id, String firstName, String lastName, String cellular, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellular = cellular;
        this.email = email;
    }
}
