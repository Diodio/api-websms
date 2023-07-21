package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactForListDto {

    private String firstName;
    private String lastName;
    private String cellular;
    private String email;

    public ContactForListDto() {
    }

    public ContactForListDto(String firstName, String lastName, String cellular, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellular = cellular;
        this.email = email;
    }
}
