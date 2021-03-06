package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.common.entity.platform.bithumb.BithumbQuotes;
import org.wallet.common.entity.platform.bithumb.BithumbResult;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.httpclient.Browsers;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_HPX)
@org.springframework.stereotype.Service
public class HPXService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(HPXService.class);

    public ServiceResponse getQuotes(ServiceRequest request, ServiceResponse response){
        String symbol = request.getParamValue("symbol");

        if(StringUtils.isEmpty(symbol)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[symbol]不存在！");
            return response;
        }
        try {
            String body = HttpConnectionPool.get("https://api.hpx.com/data/v2/ticker?currency=" + symbol.toLowerCase(), Browsers.CHROME);
            BithumbResult<List<BithumbQuotes>> result = JSON.parseObject(body, new TypeReference<BithumbResult<List<BithumbQuotes>>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", result);
        } catch (Exception e) {
            String errorMsg = "获取HPX 24H行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }

}
