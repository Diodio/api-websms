package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUserDto {

    private String userContactName;


    public AccountUserDto() {
    }

    public AccountUserDto(String userContactName) {
        this.userContactName = userContactName;
    }
}
