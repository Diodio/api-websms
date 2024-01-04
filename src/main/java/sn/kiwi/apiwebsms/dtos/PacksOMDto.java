package sn.kiwi.apiwebsms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter @Setter
//@AllArgsConstructor
public class PacksOMDto {

    private String amount;
    private String message;



    public PacksOMDto() {
    }

    public PacksOMDto(String amount, String message) {
        this.amount = amount;
        this.message = message;

    }
}