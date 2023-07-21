package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageListDto {

    private long id;
    private String groups;
    private String recipients;
    private String content;
    private String state;
    private String createdDate;

    public MessageListDto() {
    }

    public MessageListDto(long id, String groups, String recipients, String content, String state, String createdDate) {
        this.id = id;
        this.groups = groups;
        this.recipients = recipients;
        this.content = content;
        this.state = state;
        this.createdDate = createdDate;
    }
}
