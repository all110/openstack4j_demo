package com.chinaums.ysmktaln.spbillserv.common.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.ysmktaln.spbillserv.common.Exception.Asserts;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleCode;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleException;
import com.chinaums.ysmktaln.spbillserv.common.model.CloudParam;
import com.chinaums.ysmktaln.spbillserv.common.service.CloudService;
import com.chinaums.ysmktaln.spbillserv.common.utils.MsgInitUtils;
import com.chinaums.ysmktaln.spbillserv.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/cloud"})
public class CloudController {
   private Logger logger = LoggerFactory.getLogger(CloudController.class);
   @Autowired
   private CloudService cloudService;

   @RequestMapping({"/createServe"})
   @ResponseBody
   public Object createServe(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");

      try {
         this.logger.info(" {} 请求报文:{}", msgCrrltnId, JSONObject.toJSONString(reqParam));
         String request_type = (String)reqParam.get("request_type");
         String cloud_type = (String)reqParam.get("cloud_type");
         String msg_req_code = (String)reqParam.get("msg_req_code");
         Asserts.isTrue(reqParam.containsKey("req_params"), HandleCode.RSP_1001, "req_params为空");
         List<CloudParam> req_params = JSONArray.parseArray(JSONObject.toJSONString(reqParam.get("req_params")), CloudParam.class);
         Asserts.hasText(request_type, HandleCode.RSP_1001, "request_type为空");
         Asserts.hasText(cloud_type, HandleCode.RSP_1001, "cloud_type为空");
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type为空");
         Asserts.hasText(msg_req_code, HandleCode.RSP_1001, "msg_req_code为空");
         if ("createServerOnImage".equals(request_type)) {
            Asserts.isTrue(!req_params.stream().anyMatch((cloudParam) -> {
               return StringUtils.isBlank(cloudParam.getRequest_id()) || StringUtils.isBlank(cloudParam.getServe_name()) || StringUtils.isBlank(cloudParam.getFlavor_id()) || StringUtils.isBlank(cloudParam.getImage_id());
            }), HandleCode.RSP_1002, "数据基本校验不通过");
         } else {
            if (!"createServerOnVolume".equals(request_type)) {
               throw new HandleException(HandleCode.RSP_1002, "无效请求类型");
            }

            Asserts.isTrue(!req_params.stream().anyMatch((cloudParam) -> {
               return StringUtils.isBlank(cloudParam.getRequest_id()) || StringUtils.isBlank(cloudParam.getServe_name()) || StringUtils.isBlank(cloudParam.getFlavor_id()) || StringUtils.isBlank(cloudParam.getVolume_id());
            }), HandleCode.RSP_1002, "数据基本校验不通过");
         }

         this.cloudService.batchCreateCloud(msgCrrltnId, req_params, request_type, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", var11.getCode());
         respParam.put("msg_rsp_desc", var11.getMessage());
      } catch (Exception var12) {
         this.logger.error("{} {}", msgCrrltnId, var12);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/createVolume"})
   @ResponseBody
   public Object createBootableVolume(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String snapshot_id = (String)reqParam.get("snapshot_id");
      String volume_name = (String)reqParam.get("volume_name");
      String request_id = (String)reqParam.get("request_id");
      String cloud_type = (String)reqParam.get("cloud_type");
      Asserts.hasText(cloud_type, HandleCode.RSP_1001, "cloud_type为空");
      Asserts.hasText(snapshot_id, HandleCode.RSP_1001, "snapshot_id为空");
      Asserts.hasText(volume_name, HandleCode.RSP_1001, "volume_name为空");
      Asserts.hasText(request_id, HandleCode.RSP_1001, "request_id为空");
      Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type取值错误");

      try {
         this.cloudService.createBootableVolume(msgCrrltnId, volume_name, snapshot_id, request_id, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var12) {
         this.logger.error("{} {}", msgCrrltnId, var12);
         respParam.put("msg_rsp_code", var12.getCode());
         respParam.put("msg_rsp_desc", var12.getMessage());
      } catch (Exception var13) {
         this.logger.error("{} {}", msgCrrltnId, var13);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/queryServeId"})
   @ResponseBody
   public Object queryServeId(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
//      this.logger.info("reqParam:{}", reqParam);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");

      try {
         Asserts.isTrue(reqParam.containsKey("req_params"), HandleCode.RSP_1001, "req_params为空");
         List<JSONObject> req_params = JSONArray.parseArray(JSONObject.toJSONString(reqParam.get("req_params")), JSONObject.class);
         Asserts.isTrue(!req_params.stream().anyMatch((jsonObject) -> {
            return StringUtils.isBlank(jsonObject.getString("request_id"));
         }), HandleCode.RSP_1002, "数据基本校验不通过");
         List<JSONObject> restult = new ArrayList();

         req_params.stream().forEach((request_ids) -> {
            JSONObject jsonObject = new JSONObject();
            String request_id = request_ids.getString("request_id");
            CloudParam cloudParam = this.cloudService.getServIdByRequestId(request_id);

            Asserts.notNull(cloudParam, HandleCode.RSP_1001, "请求流水不存在");
            jsonObject.put("server_id", cloudParam.getService_id());
            jsonObject.put("status", cloudParam.getStatus());
            jsonObject.put("request_id", request_id);
            restult.add(jsonObject);
         });
         respParam.put("rsp_params", restult);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var9) {
         this.logger.error("{} {}", msgCrrltnId, var9);
         respParam.put("msg_rsp_code", var9.getCode());
         respParam.put("msg_rsp_desc", var9.getMessage());
      } catch (Exception var10) {
         this.logger.error("{} {}", msgCrrltnId, var10);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/queryServeStatus"})
   @ResponseBody
   public Object queryServeStatus(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String server_id = (String)reqParam.get("server_id");
      String cloud_type = (String)reqParam.get("cloud_type");
      String request_type = (String)reqParam.get("request_type");

      try {
         Asserts.hasText(cloud_type, HandleCode.RSP_1001, "cloud_type为空");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "request_type为空");
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type取值错误");
         Asserts.matches(request_type, "server|volume|template|snapShot", HandleCode.RSP_1001, "request_type类型错误");
         String status = this.cloudService.queryStatus(msgCrrltnId, request_type, server_id, cloud_type);
         if ("AVAILABLE".equals(status)) {
            status = "ACTIVE";
         }

         respParam.put("status", status);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", var11.getCode());
         respParam.put("msg_rsp_desc", var11.getMessage());
      } catch (Exception var12) {
         this.logger.error("{} {}", msgCrrltnId, var12);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/deleteServe"})
   @ResponseBody
   public Object deleteByServId(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String request_type = (String)reqParam.get("request_type");
      String cloud_type = (String)reqParam.get("cloud_type");
      String server_id = (String)reqParam.get("server_id");

      try {
         Asserts.hasText(cloud_type, HandleCode.RSP_1001, "cloud_type为空");
         Asserts.hasText(request_type, HandleCode.RSP_1001, "request_type为空");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "server_id为空");
         Asserts.matches(request_type, "server|volume|template|snapShot", HandleCode.RSP_1001, "request_type类型错误");
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type类型错误");
         this.cloudService.deleteByServId(msgCrrltnId, server_id, cloud_type, request_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", var11.getCode());
         respParam.put("msg_rsp_desc", var11.getMessage());
      } catch (Exception var12) {
         this.logger.error("{} {}", msgCrrltnId, var12);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/createTpl"})
   @ResponseBody
   public Object createTpl(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String request_id = (String)reqParam.get("request_id");
      String server_id = (String)reqParam.get("server_id");
      String serve_name = (String)reqParam.get("serve_name");
      String cloud_type = (String)reqParam.get("cloud_type");

      try {
         Asserts.hasText(cloud_type, HandleCode.RSP_1001, "cloud_type为空");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "server_id为空");
         Asserts.hasText(serve_name, HandleCode.RSP_1001, "serve_name为空");
         Asserts.hasText(request_id, HandleCode.RSP_1001, "request_id为空");
         Asserts.hasText(msgCrrltnId, HandleCode.RSP_1001, "msgCrrltnId为空");
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type类型错误");
         this.cloudService.createTpl(msgCrrltnId, request_id, server_id, serve_name, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var12) {
         this.logger.error("{} {}", msgCrrltnId, var12);
         respParam.put("msg_rsp_code", var12.getCode());
         respParam.put("msg_rsp_desc", var12.getMessage());
      } catch (Exception var13) {
         this.logger.error("{} {}", msgCrrltnId, var13);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/closeServ"})
   @ResponseBody
   public Object closeServ(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String server_id = (String)reqParam.get("server_id");
      String cloud_type = (String)reqParam.get("cloud_type");

      try {
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type类型错误");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "server_id为空");
         this.cloudService.closeServ(msgCrrltnId, server_id, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var10) {
         this.logger.error("{} {}", msgCrrltnId, var10);
         respParam.put("msg_rsp_code", var10.getCode());
         respParam.put("msg_rsp_desc", var10.getMessage());
      } catch (Exception var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/rebootServ"})
   @ResponseBody
   public Object rebootServer(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String server_id = (String)reqParam.get("server_id");
      String cloud_type = (String)reqParam.get("cloud_type");

      try {
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type类型错误");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "server_id为空");
         this.cloudService.rebootServer(msgCrrltnId, server_id, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var10) {
         this.logger.error("{} {}", msgCrrltnId, var10);
         respParam.put("msg_rsp_code", var10.getCode());
         respParam.put("msg_rsp_desc", var10.getMessage());
      } catch (Exception var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }

   @RequestMapping({"/startServer"})
   @ResponseBody
   public Object startServer(HttpServletRequest httpServletRequest) {
      long start = System.currentTimeMillis();
      Map<String, Object> reqParam = MsgInitUtils.initRequestMap(httpServletRequest);
      Map<String, Object> respParam = new HashMap();
      String msgCrrltnId = (String)reqParam.get("msg_crrltn_id");
      String server_id = (String)reqParam.get("server_id");
      String cloud_type = (String)reqParam.get("cloud_type");

      try {
         Asserts.matches(cloud_type, "PUBLIC|PRIVATE", HandleCode.RSP_1001, "cloud_type类型错误");
         Asserts.hasText(server_id, HandleCode.RSP_1001, "server_id为空");
         this.cloudService.startServer(msgCrrltnId, server_id, cloud_type);
         respParam.put("msg_rsp_code", "0000");
         respParam.put("msg_rsp_desc", "成功");
      } catch (HandleException var10) {
         this.logger.error("{} {}", msgCrrltnId, var10);
         respParam.put("msg_rsp_code", var10.getCode());
         respParam.put("msg_rsp_desc", var10.getMessage());
      } catch (Exception var11) {
         this.logger.error("{} {}", msgCrrltnId, var11);
         respParam.put("msg_rsp_code", "1001");
         respParam.put("msg_rsp_desc", "稍后再试");
      }

      this.logger.info("{} 处理完成,总耗时{} ", msgCrrltnId, System.currentTimeMillis() - start);
      return respParam;
   }
}
