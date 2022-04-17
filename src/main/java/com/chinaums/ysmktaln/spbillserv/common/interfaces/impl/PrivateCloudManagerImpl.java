package com.chinaums.ysmktaln.spbillserv.common.interfaces.impl;

import com.chinaums.ysmktaln.spbillserv.common.interfaces.CloudManager;
import com.chinaums.ysmktaln.spbillserv.common.model.CloudProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.BlockDeviceMappingCreate;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.builder.BlockDeviceMappingBuilder;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateCloudManagerImpl implements CloudManager {
   private Logger logger = LoggerFactory.getLogger(PrivateCloudManagerImpl.class);
//   private OSClientV2 os;
   private OSClientV2 os;
   public PrivateCloudManagerImpl(CloudProperties cloudProperties) {
      try {
         this.os = (OSClientV2)((V2)((V2)OSFactory.builderV2().endpoint(cloudProperties.getAuthUrl())).credentials(cloudProperties.getUserName(), cloudProperties.getPassword())).tenantName(cloudProperties.getTenantName()).authenticate();
         this.logger.info("私有云初始化成功");
      } catch (Exception var3) {
         this.logger.error("私有云初始化失败" + var3);
         this.os = null;
      }

   }

   public Server showServer(String serverId) {
      return this.os.compute().servers().get(serverId);
   }

   public Volume createBootableVolume(String volumeName, String volumeSnapshotId) {
      return this.os.blockStorage().volumes().create((Volume)Builders.volume().name(volumeName).snapshot(volumeSnapshotId).bootable(true).build());
   }

   public Server createServerFormVolume(String serverName, String flavorId, String volumeId) {
      BlockDeviceMappingBuilder blockDeviceMappingBuilder = Builders.blockDeviceMapping().uuid(volumeId).deviceName("/dev/vda").bootIndex(0);
      List<? extends Network> networks = this.os.networking().network().list();
      List<String> networkId = new ArrayList();
      networkId.add(((Network)networks.get(0)).getId());
      ServerCreate sc = (ServerCreate)Builders.server().name(serverName).flavor(flavorId).networks(networkId).blockDevice((BlockDeviceMappingCreate)blockDeviceMappingBuilder.build()).build();
      return this.os.compute().servers().boot(sc);
   }

   public void deleteServer(String serverId) {
      this.os.compute().servers().delete(serverId);
   }

   public void deleteVolume(String volumeId) {
      this.os.blockStorage().volumes().delete(volumeId);
   }

   public Volume showVolume(String volumeId) {
      return this.os.blockStorage().volumes().get(volumeId);
   }

   public List<? extends Volume> listVolumes() {
      return this.os.blockStorage().volumes().list();
   }

   public VolumeSnapshot getVolumeSnapshot(String volumeSnapshotId) {
      return this.os.blockStorage().snapshots().get(volumeSnapshotId);
   }

   public List<? extends VolumeSnapshot> listVolumeSnapshots() {
      return this.os.blockStorage().snapshots().list();
   }

   public String createServerFromImage(String serverName, String flavorId, String imageId) {
      ServerCreate serverCreate = (ServerCreate)Builders.server().name(serverName).flavor(flavorId).image(imageId).build();
      return this.os.compute().servers().boot(serverCreate).getId();
   }

   public List<? extends Server> listServers() {
      return this.os.compute().servers().list();
   }

   public void rebootServer(String serverId) {
      this.os.compute().servers().reboot(serverId, RebootType.HARD);
   }

   public void shutOffServer(String serverId) {
      this.os.compute().servers().action(serverId, Action.STOP);
   }

   public void startServer(String serverId) {
      this.os.compute().servers().action(serverId, Action.START);
   }

   public VolumeSnapshot createSnapshot(String serverId, String snapshotName) {
      String imageId = this.os.compute().servers().createSnapshot(serverId, snapshotName);
      String volumeSnapshotId = (String)((Map)((ArrayList)((ArrayList)this.getImageByimageId(imageId).getMetaData().get("block_device_mapping"))).get(0)).get("snapshot_id");
      return this.getVolumeSnapshot(volumeSnapshotId);
   }

   public String showSnapshotStatus(String snapshotId) {
      return this.os.compute().images().get(snapshotId).getStatus().name().toLowerCase();
   }

   public Image getImageByimageId(String imageId) {
      return this.os.compute().images().get(imageId);
   }

   public VolumeSnapshot getVolumeSnapshotByimageId(String imageId) {
      String volumeSnapshotId = (String)((Map)((ArrayList)((ArrayList)this.getImageByimageId(imageId).getMetaData().get("block_device_mapping"))).get(0)).get("snapshot_id");
      return this.getVolumeSnapshot(volumeSnapshotId);
   }

   public List<? extends Image> listImages() {
      return this.os.compute().images().list();
   }

   public void deleteVolumeSnapshot(String snapshotId) {
      this.os.blockStorage().snapshots().delete(snapshotId);
   }
}
