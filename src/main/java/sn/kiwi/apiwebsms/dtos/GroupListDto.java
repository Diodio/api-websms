package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class GroupListDto {

    private int value;
    private String text;

    public GroupListDto() {
    }

    public GroupListDto(int value, String text) {
        this.value = value;
        this.text = text;
    }
}
