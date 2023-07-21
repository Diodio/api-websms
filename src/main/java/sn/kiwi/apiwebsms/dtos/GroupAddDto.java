package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupAddDto {

    private int rc;
    private int oId;
    private String action;
   // private String numberOfContacts;

    public GroupAddDto() {
    }

    public GroupAddDto(int rc, int oId, String action) {
        this.rc = rc;
        this.oId = oId;
        this.action = action;
       // this.numberOfContacts = numberOfContacts;
    }
}
