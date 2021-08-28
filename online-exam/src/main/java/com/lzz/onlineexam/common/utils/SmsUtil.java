package com.lzz.onlineexam.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;



/**阿里云发送短信*/
public class SmsUtil {
    /**发送短信*/
    public Integer SendMsg(String PhoneNumbers, String TemplateParam, Integer type) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G4QJrrPyWiBadGujDnG", "zrWkk038JCRx6zTx3bcl3d5TuDHImc");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", PhoneNumbers);
        request.putQueryParameter("SignName", "猫儿王国");
        if(type == 0){//发送注册验证码
            request.putQueryParameter("TemplateCode", "SMS_184105640");
        }else if(type == 1){//发送重置密码验证码
            request.putQueryParameter("TemplateCode", "SMS_184110714");
        }else if (type == 2){//发送更换手机号验证码
            request.putQueryParameter("TemplateCode", "SMS_184120718");
        }
        request.putQueryParameter("TemplateParam", "{\"code\":\""+TemplateParam+"\"}");
        CommonResponse response=null;
        try {
            response = client.getCommonResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        assert response != null;
        JSONObject result = JSONObject.parseObject(response.getData());

        if("OK".equals(result.getString("Code"))){
            return 1;
        }
        if("isv.MOBILE_NUMBER_ILLEGAL".equals(result.getString("Code"))){
            return 2;//非法手机号
        }
        return 0;
    }
}
