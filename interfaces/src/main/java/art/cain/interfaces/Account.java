package art.cain.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class Account implements Serializable {
    private static final long serialVersionUID = -7531515137490412784L;
    private String username;
    private String password;
}
