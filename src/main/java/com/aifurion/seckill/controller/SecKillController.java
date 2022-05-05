package com.aifurion.seckill.controller;

import com.aifurion.seckill.service.OrderService;
import com.aifurion.seckill.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:14
 */

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/")
public class SecKillController {

    private static final String SUCCESS = "REQUEST SUCCESS";
    private static final String ERROR = "REQUEST ERROR";

    @Autowired
    private OrderService orderService;


    /**
     * 秒杀基本过程
     * @param sid
     * @return
     */
    @PostMapping("createNormalOrder")
    public String createNormalOrder(int sid) {
        int res = 0;
        try {
            res = orderService.createNormalOrder(sid);
        } catch (Exception e) {
            log.error("Exception:" + e);
        }

        return res == 1 ? SUCCESS : ERROR;
    }


}
