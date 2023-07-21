package sn.kiwi.apiwebsms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter @Setter
//@AllArgsConstructor
public class PacksOMDto {

    private String idendifiant;
    private String site;
    private String total;
    private String refCommand;
    private String hashType;
    private String dateH;
    private String link;
    private String command;



    public PacksOMDto() {
    }

    public PacksOMDto(String idendifiant, String site, String total, String refCommand, String hashType, String dateH, String link, String command) {
        this.idendifiant = idendifiant;
        this.site = site;
        this.total = total;
        this.refCommand = refCommand;
        this.hashType = hashType;
        this.dateH = dateH;
        this.link = link;
        this.command = command;
    }
}