package wyh.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum CompressTypeEnum {
    GZIP((byte)1 , "gzip");
    private byte code;
    private String type;
    public String getTypeByCode(byte code){
        for(CompressTypeEnum compressTypeEnum:CompressTypeEnum.values()){
            if(compressTypeEnum.getCode() == code){
                return compressTypeEnum.getType();
            }
        }
        return "";
    }
}
