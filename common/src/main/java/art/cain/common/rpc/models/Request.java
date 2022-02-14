package art.cain.common.rpc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Request implements Serializable {
    private static final long serialVersionUID = -7921802108660178724L;
    private String className;
    private String methodName;
    private List<Parameter> parameters;
}
