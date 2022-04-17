package com.chinaums.ysmktaln.spbillserv.common.service;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.ysmktaln.spbillserv.common.Exception.Asserts;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleCode;
import com.chinaums.ysmktaln.spbillserv.common.Exception.HandleException;
import com.chinaums.ysmktaln.spbillserv.common.enums.CloudRoute;
import com.chinaums.ysmktaln.spbillserv.common.interfaces.CloudManager;
import com.chinaums.ysmktaln.spbillserv.common.model.CloudParam;
import com.chinaums.ysmktaln.spbillserv.common.utils.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class CloudService {
   private Logger logger = LoggerFactory.getLogger(CloudService.class);
   @Autowired
   private JdbcTemplate jdbcTemplate;
   @Autowired
   private ThreadPoolExecutor threadPoolExecutor;

   public CloudParam getServIdByRequestId(String requestId) {
      String sql = "select SERVER_ID,STATUS from tbl_cloud_request where REQUEST_ID=?";
      return (CloudParam)this.jdbcTemplate.query(sql, new String[]{requestId}, new int[]{12}, new ResultSetExtractor<CloudParam>() {
         public CloudParam extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            if (resultSet.next()) {
               CloudParam cloudParam = new CloudParam();
               cloudParam.setStatus(resultSet.getString("STATUS"));
               cloudParam.setService_id(resultSet.getString("SERVER_ID"));
               return cloudParam;
            } else {
               return null;
            }
         }
      });
   }

   public void createBootableVolume(String msgId, String name, String servId, String requestId, String cloudType) {
      try {
         Asserts.isTrue(this.insertRequestId(requestId, cloudType, name) > 0, HandleCode.RSP_1001, "insert流水号失败");
      } catch (Exception var7) {
         this.logger.error("{}{}", msgId, var7);
         throw new HandleException(HandleCode.RSP_1001, "流水号不可重复");
      }

      this.threadPoolExecutor.execute(new Runnable() {
         public void run() {
            CloudManager cloudManager = CloudRoute.getCloudManager(cloudType);

            try {
               Volume volume = cloudManager.createBootableVolume(name, servId);
               Asserts.notNull(volume, HandleCode.RSP_1001, "创建卷失败");
               CloudService.this.logger.info("{} volume：{}", msgId, JSONObject.toJSONString(volume));
               CloudService.this.logger.info("{} 第一次创建卷更新结果 {}", msgId, CloudService.this.updateServId(volume.getId(), requestId, "DONE"));
            } catch (Exception var5) {
               CloudService.this.logger.error("{} 创建卷异常,删除重新创建{}", msgId, var5);

               try {
                  Volume volumex = cloudManager.createBootableVolume(name, servId);
                  Asserts.notNull(volumex, HandleCode.RSP_1001, "创建卷失败");
                  CloudService.this.logger.info("{} 更新结果 {}", msgId, CloudService.this.updateServId(volumex.getId(), requestId, "DONE"));
               } catch (Exception var4) {
                  CloudService.this.logger.error("{} 创建卷重试异常{}", msgId, var4);
                  CloudService.this.logger.info("{} 更新为失败 {}", msgId, CloudService.this.updateServId("", requestId, "ERROR"));
               }
            }

         }
      });
   }

   public String queryStatus(String msgId, String requestType, String servId, String cloudType) {
      CloudManager cloudManager = CloudRoute.getCloudManager(cloudType);
      if ("server".equals(requestType)) {
         Server server = cloudManager.showServer(servId);
         return server == null ? "NOEXIST" : server.getStatus().name();
      } else {
         VolumeSnapshot volumeSnapshot;
         if ("snapShot".equals(requestType)) {
            volumeSnapshot = cloudManager.getVolumeSnapshot(servId);
            return volumeSnapshot == null ? "NOEXIST" : volumeSnapshot.getStatus().name();
         } else if ("template".equals(requestType)) {
            volumeSnapshot = cloudManager.getVolumeSnapshot(servId);
            return volumeSnapshot == null ? "NOEXIST" : volumeSnapshot.getStatus().name();
         } else if ("volume".equals(requestType)) {
            Volume volume = cloudManager.showVolume(servId);
            return volume == null ? "NOEXIST" : volume.getStatus().name();
         } else {
            return null;
         }
      }
   }

   public void deleteByServId(String msgId, String servId, String cloudType, String requestType) {
      CloudManager cloudManager = CloudRoute.getCloudManager(cloudType);
      if ("server".equals(requestType)) {
         cloudManager.deleteServer(servId);
      } else if ("snapShot".equals(requestType)) {
         cloudManager.deleteVolumeSnapshot(servId);
      } else if ("template".equals(requestType)) {
         cloudManager.deleteVolumeSnapshot(servId);
      } else if ("volume".equals(requestType)) {
         cloudManager.deleteVolume(servId);
      }

   }

   public void batchCreateCloud(String msgId, List<CloudParam> list, String requestType, String cloudType) {
      list.forEach((cloudParam) -> {
         this.logger.info("{}入库结果 {}", msgId, this.insert(msgId, cloudParam, requestType, cloudType));
         this.threadPoolExecutor.execute(new Runnable() {
            public void run() {
               try {
                  CloudManager cloudManager = CloudRoute.getCloudManager(cloudType);
                  String servId = null;
                  if ("createServerOnImage".equals(requestType)) {
                     servId = cloudManager.createServerFromImage(cloudParam.getServe_name(), cloudParam.getFlavor_id(), cloudParam.getImage_id());
                  } else {
                     if (!"createServerOnVolume".equals(requestType)) {
                        throw new HandleException(HandleCode.RSP_1001, "无效请求类型");
                     }

                     servId = cloudManager.createServerFormVolume(cloudParam.getServe_name(), cloudParam.getFlavor_id(), cloudParam.getVolume_id()).getId();
                  }

                  if (StringUtils.isNotBlank(servId)) {
                     CloudService.this.logger.info("{} 服务id:{} requestId:{} 更新结果{}", new Object[]{msgId, servId, cloudParam.getRequest_id(), CloudService.this.updateServId(servId, cloudParam.getRequest_id(), "DONE")});
                  } else {
                     CloudService.this.logger.error("{} requestid{} 服务id生成失败{}", msgId, cloudParam.getRequest_id());
                  }
               } catch (Exception var3) {
                  CloudService.this.logger.error("{} 创建云平台异常{}", msgId, var3);
               }

            }
         });
      });
   }

   public int updateServId(String servId, String requestId, String status) {
      String sql = "update tbl_cloud_request set SERVER_ID=?,STATUS=? where REQUEST_ID=?";
      return this.jdbcTemplate.update(sql, new String[]{servId, status, requestId}, new int[]{12, 12, 12});
   }

   public int insertRequestId(String requestId, String cloudType, String name) {
      String sql = "insert into tbl_cloud_request(REQUEST_ID,CREATE_TYPE,CLOUD_TYPE,SERVER_NAME) values(?,?,?,?)";
      return this.jdbcTemplate.update(sql, new String[]{requestId, "createVolume", cloudType, name}, new int[]{12, 12, 12, 12});
   }

   public int insert(String msgId, CloudParam cloudParam, String creatType, String cloudType) {
      String sql = null;
      String[] args = null;
      int[] types = null;
      if ("createServerOnImage".equals(creatType)) {
         sql = "insert into tbl_cloud_request(REQUEST_ID,CREATE_TYPE,CLOUD_TYPE,SERVER_NAME,FLAVOR_ID,IMAGE_ID) values(?,?,?,?,?,?)";
         args = new String[]{cloudParam.getRequest_id(), creatType, cloudType, cloudParam.getServe_name(), cloudParam.getFlavor_id(), cloudParam.getImage_id()};
         types = new int[]{12, 12, 12, 12, 12, 12};
      } else if ("createServerOnVolume".endsWith(creatType)) {
         sql = "insert into tbl_cloud_request(REQUEST_ID,CREATE_TYPE,CLOUD_TYPE,SERVER_NAME,FLAVOR_ID,VOLUME_ID) values(?,?,?,?,?,?)";
         args = new String[]{cloudParam.getRequest_id(), creatType, cloudType, cloudParam.getServe_name(), cloudParam.getFlavor_id(), cloudParam.getVolume_id()};
         types = new int[]{12, 12, 12, 12, 12, 12};
      }

      this.logger.info("{}sql：{}", msgId, sql);
      return this.jdbcTemplate.update(sql, args, types);
   }

   public void createTpl(String msgId, String requestId, String servId, String servName, String cloudType) {
      String sql = "insert into tbl_cloud_request(REQUEST_ID,CLOUD_TYPE,SERVER_NAME) values(?,?,?)";
      String[] args = new String[]{requestId, cloudType, servName};
      int[] types = new int[]{12, 12, 12};
      this.jdbcTemplate.update(sql, args, types);
      this.threadPoolExecutor.execute(new Runnable() {
         public void run() {
            try {
               CloudManager cloudManager = CloudRoute.getCloudManager(cloudType);
               VolumeSnapshot volumeSnapshot = cloudManager.createSnapshot(servId, servName);
               CloudService.this.logger.info("{} 服务id:{} requestId:{} 更新结果{}", new Object[]{msgId, requestId, CloudService.this.updateServId(volumeSnapshot.getId(), requestId, "DONE")});
            } catch (Exception var3) {
               CloudService.this.logger.error("{}{}", msgId, var3);
            }

         }
      });
   }

   public void closeServ(String msgId, String servId, String cloud_type) {
      CloudManager cloudManager = CloudRoute.getCloudManager(cloud_type);
      cloudManager.shutOffServer(servId);
   }

   public void rebootServer(String msgId, String servId, String cloud_type) {
      CloudManager cloudManager = CloudRoute.getCloudManager(cloud_type);
      cloudManager.rebootServer(servId);
   }

   public void startServer(String msgId, String servId, String cloud_type) {
      CloudManager cloudManager = CloudRoute.getCloudManager(cloud_type);
      cloudManager.startServer(servId);
   }
}
