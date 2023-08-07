package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactsByStatusDto {

    ////private int contactId;
    //private String firstName;
   // private String lastName;
    private String createdDate;
    private String recipient;

    public ContactsByStatusDto() {}

    //public ContactsByStatusDto(int contactId, String firstName, String lastName, String cellular, String email) {
    public ContactsByStatusDto(String createdDate, String recipient ) {
        this.createdDate=createdDate;
        this.recipient=recipient;
//        this.contactId = contactId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.cellular = cellular;
//        this.email = email;
    }
}
