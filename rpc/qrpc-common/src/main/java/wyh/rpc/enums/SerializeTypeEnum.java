package wyh.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializeTypeEnum {
    PROTOSTUFF((byte)1 , "protostuff"),
    HESSIAN((byte)2 , "hessian");
    private byte code;
    private String type;
    public String getTypeByCode(byte code){
        for(SerializeTypeEnum serializeTypeEnum:SerializeTypeEnum.values()){
            if(serializeTypeEnum.getCode() == code){
                return serializeTypeEnum.getType();
            }
        }
        return "";
    }
}
