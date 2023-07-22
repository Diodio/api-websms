package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUserDto {

    private String remainingNumberOfSms;
    private String expirationDate;


    public AccountUserDto() {
    }

    public AccountUserDto(String remainingNumberOfSms, String expirationDate ) {

        this.remainingNumberOfSms = remainingNumberOfSms;
        this.expirationDate = expirationDate;
    }
}
