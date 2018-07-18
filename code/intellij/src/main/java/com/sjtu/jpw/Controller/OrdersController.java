package com.sjtu.jpw.Controller;

import com.google.gson.JsonArray;
import com.sjtu.jpw.Service.OrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class OrdersController {

    @Resource(name="ordersService")
    private OrdersService ordersService;
    @RequestMapping(value="/getCurrentOrder",produces="application/json;charset=UTF-8")
    public void Hello(HttpServletRequest request, HttpServletResponse response) throws InterruptedException,IOException {
        response.setHeader("Content-type","application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        int userId=Integer.parseInt(request.getParameter("userId"));
        JsonArray currentOrders=ordersService.getCurrentOrder(userId);

        System.out.println(currentOrders);
        out.print(currentOrders);
        out.flush();
    }
}