package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageDto {

    private long id;
    private String groups;
    private String content;
    private String state;
    private String createdDate;

    public MessageDto() {
    }

    public MessageDto(long id, String groups, String content, String state, String createdDate) {
        this.id = id;
        this.groups = groups;
        this.content = content;
        this.state = state;
        this.createdDate = createdDate;
    }
}
