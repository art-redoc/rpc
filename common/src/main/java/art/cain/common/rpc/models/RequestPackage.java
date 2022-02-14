package art.cain.common.rpc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class RequestPackage implements Serializable {

    private static final long serialVersionUID = -6580369901753893103L;

    private Header header;
    private Request request;
}
