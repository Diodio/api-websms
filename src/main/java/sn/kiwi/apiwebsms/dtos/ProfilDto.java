package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilDto {

    private String contactName;
    private String subscriptionDate;
    //private String subscriptionType;
    private String language;


    public ProfilDto() {
    }

    public ProfilDto(String contactName, String subscriptionDate, String language) {
        this.contactName = contactName;
        this.subscriptionDate = subscriptionDate;
        //this.subscriptionType = subscriptionType;
        this.language = language;
    }
}
