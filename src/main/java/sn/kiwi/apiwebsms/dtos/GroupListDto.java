package sn.kiwi.apiwebsms.dtos;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class GroupListDto {

    private int value;
    private String text;
    private String desc="néant";

    public GroupListDto() {
    }

    public GroupListDto(int value, String text, String desc) {
        this.value = value;
        this.text = text;
        this.desc = desc;
    }
}