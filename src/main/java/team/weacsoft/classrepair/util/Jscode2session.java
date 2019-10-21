package team.weacsoft.classrepair.util;

import cn.hutool.json.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author GreenHatHG
 **/

@Component
@Data
public class Jscode2session {
    @Value("${APPID}")
    private String APPID;

    @Value("${APPSECRET}")
    private String SECRET;

    private String GRANT_TYPE = "authorization_code";

    public JSONObject get(String jsCode){
        return WxUtils.wxAuth(jsCode, APPID, SECRET, GRANT_TYPE);
    }
}
