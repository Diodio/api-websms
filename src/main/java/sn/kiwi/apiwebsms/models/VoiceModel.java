package sn.kiwi.apiwebsms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VoiceModel {

    @NotBlank(message = "User is required.")
    private String user_id;

    @NotBlank(message = "Customer id is required.")
    private String customer_id;

    @NotBlank(message = "Recipient is required.")
    private String recipient;

    private MultipartFile file;

}
