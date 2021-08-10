package tads.ufrn.eaj.loja.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.text.SimpleDateFormat;
import java.util.Date;

import tads.ufrn.eaj.loja.Message.Message;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Joia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String imageUri;
    private Date deleted = null;
    private String description;
    private String category;
    @Min(value = 1, message = Message.ERRO_TAMANHO_STRING)
    private double price;
    private String type;
}
