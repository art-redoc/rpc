package art.cain.common.rpc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Parameter implements Serializable {
    private static final long serialVersionUID = -2553574659813470984L;
    private String parameterType;
    private Object arg;
}
