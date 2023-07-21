package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilDto {

    private String contactName;


    public ProfilDto() {
    }
    public ProfilDto(String contactName) {
        this.contactName = contactName;
    }
}
