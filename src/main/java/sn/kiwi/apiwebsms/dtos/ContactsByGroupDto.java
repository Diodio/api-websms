package sn.kiwi.apiwebsms.dtos;

import java.util.ArrayList;

public class ContactsByGroupDto {

    private int contactId;
    private String contactFirstName;
    private String contactLastName;
    private String contactCellular;
    private String contactEmail;

    public ArrayList<ContactsGroupDto> contacts;

    public ContactsByGroupDto() {
    }

    public ContactsByGroupDto(int contactId, String contactFirstName, String contactLastName, String contactCellular, String contactEmail) {

        this.contactId = contactId;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactCellular = contactCellular;
        this.contactEmail = contactEmail;
    }
}
