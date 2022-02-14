package art.cain.interfaces;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -9172141332616435448L;
    private String name;
    private Integer age;
}
