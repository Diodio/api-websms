package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PubListDto {

    private long id;
    private String image;
    private String createdDate;
    private String startDate;
    private String endDate;
    private String typeMSG;

    public PubListDto() {
    }

    public PubListDto(long id, String image, String createdDate, String startDate, String endDate, String typeMSG) {
        this.id = id;
        this.image = image;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.typeMSG=typeMSG;
    }
}