package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ContactsGroupDto {

    private int groupId;
    private String groupName;
  /*  private int contactId;
    private String contactFirstName;
    private String contactLastName;
    private String contactCellular;
    private String contactEmail;*/

    public ArrayList<ContactsByGroupDto> contacts;

    public ContactsGroupDto() {
    }

    public ContactsGroupDto(int groupId, String groupName,  ArrayList<ContactsByGroupDto> contacts ) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.contacts=contacts;
    }
}


