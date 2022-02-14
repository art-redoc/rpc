package art.cain.common.rpc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePackage implements Serializable {
    private static final long serialVersionUID = 7032199509712758734L;
    private Header header;
    private Object response;
}
